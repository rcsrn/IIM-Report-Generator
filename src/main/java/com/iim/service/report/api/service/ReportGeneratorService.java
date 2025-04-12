package com.iim.service.report.api.service;

import org.springframework.core.io.Resource;

public interface ReportGeneratorService {

    Resource generateReport(Long id, String reportName, String template, String format);
}
