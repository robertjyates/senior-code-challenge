package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;
    private String reportingStructureIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
    	reportingStructureUrl = "http://localhost:" + port + "/reportingStructure";
    	reportingStructureIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
    	Employee testEmployeeA = new Employee();
        testEmployeeA.setFirstName("Aaron");
        testEmployeeA.setLastName("Anders");
        testEmployeeA.setDepartment("Engineering");
        testEmployeeA.setPosition("Developer");
        ReportingStructure testReportingStructureA = new ReportingStructure();
        testReportingStructureA.setEmployee(testEmployeeA);
        ReportingStructure createdReportingStructureA = restTemplate.postForEntity(reportingStructureUrl, testReportingStructureA, ReportingStructure.class).getBody();
        
    	Employee testEmployeeB = new Employee();
        testEmployeeB.setFirstName("Brian");
        testEmployeeB.setLastName("Billings");
        testEmployeeB.setDepartment("Engineering");
        testEmployeeB.setPosition("Developer");
        ReportingStructure testReportingStructureB = new ReportingStructure();
        testReportingStructureB.setEmployee(testEmployeeB);
        ReportingStructure createdReportingStructureB = restTemplate.postForEntity(reportingStructureUrl, testReportingStructureB, ReportingStructure.class).getBody();

        Employee testEmployeeC = new Employee();
        testEmployeeC.setFirstName("Carly");
        testEmployeeC.setLastName("Carlson");
        testEmployeeC.setDepartment("Engineering");
        testEmployeeC.setPosition("Developer");
        testEmployeeC.setDirectReports(Arrays.asList(createdReportingStructureA.getEmployee(), createdReportingStructureB.getEmployee()));
        ReportingStructure testReportingStructureC = new ReportingStructure();
        testReportingStructureC.setEmployee(testEmployeeC);
        ReportingStructure createdReportingStructureC = restTemplate.postForEntity(reportingStructureUrl, testReportingStructureC, ReportingStructure.class).getBody();

    	Employee testEmployeeD = new Employee();
        testEmployeeD.setFirstName("Debra");
        testEmployeeD.setLastName("Daniels");
        testEmployeeD.setDepartment("Engineering");
        testEmployeeD.setPosition("Developer");
        ReportingStructure testReportingStructureD = new ReportingStructure();
        testReportingStructureD.setEmployee(testEmployeeD);
        ReportingStructure createdReportingStructureD = restTemplate.postForEntity(reportingStructureUrl, testReportingStructureD, ReportingStructure.class).getBody();
        
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.setDirectReports(Arrays.asList(createdReportingStructureC.getEmployee(), createdReportingStructureD.getEmployee()));
        ReportingStructure testReportingStructure = new ReportingStructure();
        testReportingStructure.setEmployee(testEmployee);

        // Create checks
        ReportingStructure createdReportingStructure = restTemplate.postForEntity(reportingStructureUrl, testReportingStructure, ReportingStructure.class).getBody();
        Employee createdEmployee = createdReportingStructure.getEmployee();
        assertNotNull(createdEmployee.getEmployeeId());
        assertEquals(4, createdReportingStructure.getNumberOfReports());
        assertEmployeeEquivalence(testEmployee, createdEmployee);

        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        Employee readEmployee = readReportingStructure.getEmployee();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEquals(createdReportingStructure.getNumberOfReports(), readReportingStructure.getNumberOfReports());
        assertEmployeeEquivalence(createdEmployee, readEmployee);
        
        // Update checks
        final String newPosition = "Development Manager";
        readEmployee.setPosition(newPosition);
        readReportingStructure.setEmployee(readEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure updatedReportingStructure =
                restTemplate.exchange(reportingStructureIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<ReportingStructure>(readReportingStructure, headers),
                        ReportingStructure.class,
                        readReportingStructure.getEmployee().getEmployeeId()).getBody();

        Employee updatedEmployee = updatedReportingStructure.getEmployee();
        assertEmployeeEquivalence(readReportingStructure.getEmployee(), updatedEmployee);
        assertEquals(readReportingStructure.getEmployee().getPosition(), updatedEmployee.getPosition());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
