package com.hubspot.assessment.Configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * CustomAppProperties - Configruation class to specify any app properties that we would like to use. 
 * 
 * These are specified in application.properties and can be used in code using this class. 
 * For ex: we use user key in our service while calling the GET and POST Endpoints. We don't want to hardcode it.
 */
@Component
@ConfigurationProperties(prefix = "custom.hubspot.app")
public class CustomAppProperties {
    private String userKey;

    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
