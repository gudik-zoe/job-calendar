package com.work.calendar.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.work.calendar.aspect.UserHelper;
import com.work.calendar.aspect.WorkCalendarAPI;
import com.work.calendar.dto.LoginDto;
import com.work.calendar.entity.User;
import com.work.calendar.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	private Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping("/userData")
	@WorkCalendarAPI
	public ResponseEntity<?> getUserData(HttpServletRequest request, UserHelper userHelper) {
		try {
			return ResponseEntity.ok().body(userService.getUserById(userHelper.getId()));
		} catch (Exception e) {
			log.error("EXCEPTION on getUserById: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Error("an unknown error occured in getUserById"));
		}
	}

	@PostMapping("/")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		try {
			return ResponseEntity.ok().body(userService.addUser(user));
		} catch (Exception e) {
			log.error("EXCEPTION on addUser: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Error("an unknown error occured in addUser"));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		try {
			return ResponseEntity.ok().body(userService.login(loginDto));
		} catch (Exception e) {
			log.error("EXCEPTION on login: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Error("an unknown error occured in login"));
		}
	}
}
