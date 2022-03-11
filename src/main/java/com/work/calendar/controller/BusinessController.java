package com.work.calendar.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
	private final String standardDate = "yyyy-mm-dd";
	SimpleDateFormat formatter = new SimpleDateFormat(dateFormat2, Locale.ENGLISH);

	@Autowired
	private BusinessService businessService;
	@Value("#{${monthsList}}")
	private List<String> monthsList;

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
			@RequestParam(value = "jobId", required = false) Long jobId,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "month", required = true) String month) {
		try {
			log.info("month index " + month);
			ClientJobFilterDTO clientJobFilterDTO = buildclientJobFilterDTO(clientId, jobId, startDate, endDate, date,
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
			log.info("arriving here");
			return ResponseEntity.ok().body(businessService.findBusinessById(businessId));
		} catch (Exception e) {
			log.error("EXCEPTION on getBusinessById: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	private ClientJobFilterDTO buildclientJobFilterDTO(Long clientId, Long jobId, String startingDate,
			String endingDate, String date, String month) {
		try {

			String myFormat = "yyyy-MM-dd hh:mm:ss";

//			Date theStartDay = java.util.Date.from(firstDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Calendar calendar = Calendar.getInstance();
			calendar.set(2022, monthsList.indexOf(month), 1);
//			Date theLastDay = java.util.Date.from(lastDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
//			log.info("first day " + theStartDay + " lastDay  " + theLastDay);
			SimpleDateFormat formatter = new SimpleDateFormat(myFormat, Locale.ENGLISH);
			Date startDate = startingDate != null ? formatter.parse(startingDate.replace("T", " "))
					: createStartDate(month);
//			log.info("start day " + startDate);
			Date endDate = endingDate != null ? formatter.parse(endingDate.replace("T", " ")) : createEndDate(month);
//			log.info("endDate" + endingDate);
			if (startDate != null && endDate != null && endDate.getTime() < startDate.getTime()) {
				throw new Error("dates are not valid");
			}
			ClientJobFilterDTO clientJobFilterDTO = new ClientJobFilterDTO(clientId, jobId, startDate, endDate,
					calendar);
			return clientJobFilterDTO;
		} catch (Exception e) {
			log.info("error " + e.getMessage());
			return null;
		}
	}

	private Date createStartDate(String month) {
		Calendar calendar = Calendar.getInstance();
		int monthindex = monthsList.indexOf(month);
		calendar.set(2022, monthindex, 1);
		java.util.Date utilDate = calendar.getTime();
//		LocalDate firstDay = LocalDate.of(2022, monthindex + 1, 1);
		log.info("start date " + utilDate);
		return utilDate;

	}

	private Date createEndDate(String month) {
		Calendar calendar = Calendar.getInstance();
		int monthindex = monthsList.indexOf(month);
		calendar.set(2022, monthindex, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		java.util.Date utilDate = calendar.getTime();
		log.info("last date " + utilDate);
		return utilDate;
//		LocalDate lastDay = LocalDate.of(2022, monthindex + 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	}

}
