package com.vanguard.weatherchallenge.weather.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.vanguard.weatherchallenge.weather.controller.WeatherController;
import com.vanguard.weatherchallenge.weather.exception.ApiKeyException;
import com.vanguard.weatherchallenge.weather.exception.LocationNotFoundException;
import com.vanguard.weatherchallenge.weather.service.ApiKeyService;
import com.vanguard.weatherchallenge.weather.service.WeatherService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherService weatherService;

	@MockBean
	private ApiKeyService apiKeyService;

	private final String VALID_API_KEY = "askfhkalajslfla";
	private final String INVALID_API_KEY = "invalidApiKey";

	@Test
	public void testGetWeatherDescriptionWithValidApiKey() throws Exception {
		String city = "Sydney";
		String country = "AU";
		String description = "Sunny";

		when(weatherService.getWeatherDescription(city, country)).thenReturn(description);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/description").param("city", city)
				.param("country", country).header("x-api-key", VALID_API_KEY).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(description));

		verify(apiKeyService, times(1)).save(anyString());
	}

	@Test
	public void testGetWeatherDescriptionWithNullApiKey() throws Exception {
		String city = "Sydney";
		String country = "AU";

		mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/description").param("city", city)
				.param("country", country).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.content().string("API key is missing"));

		verify(apiKeyService, times(0)).save(anyString());
	}

	@Test
	public void testGetWeatherDescriptionWithInvalidApiKey() throws Exception {
		String city = "Sydney";
		String country = "AU";

		doThrow(new ApiKeyException("Invalid API key")).when(apiKeyService).isRateLimitExceeded(INVALID_API_KEY);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/description").param("city", city)
				.param("country", country).header("x-api-key", INVALID_API_KEY).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andExpect(MockMvcResultMatchers.content().string("Invalid API key"));

		verify(apiKeyService, times(0)).save(anyString());
	}

	@Test
	public void testGetWeatherDescriptionWithQuotaExceeded() throws Exception {
		String city = "Sydney";
		String country = "AU";

		when(apiKeyService.isRateLimitExceeded(VALID_API_KEY)).thenReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/description").param("city", city)
				.param("country", country).header("x-api-key", VALID_API_KEY).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.content().string("Rate limit exceeded"));

		verify(apiKeyService, times(1)).save(anyString());
	}

	@Test
	public void testGetWeatherDescriptionWithInvalidLocation() throws Exception {
		String city = "Sydney1";
		String country = "AU1";

		when(apiKeyService.isRateLimitExceeded(VALID_API_KEY)).thenReturn(false);
		doThrow(new LocationNotFoundException("No record found for provided city and country.")).when(weatherService)
				.getWeatherDescription(city, country);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/description").param("city", city)
				.param("country", country).header("x-api-key", VALID_API_KEY).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andExpect(MockMvcResultMatchers.content().string("No record found for provided city and country."));

		verify(apiKeyService, times(1)).save(anyString());
	}
}