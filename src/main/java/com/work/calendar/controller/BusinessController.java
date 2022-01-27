package com.work.calendar.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.dto.ClientJobFilterDTO;
import com.work.calendar.service.BusinessService;

import antlr.StringUtils;

@RestController
@RequestMapping("clientJob")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BusinessController {

	private Logger log = LoggerFactory.getLogger(BusinessController.class);

	private final String dateFormat = "yyyy-MM-dd HH:mm:ss.ssssss";

	@Autowired
	private BusinessService clientJobService;

	@PostMapping("/")
	public ResponseEntity<?> addJobClient(@RequestBody BusinessDTO clientJobDTO) {
		try {
			return ResponseEntity.ok().body(clientJobService.addEntity(clientJobDTO));
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@GetMapping("/summary")
	public ResponseEntity<?> getClientJobSummaryForClientId(
			@RequestParam(value = "clientId", required = true) Long clientId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			ClientJobFilterDTO clientJobFilterDTO = new ClientJobFilterDTO(clientId,
					startDate != null ? formatter.parse(startDate) : null,
					endDate != null ? formatter.parse(endDate) : null);

			return ResponseEntity.ok().body(clientJobService.getClientSummaryOnSpecificPeriod(clientJobFilterDTO));

		} catch (Exception e) {
			log.error("EXCEPTION on getClientJobSummaryForClientId: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

}
