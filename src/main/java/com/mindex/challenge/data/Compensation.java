package com.mindex.challenge.data;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


public class Compensation {
    private String compensationId;
    private Employee employee;
    private int salary;

    private LocalDate effectiveDate;

    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public int getSalary() { return this.salary; }

    public LocalDate getEffectiveDate() { return this.effectiveDate; }

    public void setEmployee(Employee employee) { this.employee=employee; }

    public void setSalary(int salary) { this.salary=salary; }

    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate=effectiveDate; }
}