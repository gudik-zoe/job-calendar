package com.work.calendar.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.work.calendar.dto.BusinessDTO;
import com.work.calendar.dto.BusinessFilterDTO;
import com.work.calendar.service.BusinessService;

@RestController
@RequestMapping("business")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BusinessController {

	private Logger log = LoggerFactory.getLogger(BusinessController.class);

//	private final String dateFormat = "yyyy-MM-dd HH:MM:SS";
//	private final String dateFormat2 = "yyyy-MM-dd HH:mm:ss.ssssss";
//	private final String standardDate = "yyyy-mm-dd";

	@Value("${dateformat}")
	private String dateFormat;
	@Autowired
	private BusinessService businessService;
	@Value("#{${monthsList}}")
	private List<String> monthsList;

//	SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
	@PostMapping("/")
	public ResponseEntity<?> addBusiness(@RequestBody BusinessDTO clientJobDTO) {
		try {
			return ResponseEntity.ok().body(businessService.addEntity(clientJobDTO));
		} catch (Exception e) {
			log.error("EXCEPTION on addBusiness: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@GetMapping("/summary")
	public ResponseEntity<?> getBusinessSummaryForClientId(
			@RequestParam(value = "clientId", required = false) Long clientId,
			@RequestParam(value = "jobId", required = false) Long jobId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "month", required = true) String month) {
		try {
			BusinessFilterDTO clientJobFilterDTO = buildclientJobFilterDTO(clientId, jobId, startDate, endDate, date,
					month);
			return ResponseEntity.ok().body(businessService.getBusinessSummary(clientJobFilterDTO));
		} catch (Exception e) {
			log.error("EXCEPTION on getClientJobSummaryForClientId: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@GetMapping("/")
	public ResponseEntity<?> getBusinessOnDate(@RequestParam(value = "date", required = true) String date) {
		String myFormat = "yyyy-MM-dd hh:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(myFormat, Locale.ENGLISH);
		try {
			return ResponseEntity.ok().body(businessService.getBusinessOnDate(formatter.parse(date.replace("T", " "))));
		} catch (Exception e) {
			log.error("EXCEPTION on getBusinessOnDate: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@GetMapping("/{businessId}")
	public ResponseEntity<?> getBusinessById(@PathVariable Long businessId) {
		try {
			return ResponseEntity.ok().body(businessService.findBusinessDtoById(businessId));
		} catch (Exception e) {
			log.error("EXCEPTION on getBusinessById: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editBusiness(@PathVariable Long id ,@RequestBody BusinessDTO businessDTO) {
		try {
			return ResponseEntity.ok().body(businessService.editBusiness(id ,businessDTO));
		} catch (Exception e) {
			log.error("EXCEPTION on editBusiness: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	private BusinessFilterDTO buildclientJobFilterDTO(Long clientId, Long jobId, String startingDate,
			String endingDate, String date, String month) {
		try {

//			String myFormat = "yyyy-MM-dd hh:mm:ss";
			Calendar calendar = Calendar.getInstance();
			calendar.set(2022, monthsList.indexOf(month), 1);
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
			Date startDate = startingDate != null ? formatter.parse(startingDate.replace("T", " "))
					: createStartDate(calendar, month);
			Date endDate = endingDate != null ? formatter.parse(endingDate.replace("T", " "))
					: createEndDate(calendar, month);
			if (startDate != null && endDate != null && endDate.getTime() < startDate.getTime()) {
				throw new Error("dates are not valid");
			}
			BusinessFilterDTO clientJobFilterDTO = new BusinessFilterDTO(clientId, jobId, startDate, endDate,
					calendar);
			return clientJobFilterDTO;
		} catch (Exception e) {
			log.info("error " + e.getMessage());
			return null;
		}
	}

	private Date createStartDate(Calendar calendar, String month) {
		int monthindex = monthsList.indexOf(month);
		calendar.set(calendar.get(Calendar.YEAR), monthindex, 1);
		java.util.Date utilDate = calendar.getTime();
		return utilDate;
	}

	private Date createEndDate(Calendar calendar, String month) {
		int monthindex = monthsList.indexOf(month);
		calendar.set(calendar.get(Calendar.YEAR), monthindex, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		java.util.Date utilDate = calendar.getTime();
		return utilDate;
	}

}
