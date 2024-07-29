package com.vanguard.weatherchallenge.weather.exception;

public class QuotaExceededException extends Exception {
	private static final long serialVersionUID = -2230727729560168720L;

	public QuotaExceededException(String message) {
		super(message);
	}
}