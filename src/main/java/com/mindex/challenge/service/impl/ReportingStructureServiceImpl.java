package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.exceptions.CircularDependencyException;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
    public int count = 0;

    @Autowired
    private EmployeeRepository employeeRepository;

    public ReportingStructure read(String employeeId) {
        LOG.debug("Creating in memory Reporting Structure for [{}]", employeeId);
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        Map<String, Object> reportingStructureMap = new HashMap<>();

        try {
            reportingStructureMap.put(getEmployeeName(employee), processDirectReports(employee));
        } catch (StackOverflowError e) {
            LOG.error("Circular dependency found in the reporting structure", e);
            throw new CircularDependencyException(employeeId);
        }
        reportingStructure.setReportingStructureMap(reportingStructureMap);
        reportingStructure.setNumberOfReports(count);
        return reportingStructure;
    }

    private Map<String, Object> processDirectReports(Employee employee){
        Map<String, Object> reportingStructureMap = new HashMap<>();

        try {
            List<Employee> directReports = employee.getDirectReports();
            for (Employee listedDirectReport : directReports) {
                Employee fullEmployeeData = employeeRepository.findByEmployeeId(listedDirectReport.getEmployeeId());
                count++;
                reportingStructureMap.put(getEmployeeName(fullEmployeeData), processDirectReports(fullEmployeeData)
                );
            }
        } catch (Exception e) {
            LOG.debug(String.format("Employee %s: has no direct reports. Error message: %s", employee.getEmployeeId(), e.getMessage()));
        }
        return reportingStructureMap;
    }

    private String getEmployeeName(Employee employee) {
        if (employee.getFirstName() != null && employee.getLastName() != null) {
            return String.format("%s %s", employee.getFirstName(), employee.getLastName());
        } else {
            return String.format("%s does not have a full name", employee.getEmployeeId());
        }
    }
}