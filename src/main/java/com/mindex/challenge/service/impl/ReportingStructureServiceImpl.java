package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating report for employee with id [{}]", id);

        //verify if this employee id exist.
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        // if employee exists, then start to generate the reports for that employee
        // by setting the employee and calculate the number of reports to a new Reporting Structure.
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(calculateTotalReports(employee));

        return reportingStructure;
    }

    // calculate the number of reports by going through employee who has the report recursively.
    private int calculateTotalReports(Employee employee){
        // This can get all info of employee instead of getting only employee ID with all null values.
        employee = employeeRepository.findByEmployeeId(employee.getEmployeeId());

        int numOfReports = 0;

        // loop through each of the direct report and check if that direct report has more reports.
        if (employee.getDirectReports() != null){
            for (Employee directReport: employee.getDirectReports()){
                numOfReports += 1;
                numOfReports += calculateTotalReports(directReport);
            }
        }

        return numOfReports;
    }
}
