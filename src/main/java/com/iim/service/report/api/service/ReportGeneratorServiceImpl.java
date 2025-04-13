package com.iim.service.report.api.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import static com.iim.service.report.api.constant.Constant.PDF_FORMAT;

@Service
public class ReportGeneratorServiceImpl implements ReportGeneratorService {

    private final DataSource dataSource;

    @Value("${app.reports.directory}")
    private String workingDirectory;

    private final Logger LOGGER = LoggerFactory.getLogger(ReportGeneratorServiceImpl.class);

    public ReportGeneratorServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Resource generateReport(Long id, String reportName, String template, String format) {
        LOGGER.info("Started report generation...");
        String jrxmlFile = template + ".jrxml";
        String jasperFile = template + ".jasper";

        String inputPath = workingDirectory + File.separator + "templates" + File.separator + jrxmlFile;
        LOGGER.info("Input Path: {}", inputPath);

        try(InputStream reportStream = new FileInputStream(inputPath)) {

            LOGGER.info("InputStream: {}", reportStream);

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            saveJasperFileIntoWorkingDirectory(jasperFile);

            String subReportDirectory = workingDirectory + File.separator + "templates" + File.separator;

            LOGGER.info("Sub Report Directory: {}", subReportDirectory);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", id);
            parameters.put("SUBREPORT_DIR", subReportDirectory);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

            decideExportBasedOnInputFormat(format, reportName, jasperPrint);

        } catch (IOException ioException) {
            LOGGER.error("The file {} cannot be opened. Exception Message: {}", jrxmlFile, ioException.getMessage());
            return null;
        } catch (JRException jre) {
            LOGGER.error("The file {} cannot be compiled. Exception Message: {}", jrxmlFile, jre.getMessage());
            return null;
        } catch (SQLException sqle) {
            LOGGER.error("Report {} cannot be filled, problem while creating connection. Exception Message: {}", jasperFile, sqle.getMessage());
            return null;
        };

        LOGGER.info("Report created successfully");

        return retrieveReport(reportName, format);
    }

    private void saveJasperFileIntoWorkingDirectory(String jasperFile) {
        String path = workingDirectory + File.separator + "templates" + File.separator + jasperFile;
        try {
            JRSaver.saveObject(jasperFile, new FileOutputStream(path));
        } catch (FileNotFoundException fnfe) {
            LOGGER.error("Jasper file {} cannot be saved into {}. Exception Message: {}", jasperFile, path , fnfe.getMessage());
        } catch (JRException jre) {
            LOGGER.error("Something failed while saving the file {}. Exception Message: {}", jasperFile, jre.getMessage());
        }
    }

    private void decideExportBasedOnInputFormat(String format, String reportName, JasperPrint jasperPrint) {
        if (format.equals(PDF_FORMAT)) {
            exportPDFReport(jasperPrint, reportName);
        }
    }

    private void exportPDFReport(JasperPrint jasperPrint, String reportName) {
        String pdfFile = reportName + ".pdf";
        String outputPath = workingDirectory + File.separator + pdfFile;

        LOGGER.info("Exporting pdf file: {} into {}", reportName, outputPath);

        JRPdfExporter exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));
        try {
            exporter.exportReport();
        } catch (JRException jre) {
            LOGGER.error("Report {} cannot be exported, problem while creating connection. Exception Message: {}", jasperPrint, jre.getMessage());
        }
    }

    private Resource retrieveReport(String reportName, String format) {
        String report = reportName + "." + format;
        String reportPath = workingDirectory + File.separator + report;
        File file = new File(reportPath);

        try {
            if (file.exists())
                return new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException fnfe) {
            LOGGER.error("Report {} cannot be retrieved. Exception Message: {}", report, fnfe.getMessage());
        }

        return null;
    }
}
