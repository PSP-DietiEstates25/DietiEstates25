package com.dietiestates.resourceserver.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "aspect")
@EnableAspectJAutoProxy
public class AspectConfig {

}
