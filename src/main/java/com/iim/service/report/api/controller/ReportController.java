package com.iim.service.report.api.controller;

import com.iim.service.report.api.service.ReportGeneratorService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

import javax.sql.DataSource;

import static com.iim.service.report.api.constant.Path.baseUrl;
import static com.iim.service.report.api.constant.Path.createReportMapping;

@RestController
@RequestMapping(baseUrl)
public class ReportController {

    private ReportGeneratorService reportGeneratorService;

    public ReportController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @GetMapping(createReportMapping)
    public ResponseEntity<?> createReport(@PathVariable Long id,
                                          @RequestParam String template,
                                          @RequestParam String name,
                                          @RequestParam String format) {
        LOGGER.info("{Start Report Generator Application}");
        LOGGER.info("Params: {}, {}, {}, {}", id, template, name, format);

        return ResponseEntity.ok().body(reportGeneratorService.generateReport(id, name, template, format));
    }
}
