package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureIdUrl;
    private String employeeIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureIdUrl = "http://localhost:" + port + "/reporting/{id}";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testGetNumOfReports() {
        // test with John Lennon by using his employeeId.
        String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, employeeId).getBody();

        // Make sure readEmployee is not Null by using that employeeId
        assertNotNull(readEmployee);

        // Get number of reports based on the employeeId
        ReportingStructure report = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, employeeId).getBody();

        // Test if report.getEmployee == null
        assertNotNull(report.getEmployee());

        // Test if John's ID does not match with report's employee's id.
        assertEquals(employeeId, report.getEmployee().getEmployeeId());

        // Test if the number of reports does not match.
        assertEquals(4, report.getNumberOfReports());
    }

}
