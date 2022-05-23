package com.work.calendar.controller;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.calendar.aspect.UserHelper;
import com.work.calendar.aspect.WorkCalendarAPI;
import com.work.calendar.dto.ClientDTO;
import com.work.calendar.entity.Client;
import com.work.calendar.service.ClientService;

@RestController
@RequestMapping("client")
public class ClientController {

	private Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;

//	@GetMapping("/")
//	public ResponseEntity<?> getClients() {
//		try {
//			return ResponseEntity.ok().body(clientService.getClients());
//		} catch (Exception e) {
//			log.error("EXCEPTION on getClients: ", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
//		}
//	}
	
	@GetMapping("/{id}")
	@WorkCalendarAPI
	public ResponseEntity<?> getClientById(HttpServletRequest request, UserHelper userHelper,@PathVariable Long id) {
		try {
			return ResponseEntity.ok().body(clientService.getClientById(userHelper ,id));
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}
	
	@GetMapping("/")
	@WorkCalendarAPI
	public ResponseEntity<?> getClientsByUser(HttpServletRequest request, UserHelper userHelper) {
		try {
			return ResponseEntity.ok().body(clientService.getClientsByUser(userHelper));
		} catch (Exception e) {
			log.error("EXCEPTION on getClientsByUser: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured in getClientsByUser"));
		}
	}
	@PutMapping("/")
	@WorkCalendarAPI
	public ResponseEntity<?> updateClient(HttpServletRequest request, UserHelper userHelper,@RequestBody Client clientDTO) {
		try {
			return ResponseEntity.ok().body(clientService.updateClient(userHelper ,clientDTO));
		} catch (Exception e) {
			log.error("EXCEPTION on updateClient: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}


	@PostMapping("/")
	@WorkCalendarAPI
	public ResponseEntity<?> addClient(HttpServletRequest request, UserHelper userHelper,@RequestBody ClientDTO clientDTO) {
		try {
			Client client = clientService.addClient(userHelper ,clientDTO);
			return ResponseEntity.ok().body(client);
		} catch (Exception e) {
			log.error("EXCEPTION on getClients: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Error("an unknown error occured"));
		}
	}

	@DeleteMapping("/{id}")
	@WorkCalendarAPI
	public void deleteClient(HttpServletRequest request, UserHelper userHelper,@PathVariable Long id) {
		try {
			System.out.println("in controller " + id);
			clientService.deleteClient(userHelper ,id);
		} catch (Exception e) {
			log.error("EXCEPTION on deleteClient: ", e);
		}
	}

}
