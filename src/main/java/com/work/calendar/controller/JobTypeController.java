package com.work.calendar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.work.calendar.entity.JobType;
import com.work.calendar.service.JobTypeService;

@Controller
@RequestMapping("job")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JobTypeController {

	private Logger log = LoggerFactory.getLogger(JobTypeController.class);

	@Autowired
	private JobTypeService jobTypeService;

	@GetMapping("/")
	public ResponseEntity<?> getClients() {
		try {
			return ResponseEntity.ok().body(jobTypeService.getEntities());
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@PostMapping("/")
	public ResponseEntity<?> addJobType(@RequestBody JobType theJobType) {
		try {
			JobType jobType = jobTypeService.addEntity(theJobType);
			return ResponseEntity.ok().body(jobType);
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

}
