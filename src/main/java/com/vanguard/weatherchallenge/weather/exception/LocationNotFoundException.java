package com.vanguard.weatherchallenge.weather.exception;

public class LocationNotFoundException extends RuntimeException {
	public LocationNotFoundException(String message) {
		super(message);
	}
}
