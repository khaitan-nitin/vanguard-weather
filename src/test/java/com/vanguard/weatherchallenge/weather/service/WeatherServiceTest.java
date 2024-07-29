package com.vanguard.weatherchallenge.weather.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.weatherchallenge.weather.entity.Weather;
import com.vanguard.weatherchallenge.weather.provider.WeatherProvider;
import com.vanguard.weatherchallenge.weather.repository.WeatherRepository;
import com.vanguard.weatherchallenge.weather.service.WeatherService;
import com.vanguard.weatherchallenge.weather.util.DateUtil;

public class WeatherServiceTest {

	@Mock
	private WeatherRepository weatherRepository;

	@Mock
	private WeatherProvider weatherProvider;

	@InjectMocks
	private WeatherService weatherService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetWeatherDescription_FromDatabase() throws IOException {
		String city = "Sydney";
		String country = "AU";
		String description = "Sunny";
		LocalDateTime startOfDay = DateUtil.getStartOfDay();

		Weather weather = Weather.create(city, country, "{\"weather\":[{\"description\":\"Sunny\"}]}", description,
				startOfDay);
		weather.setCreatedOn(LocalDateTime.now());
		when(weatherRepository.getWeatherFromToday(city, country, startOfDay)).thenReturn(Optional.of(weather));

		String result = weatherService.getWeatherDescription(city, country);

		assertEquals(description, result);
		verify(weatherProvider, never()).getWeatherInfo(anyString(), anyString());
	}

	@Test
	public void testGetWeatherDescription_FromExternalSource() throws IOException {
		String city = "Sydney";
		String country = "AU";
		String description = "Sunny";
		LocalDateTime startOfDay = DateUtil.getStartOfDay();

		Weather weather = Weather.create(city, country, "{\"weather\":[{\"description\":\"Sunny\"}]}", description,
				startOfDay.minusDays(1));
		weather.setCreatedOn(LocalDateTime.now().minusDays(1));
		when(weatherRepository.getWeatherFromToday(city, country, startOfDay)).thenReturn(Optional.of(weather));

		JsonNode weatherInfo = prepareDummyWeatherInfo();
		when(weatherProvider.getWeatherInfo(city, country)).thenReturn(weatherInfo);

		String result = weatherService.getWeatherDescription(city, country);

		assertEquals(description, result);
		verify(weatherProvider, times(1)).getWeatherInfo(anyString(), anyString());
	}

	private JsonNode prepareDummyWeatherInfo() throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		String jsonString = "{ \"weather\": [{ \"description\": \"Sunny\" }] }";

		JsonNode rootNode = objectMapper.readTree(jsonString);
		return rootNode;

	}
}
