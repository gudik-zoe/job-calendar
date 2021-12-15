package com.work.calendar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.calendar.service.ClientService;

@Controller
@RequestMapping("client")
public class ClientController {

	private Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;

	public ResponseEntity<?> getClients() {
		try {
			return ResponseEntity.ok().body(clientService);
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

}
