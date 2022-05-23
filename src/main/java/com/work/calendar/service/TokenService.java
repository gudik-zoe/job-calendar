package com.work.calendar.service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.calendar.aspect.UserHelper;
import com.work.calendar.dto.TokenDto;
import com.work.calendar.dto.UserTokenInfo;
import com.work.calendar.entity.User;
import com.work.calendar.exceptions.MyPlatformException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	private static Logger log = LoggerFactory.getLogger(TokenService.class);
	
	
	@Value("u7x!A%C*F-JaNdRg")
	private String secret;
	@Value("Authorization")
	private String AUTHORIZATION;
	@Value("Bearer ")
	public String TOKENTYPE;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAUTHORIZATION() {
		return AUTHORIZATION;
	}

	public void setAUTHORIZATION(String aUTHORIZATION) {
		AUTHORIZATION = aUTHORIZATION;
	}

	public String getTOKENTYPE() {
		return TOKENTYPE;
	}

	public void setTOKENTYPE(String tOKENTYPE) {
		TOKENTYPE = tOKENTYPE;
	}

	public TokenDto createToken(User user) throws Exception {
		UserTokenInfo userTokenInfo = new UserTokenInfo(user.getId(), user.getFullName());
		HashMap<String, Object> addedValues = new HashMap<String, Object>();
		addedValues.put("data", encryptValue(userTokenInfo));
		String token = buildToken(addedValues, secret);
		TokenDto tokenDto = new TokenDto(token, null);
		return tokenDto;
	}

	public String encryptValue(Object tokenJPRequestDto) throws Exception {
		try {
			final JsonWebEncryption jwe = new JsonWebEncryption();
			ObjectMapper mapper = new ObjectMapper();
			Key key = new AesKey(secret.getBytes());
			jwe.setKey(key);
			jwe.setPayload(mapper.writeValueAsString(tokenJPRequestDto));
			jwe.enableDefaultCompression();
			jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
			jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_GCM);
			return jwe.getCompactSerialization();
		} catch (final Exception e) {
			log.error("Exception in encryptValue: " + e.getMessage());
		}
		return null;
	}

	public String buildToken(HashMap<String, Object> addedValues, String secret) {
		String token = Jwts.builder().setClaims(addedValues).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
		return token;
	}

	public  UserHelper decodeInternalToken(String theToken, String prefix, String secret) {
		UserHelper userHelper = new UserHelper();
		try {
			String token = theToken.replace(prefix, "");
			String parsedToken = (String) Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody()
					.get("data");
			String decryptedValue = decryptJWT(parsedToken, secret);
			ObjectMapper mapper = new ObjectMapper();
			userHelper = mapper.readValue(decryptedValue, UserHelper.class);
		} catch (ExpiredJwtException expiredExc) {
			log.error("TOKEN_EXP - Token Expired");
			throw new MyPlatformException("TOKEN_EXP - Token Expired", HttpStatus.UNAUTHORIZED.value());
		} catch (Exception e) {
			log.error("Errore nella decodifica del token: " + e.getMessage());
		}
		return userHelper;
	}
	
	private static String decryptJWT(String token, String secret) {
		JsonWebEncryption jwe = new JsonWebEncryption();
		Key key = new AesKey(secret.getBytes());
		jwe.setKey(key);
		jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);
		jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_GCM);
		try {
			jwe.setCompactSerialization(token);
			return jwe.getPayload();
		} catch (JoseException e) {
			log.error("Exception in decryptJWT: " + e.getMessage());
		}
		return null;
	}

}
