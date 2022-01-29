package com.work.calendar.controller;

import java.text.ParseException;
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

@RestController
@RequestMapping("business")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BusinessController {

	private Logger log = LoggerFactory.getLogger(BusinessController.class);

	private final String dateFormat = "yyyy-MM-dd HH:MM:SS";
	private final String dateFormat2 = "yyyy-MM-dd HH:mm:ss.ssssss";

	@Autowired
	private BusinessService businessService;

	@PostMapping("/")
	public ResponseEntity<?> addJobClient(@RequestBody BusinessDTO clientJobDTO) {
		try {
			return ResponseEntity.ok().body(businessService.addEntity(clientJobDTO));
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@GetMapping("/summary")
	public ResponseEntity<?> getClientJobSummaryForClientId(
			@RequestParam(value = "clientId", required = false) Long clientId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "date", required = false) String date) {
		try {

			ClientJobFilterDTO clientJobFilterDTO = buildclientJobFilterDTO(clientId, startDate, endDate, date);
			return ResponseEntity.ok().body(businessService.getBusinessSummary(clientJobFilterDTO));

		} catch (Exception e) {
			log.error("EXCEPTION on getClientJobSummaryForClientId: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	private ClientJobFilterDTO buildclientJobFilterDTO(Long clientId, String startingDate, String endingDate,
			String date) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat , Locale.ENGLISH);
		log.info("after the change");
		Date theDate = date != null ? formatter.parse(date.replace('T' , ' ')) : null;
		Date startDate = startingDate != null ? formatter.parse(startingDate) : null;
		Date endDate = endingDate != null ? formatter.parse(endingDate) : null;
		if (startDate != null && endDate != null && endDate.getTime() < startDate.getTime()) {
			throw new Error("dates are not valid");
		}
		ClientJobFilterDTO clientJobFilterDTO = new ClientJobFilterDTO(clientId, startDate, endDate, theDate);
		log.info(clientJobFilterDTO.getDate()+ " " + clientJobFilterDTO.getEndDate());
		return clientJobFilterDTO;
	}

}
