package com.mindex.challenge;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompensationDataBootstrapTest {

    @Autowired
    private CompensationDataBootstrap compensationDataBootstrap;

    @Autowired
    private CompensationRepository compensationRepository;

    @Before
    public void init() {
    	compensationDataBootstrap.init();
    }

    @Test
    public void test() throws ParseException {
        Compensation compensation = compensationRepository.findByEmployee_EmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        assertNotNull(compensation);
        assertEquals(160000, compensation.getSalary());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(simpleDateFormat.parse("2010-07-19"), compensation.getEffectiveDate());
        
        Employee employee = compensation.getEmployee();
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Lennon", employee.getLastName());
        assertEquals("Development Manager", employee.getPosition());
        assertEquals("Engineering", employee.getDepartment());        
    }
}
