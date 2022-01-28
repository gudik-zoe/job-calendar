package com.work.calendar.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.calendar.entity.Job;
import com.work.calendar.service.JobService;

@RestController
@RequestMapping("job")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JobController {

	private Logger log = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private JobService jobTypeService;

	@GetMapping("/")
	public ResponseEntity<?> getJobTypes() {
		try {
			return ResponseEntity.ok().body(jobTypeService.getJobTypes());
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@PostMapping()
	public ResponseEntity<?> addJobType(@RequestBody Job theJobType) {
		try {
			theJobType.setTimeStamp(new Date());
			Job jobType = jobTypeService.addJobType(theJobType);
			return ResponseEntity.ok().body(jobType);
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@DeleteMapping("/{id}")
	public void deleteJob(@PathVariable Long id) {

		try {
			log.info("this is enter the delete job controller " + id);
			jobTypeService.deleteJob(id);
		} catch (Exception e) {
			log.error("EXCEPTION on delete jobType: ", e.getMessage());
		}
	}

}
