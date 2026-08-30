package com.vannguyen.java_learn_ecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JavaLearnEcomApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaLearnEcomApplication.class, args);
	}
}