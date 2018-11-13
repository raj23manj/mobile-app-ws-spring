package com.appsdeveloperblog.app.ws.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionsHandler {
	// this annotation say what type of exception this method needs to handle 
	@ExceptionHandler(value = {UserServiceException.class})
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {
//		return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
