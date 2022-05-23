package com.work.calendar.aspect;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.work.calendar.exceptions.MyPlatformException;
import com.work.calendar.service.TokenService;

@Aspect
@Component
public class WorkCalendarAspect {
	protected static final Logger log = LoggerFactory.getLogger(WorkCalendarAspect.class);

	@Autowired
	private TokenService tokenService;

	@Around("@annotation(com.work.calendar.aspect.WorkCalendarAPI)")
	public Object trace(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			UserHelper userHelper = decodeToken(joinPoint);
			List<Object> params = new ArrayList<Object>();
			for (Object obj : joinPoint.getArgs()) {
				if (obj instanceof UserHelper) {
					params.add(userHelper);
				} else {
					params.add(obj);
				}

			}
			Object proceed = joinPoint.proceed(params.toArray());

			return proceed;
		} catch (Throwable t) {
			if (!(t instanceof MyPlatformException)) {
				log.error("Error:", t);
			}
			throw t;
		}

	}

	private UserHelper decodeToken(ProceedingJoinPoint joinPoint) {
		UserHelper userHelper = new UserHelper();
		for (Object obj : joinPoint.getArgs()) {
			if (obj instanceof HttpServletRequest) {
				userHelper = parseHttpServletRequest((HttpServletRequest) obj);
			}
		}
		return userHelper;
	}

	private UserHelper parseHttpServletRequest(HttpServletRequest request) {
		log.info("auhto " + tokenService.getAUTHORIZATION());
		String token = request.getHeader(tokenService.getAUTHORIZATION());
		if (token == null)
			throw new MyPlatformException("Missing token", HttpStatus.UNAUTHORIZED.value());
		UserHelper userHelperFromToken;
		try {
			userHelperFromToken = tokenService.decodeInternalToken(token, tokenService.getTOKENTYPE(),
					tokenService.getSecret());
		} catch (MyPlatformException e) {
			log.error(e.toString());
			throw e;
		} catch (Exception e) {
			log.error(e.toString());
			throw new MyPlatformException("Invalid token", HttpStatus.UNAUTHORIZED.value());
		}
		if (userHelperFromToken == null || userHelperFromToken.getId() == null)
			throw new MyPlatformException("Missing token", HttpStatus.UNAUTHORIZED.value());
		UserHelper userHelper = new UserHelper();
		userHelper.setId(userHelperFromToken.getId());
		userHelper.setFullName(userHelperFromToken.getFullName());
		return userHelper;
	}

}