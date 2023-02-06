package com.mindex.challenge.data;

import java.util.Map;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;
    private Map<String, Object> reportingStructureMap;

    public ReportingStructure() {
    }

    public Employee getEmployee() {return employee;}

    public void setEmployee(Employee employee) {this.employee=employee;}

    public int getNumberOfReports() {return numberOfReports;}

    public void setNumberOfReports(int numberOfReports) {this.numberOfReports=numberOfReports;}

    public Map<String, Object> getReportingStructureMap() {return reportingStructureMap;}

    public void setReportingStructureMap(Map<String, Object> reportingStructureMap) {this.reportingStructureMap=reportingStructureMap;}
}