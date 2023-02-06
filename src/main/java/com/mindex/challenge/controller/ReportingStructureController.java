package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.CircularDependencyException;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructure.class);

    @Autowired
    private ReportingStructureService reportingStructureService;

    @GetMapping("/reportingStructure/{employeeId}")

    public ReportingStructure read(@PathVariable String employeeId) {
        LOG.info("Received getReportingStructure request for id [{}]", employeeId);
        return reportingStructureService.read(employeeId);
    }
}