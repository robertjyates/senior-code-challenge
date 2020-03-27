package com.mindex.challenge.data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Compensation {
    private Employee employee;
    private long salary;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/New_York")
    private Date effectiveDate;
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setSalary(long salary) {
    	this.salary = salary;
    }
    
    public long getSalary() {
    	return salary;
    }
    
    public void setEffectiveDate(Date effectiveDate) {
    	this.effectiveDate = effectiveDate;
    }
    
    public Date getEffectiveDate() {
    	return effectiveDate;
    }
}
