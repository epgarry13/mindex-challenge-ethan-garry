package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;

    private Employee createdHappyEmployee1;
    private Employee createdSadEmployee1;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        String employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";

        Employee happyEmployee1 = new Employee();
        Employee happyEmployee2 = new Employee();
        Employee happyEmployee3 = new Employee();
        Employee happyEmployee4 = new Employee();
        Employee sadEmployee1 = new Employee();

        happyEmployee1.setFirstName("John");
        happyEmployee1.setLastName("Doe");
        happyEmployee2.setFirstName("Ethan");
        happyEmployee2.setLastName("Garry");
        happyEmployee3.setFirstName("Joe");
        happyEmployee3.setLastName("Shmo");
        happyEmployee4.setFirstName("Larry");
        happyEmployee4.setLastName("Miami");

        sadEmployee1.setFirstName("NoLastName");

        // Create employees with good reporting structure
        Employee createdHappyEmployee4 = restTemplate.postForEntity(employeeUrl, happyEmployee4, Employee.class).getBody();
        Employee createdHappyEmployee3 = restTemplate.postForEntity(employeeUrl, happyEmployee3, Employee.class).getBody();

        happyEmployee2.setDirectReports(Collections.singletonList(createdHappyEmployee4));
        Employee createdHappyEmployee2 = restTemplate.postForEntity(employeeUrl, happyEmployee2, Employee.class).getBody();

        happyEmployee1.setDirectReports(Arrays.asList(createdHappyEmployee2, createdHappyEmployee3));
        createdHappyEmployee1 = restTemplate.postForEntity(employeeUrl, happyEmployee1, Employee.class).getBody();

        // create employee with no full name
        createdSadEmployee1 = restTemplate.postForEntity(employeeUrl, sadEmployee1, Employee.class).getBody();
    }

    @Test
    public void testReportingStructureServiceWithGoodInput() {

        // happy case with no issue
        Map<String, Object> happyReportingStructure = new HashMap<>();
        happyReportingStructure.put("John Doe",
                new HashMap<String, Object>() {{
                    put("Joe Shmo", new HashMap<String, Object>());
                    put("Ethan Garry",
                            new HashMap<String, Object>() {{
                                put("Larry Miami", new HashMap<String, Object>());
                            }});
                }});
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdHappyEmployee1.getEmployeeId()).getBody();
        assertEquals(readReportingStructure.getReportingStructureMap(), happyReportingStructure);
    }

    @Test
    public void testReportingStructureServiceWithNoFullName() {

        // text replacement when no full name provided
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdSadEmployee1.getEmployeeId()).getBody();
        String firstKey = String.valueOf(readReportingStructure.getReportingStructureMap().entrySet().iterator().next());
        assertTrue(firstKey.contains("does not have a full name"));
    }
}