package com.work.calendar.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.calendar.entity.Client;
import com.work.calendar.service.ClientService;

@RestController
@RequestMapping("client")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

	private Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;

	@GetMapping("/")
	public ResponseEntity<?> getClients() {
		try {
			return ResponseEntity.ok().body(clientService.getClients());
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getClientById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok().body(clientService.getClientById(id));
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@PostMapping("/")
	public ResponseEntity<?> addClient(@RequestBody Client theClient) {
		try {
			Client client = clientService.addClient(theClient);
			return ResponseEntity.ok().body(client);
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@DeleteMapping("/{id}")
	public void deleteClient(@PathVariable Long id) {
		try {
			System.out.println("in controller " + id);
			clientService.deleteClient(id);
		} catch (Exception e) {
			log.error("EXCEPTION on deleteClient: ", e);
		}
	}

}
