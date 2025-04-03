package com.iim.service.report.api.service;

public interface ReportGeneratorService {

    byte[] generateReport(Long id, String reportName, String template, String format);
}
