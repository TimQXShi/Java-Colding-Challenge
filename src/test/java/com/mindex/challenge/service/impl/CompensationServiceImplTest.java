package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.CompletionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationWithEmployeeIdUrl;
    private String employeeIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationWithEmployeeIdUrl = "http://localhost:" + port + "/compensation/{id}";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testCreateReadCompensation() {
        // Get a exists employee
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeId).getBody();

        // Make sure readEmployee is not Null
        assertNotNull(readEmployee);

        // Create test compensation that uses the employee above
        Date tempDate = new Date();
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(employeeId);
        testCompensation.setEmployee(readEmployee);
        testCompensation.setSalary(8000);
        testCompensation.setEffectiveDate(tempDate);

        // Create compensation check
        Compensation createCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        // Test if createCompensation is null
        assertNotNull(createCompensation);

        // Test if testCompensation not same as the createCompensation
        assertCompensationEquivalence(testCompensation, createCompensation);

        // Read compensation check by using the employeeId above
        Compensation readCompensation = restTemplate.getForEntity(compensationWithEmployeeIdUrl, Compensation.class, employeeId).getBody();

        // Test if readCompensation is null
        assertNotNull(readCompensation);

        // Test if createCompensation not same as the readCompensation
        assertCompensationEquivalence(createCompensation, readCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getEmployee().getFirstName(), actual.getEmployee().getFirstName());
        assertEquals(expected.getEmployee().getLastName(), actual.getEmployee().getLastName());
        assertEquals(expected.getEmployee().getDepartment(), actual.getEmployee().getDepartment());
        assertEquals(expected.getEmployee().getPosition(), actual.getEmployee().getPosition());
        assertEquals(expected.getSalary(), actual.getSalary(), 0.00001);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
