package com.vanguard.weatherchallenge.weather.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.vanguard.weatherchallenge.weather.exception.ApiKeyException;
import com.vanguard.weatherchallenge.weather.exception.LocationNotFoundException;
import com.vanguard.weatherchallenge.weather.exception.QuotaExceededException;
import com.vanguard.weatherchallenge.weather.exception.WeatherNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ApiKeyException.class)
	public ResponseEntity<String> handleApiKeyException(ApiKeyException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(QuotaExceededException.class)
	public ResponseEntity<String> handleQuotaExceededException(QuotaExceededException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
	}

	@ExceptionHandler({ LocationNotFoundException.class, WeatherNotFoundException.class })
	public ResponseEntity<String> handleBusinessException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}