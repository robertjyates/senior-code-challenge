package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Override
    public ReportingStructure create(ReportingStructure reportingStructure) {
        LOG.debug("Creating reporting structure [{}]", reportingStructure);

        Employee employee = reportingStructure.getEmployee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);
        
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(computeNumOfReports(employee));
        
        return reportingStructure;
    }

    @Override
    public ReportingStructure read(String id) {    	
        LOG.debug("Creating reporting structure with employeeId [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid reporting structure with employeeId: " + id);
        }
      
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(computeNumOfReports(employee));

        return reportingStructure;
    }

    @Override
    public ReportingStructure update(ReportingStructure reportingStructure) {
        LOG.debug("Updating reporting structure [{}]", reportingStructure);
        
        Employee employee = employeeRepository.save(reportingStructure.getEmployee());
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(computeNumOfReports(employee));
        
        return reportingStructure;
    }
    
    private int computeNumOfReports(Employee employee)
    {
        int numOfReports = 0;
        if (employee.getDirectReports() != null) {
            numOfReports += employee.getDirectReports().size();
   		    for(Employee innerEmployee : employee.getDirectReports()) {
                ReportingStructure innerReportingStructure = read(innerEmployee.getEmployeeId());
                numOfReports += innerReportingStructure.getNumberOfReports();
   	        }
        }
        return numOfReports;
    }
}
