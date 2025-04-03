package com.iim.service.report.api.service;

import org.springframework.stereotype.Service;

@Service
public class ReportGeneratorServiceImpl implements ReportGeneratorService {
    @Override
    public byte[] generateReport(Long id, String reportName, String template, String format) {
        return new byte[0];
    }
}
