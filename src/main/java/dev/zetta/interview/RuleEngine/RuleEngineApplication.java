package dev.zetta.interview.RuleEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RuleEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuleEngineApplication.class, args);
	}

}
