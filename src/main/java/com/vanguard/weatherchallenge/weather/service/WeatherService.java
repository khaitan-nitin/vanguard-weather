package com.vanguard.weatherchallenge.weather.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vanguard.weatherchallenge.weather.entity.Weather;
import com.vanguard.weatherchallenge.weather.provider.WeatherProvider;
import com.vanguard.weatherchallenge.weather.repository.WeatherRepository;
import com.vanguard.weatherchallenge.weather.util.DateUtil;

@Service
public class WeatherService {
	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private WeatherProvider weatherProvider;

	public String getWeatherDescription(String city, String country) throws IOException {
		LocalDateTime startOfDay = DateUtil.getStartOfDay();

		Optional<Weather> optionalWeather = weatherRepository.getWeatherFromToday(city, country, startOfDay);

		if (optionalWeather.isPresent() && DateUtil.isDateAfterAnHour(optionalWeather.get().getCreatedOn())) {
			return optionalWeather.get().getDescription();
		}

		JsonNode weatherData = weatherProvider.getWeatherInfo(city, country);
		String description = weatherData.path("weather").get(0).path("description").asText();

		Weather weather = Weather.create(city, country, weatherData.toPrettyString(), description, startOfDay);
		weatherRepository.save(weather);

		return description;
	}
}
