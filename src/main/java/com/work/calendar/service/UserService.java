package com.work.calendar.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.calendar.dto.LoginDto;
import com.work.calendar.dto.SignUpDto;
import com.work.calendar.dto.TokenDto;
import com.work.calendar.dto.UserTokenInfo;
import com.work.calendar.entity.User;
import com.work.calendar.exceptions.BadRequestException;
import com.work.calendar.exceptions.MyPlatformException;
import com.work.calendar.exceptions.NotFoundException;
import com.work.calendar.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

	private Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenService tokenService;

	public User getUserById(Long id) throws NotFound {
		if (userRepository.findById(id).isPresent()) {
			return userRepository.findById(id).get();
		}
		throw new NotFoundException("no user with the id " + id);
	}

	public User addUser(SignUpDto signUpDto) {
		if (checkUser(signUpDto)) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			User theUser = new User();
			theUser.setFullName(signUpDto.getFullName());
			theUser.setPassword(encoder.encode(signUpDto.getPassword()));
			theUser.setEmail(signUpDto.getEmail());
			return userRepository.save(theUser);
		}
		throw new MyPlatformException("insert valid user");

	}

	public boolean checkUser(SignUpDto signUpDto) {
		if (checkPassword(signUpDto.getPassword(), signUpDto.getConfirmPassword())
				&& !StringUtils.isEmpty(signUpDto.getFullName()) && checkEmail(signUpDto.getEmail())) {
			return true;
		}
		return false;
	}

	private boolean checkEmail(String email) {
		log.info(email);
		if (!StringUtils.isEmpty(email)) {
			User user = userRepository.findUserByEmail(email);
			if (user != null) {
				throw new MyPlatformException("email already exists");
			}
			return true;
		}
		return false;
	}

	public boolean checkPassword(String password, String confirmPassword) {
		String passwordPattern = "^(?=.*[0-7])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
		if (!StringUtils.isEmpty(password) && password.matches(passwordPattern) && password.equals(confirmPassword)) {
			return true;
		}
		throw new MyPlatformException("invalid password");
	}

	public TokenDto login(LoginDto loginDto) throws Exception {
		User theUser = userRepository.findUserByEmail(loginDto.getEmail());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean check = encoder.matches(loginDto.getPassword(), theUser.getPassword());
		if (theUser != null && check) {
			TokenDto tokenDto = tokenService.createToken(theUser);
			return tokenDto;
		}
		throw new BadRequestException("invalid credentials");

	}

}
