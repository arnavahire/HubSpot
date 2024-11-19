package com.hubspot.assessment.Configurations;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;

/*
 * Configuration Class for OkHttpClient - Dependency injected into HubSpotApiService by creating @Bean 
 * 
 * This is used to create HTTP Request and Response objects through which we can communicate with the APIs
 */
@Configuration
public class OkHttpClientConfig {

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
    }
    
}
/*
Note: If we do not define these configuration classes and directly try to use objects of OkHttpClient or ObjectMapper, then we run into the following error: 
------------------------------------------------------------------------------------------------------------------------------------------------------------
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in com.hubspot.assessment.Services.HubSpotApiService required a bean of type 'okhttp3.OkHttpClient' that could not be found.


Action:

Consider defining a bean of type 'okhttp3.OkHttpClient' in your configuration.
 * 
 */