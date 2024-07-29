package com.vanguard.weatherchallenge.weather.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vanguard.weatherchallenge.weather.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	@GetMapping("/description")
	public String getWeatherDescription(@RequestParam String city, @RequestParam String country) throws IOException {
		return weatherService.getWeatherDescription(city, country);
	}
}