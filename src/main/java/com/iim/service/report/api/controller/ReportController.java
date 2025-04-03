package com.iim.service.report.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.iim.service.report.api.constant.Path.baseUrl;

@RestController
@RequestMapping(baseUrl)
public class ReportController {

    @GetMapping
    public ResponseEntity<?> createReport() {
        return null;
    }
}
