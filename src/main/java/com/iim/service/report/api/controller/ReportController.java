package com.iim.service.report.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.iim.service.report.api.constant.Path.baseUrl;
import static com.iim.service.report.api.constant.Path.createReportMapping;

@RestController
@RequestMapping(baseUrl)
public class ReportController {

    @GetMapping(createReportMapping)
    public ResponseEntity<?> createReport(@PathVariable String id, @RequestParam String template, @RequestParam String format) {
        return null;
    }
}
