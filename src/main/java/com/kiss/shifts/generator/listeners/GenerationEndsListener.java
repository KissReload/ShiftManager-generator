package com.kiss.shifts.generator.listeners;

import org.slf4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kiss.shifts.utils.ShiftsType;

@Component
public class GenerationEndsListener extends JobExecutionListenerSupport {

	@Autowired
	Logger log;
//	private final JdbcTemplate jdbcTemplate;

//	@Autowired
//	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
//		this.jdbcTemplate = jdbcTemplate;
//	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");
		}
	}
}