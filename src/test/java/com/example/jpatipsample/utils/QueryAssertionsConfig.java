package com.example.jpatipsample.utils;

import com.example.jpatipsample.utils.support.ConnectionProxyAspect;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@TestConfiguration
@EnableAspectJAutoProxy
public class QueryAssertionsConfig {
    @Bean
    public ConnectionProxyAspect connectionProxyAspect() {
        return new ConnectionProxyAspect();
    }
}
