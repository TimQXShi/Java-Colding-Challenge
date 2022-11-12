package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Create compensation.
    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensationRepository.insert(compensation);

        return compensation;
    }

    // Read compensation by employeeId.
    @Override
    public Compensation read(String id) {
        LOG.debug("Getting compensation with id [{}]", id);

        // Get compensation by using compensationRepository.
        Compensation compensation = compensationRepository.findCompensationByEmployeeId(id);

        // Check compensation exists with that employeeId
        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }


}
