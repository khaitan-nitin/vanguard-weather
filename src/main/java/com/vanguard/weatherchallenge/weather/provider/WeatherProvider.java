package com.vanguard.weatherchallenge.weather.provider;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;
import com.vanguard.weatherchallenge.weather.exception.LocationNotFoundException;
import com.vanguard.weatherchallenge.weather.exception.WeatherNotFoundException;

import reactor.core.publisher.Mono;

@Service
public class WeatherProvider {
	@Autowired
	private WebClient.Builder webClientBuilder;

	@Value("${openweathermap.api.location-url}")
	private String locationUrl;

	@Value("${openweathermap.api.weather-url}")
	private String weatherUrl;

	@Value("${openweathermap.api.key}")
	private String weatherApiKey;

	public JsonNode getWeatherInfo(String city, String country) throws IOException {
		JsonNode locationData = this.getLocation(city, country);
		if (locationData.isEmpty()) {
			throw new LocationNotFoundException("No record found for provided city and country.");
		}
		Double lat = locationData.get(0).path("lat").asDouble();
		Double lon = locationData.get(0).path("lon").asDouble();

		String url = String.format(weatherUrl, lat, lon, weatherApiKey);
		WebClient webClient = webClientBuilder.build();
		Mono<JsonNode> weatherDataMono = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class).onErrorMap(
				WebClientResponseException.class,
				ex -> new WeatherNotFoundException("Weather not found for the provided location."));

		JsonNode weatherData = weatherDataMono.block();
		if (weatherData == null || weatherData.isEmpty()) {
			throw new WeatherNotFoundException("Weather not found for the provided location.");
		}

		return weatherData;
	}

	public JsonNode getLocation(String city, String country) throws IOException {
		String url = String.format(locationUrl, city, country, weatherApiKey);
		WebClient webClient = webClientBuilder.build();
		Mono<JsonNode> locationDataMono = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class).onErrorMap(
				WebClientResponseException.class,
				ex -> new LocationNotFoundException("No record found for provided city and country."));
		;

		JsonNode locationData = locationDataMono.block();
		if (locationData == null || locationData.isEmpty()) {
			throw new LocationNotFoundException("No record found for provided city and country.");
		}

		return locationData;
	}
}
