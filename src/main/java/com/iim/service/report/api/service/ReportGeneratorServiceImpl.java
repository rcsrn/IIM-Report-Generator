package com.iim.service.report.api.service;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class ReportGeneratorServiceImpl implements ReportGeneratorService {

    private DataSource dataSource;

    public ReportGeneratorServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public byte[] generateReport(Long id, String reportName, String template, String format) {
        return new byte[0];
    }
}
