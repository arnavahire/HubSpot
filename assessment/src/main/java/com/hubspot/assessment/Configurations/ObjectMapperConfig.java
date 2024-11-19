package com.hubspot.assessment.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * Configuration Class for Jackson Databind ObjectMapper - Dependency injected into HubSpotApiService by creating @Bean. 
 * 
 * This is used to serialize and serialize data obtained from API and map to our models 
 */

@Configuration
public class ObjectMapperConfig {
    
    @Bean
    public ObjectMapper ObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
}
