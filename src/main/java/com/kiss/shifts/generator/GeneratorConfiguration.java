package com.kiss.shifts.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class GeneratorConfiguration {
	
	@Bean
	Logger initLogger (){
		return LoggerFactory.getLogger(this.getClass());
	}
	
	

}
