package com.vanguard.weatherchallenge.weather.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.weatherchallenge.weather.exception.LocationNotFoundException;
import com.vanguard.weatherchallenge.weather.exception.WeatherNotFoundException;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Import(WeatherProvider.class)
@TestPropertySource(locations = "classpath:application.properties")
public class WeatherProviderTest {

	@MockBean
	private WebClient.Builder webClientBuilder;

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	@Autowired
	private WeatherProvider weatherProvider;

	@Value("${openweathermap.api.location-url}")
	private String locationUrl;

	@Value("${openweathermap.api.weather-url}")
	private String weatherUrl;

	@Value("${openweathermap.api.key}")
	private String weatherApiKey;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setup() {
		when(webClientBuilder.build()).thenReturn(webClient);
		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
	}

	@Test
	public void testGetWeatherInfo_Success() throws IOException {
		String city = "Sydney";
		String country = "AU";
		String locationJson = "[{\"lat\": -33.8675, \"lon\": 151.207} ]";
		String weatherJson = "{\"weather\": [{\"description\": \"Sunny\"}]}";

		JsonNode locationNode = objectMapper.readTree(locationJson);
		JsonNode weatherNode = objectMapper.readTree(weatherJson);

		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(locationNode), Mono.just(weatherNode));

		JsonNode result = weatherProvider.getWeatherInfo(city, country);

		assertEquals("Sunny", result.path("weather").get(0).path("description").asText());
	}

	@Test
	public void testGetWeatherInfo_LocationNotFound() throws IOException {
		String city = "InvalidCity";
		String country = "AU";
		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(
				Mono.error(new LocationNotFoundException("No record found for provided city and country.")));

		assertThrows(LocationNotFoundException.class, () -> {
			weatherProvider.getWeatherInfo(city, country);
		});
	}

	@Test
	public void testGetWeatherInfo_WeatherNotFound() throws IOException {
		String city = "Sydney";
		String country = "AU";
		String locationJson = "[{\"lat\": -33.8675, \"lon\": 151.207}]";
		JsonNode locationNode = objectMapper.readTree(locationJson);

		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(locationNode), Mono.empty());

		assertThrows(WeatherNotFoundException.class, () -> {
			weatherProvider.getWeatherInfo(city, country);
		});
	}

	@Test
	public void testGetLocation_Success() throws IOException {
		String city = "Sydney";
		String country = "AU";
		String locationJson = "[{\"lat\": -33.8675, \"lon\": 151.207}]";
		JsonNode locationNode = objectMapper.readTree(locationJson);

		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(Mono.just(locationNode));

		JsonNode result = weatherProvider.getLocation(city, country);

		assertFalse(result.isEmpty());
	}

	@Test
	public void testGetLocation_NotFound() throws IOException {
		String city = "InvalidCity";
		String country = "AU";
		when(responseSpec.bodyToMono(JsonNode.class)).thenReturn(
				Mono.error(new LocationNotFoundException("No record found for provided city and country.")));

		assertThrows(LocationNotFoundException.class, () -> {
			weatherProvider.getLocation(city, country);
		});
	}
}
