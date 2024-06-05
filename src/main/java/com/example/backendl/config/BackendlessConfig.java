package com.example.backendl.config;

import com.backendless.Backendless;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Configuration
@PropertySource("application.properties")
public class BackendlessConfig {

    @Value("${subDom}")
    private String subDom;

    @PostConstruct
    public void init() {
       Backendless.initApp(subDom);
    }
}



