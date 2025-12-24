package com.authflow.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    
	@ExceptionHandler(EmailAlreadyExist.class)
	public ResponseEntity<Map<String, String>> handleAlreadyExistEmail(EmailAlreadyExist ex){
		Map<String,String> error = new HashMap<>();
		error.put("error", ex.getLocalizedMessage());
		return new ResponseEntity<Map<String,String>>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleNotValidMethodArgumentHandle(MethodArgumentNotValidException ex){
		 Map<String , String> errors = new HashMap<>();
		   ex.getBindingResult()
		     .getAllErrors()
		     .forEach(error -> errors.put( ((FieldError) error).getField(),error.getDefaultMessage()));
		  return new ResponseEntity<Map<String, String >>(errors, HttpStatus.BAD_REQUEST);
	}
}
