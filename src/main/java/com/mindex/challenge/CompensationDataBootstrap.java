package com.mindex.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

@Component
public class CompensationDataBootstrap {
    private static final String DATASTORE_LOCATION = "/static/compensation_database.json";

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION);

        Compensation[] compensations = null;

        try {
        	compensations = objectMapper.readValue(inputStream, Compensation[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Compensation compensation : compensations) {
        	compensationRepository.insert(compensation);
        }
    }
}
