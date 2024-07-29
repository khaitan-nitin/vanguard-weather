package com.vanguard.weatherchallenge.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vanguard.weatherchallenge.weather.entity.Weather;
import com.vanguard.weatherchallenge.weather.repository.WeatherRepository;
import com.vanguard.weatherchallenge.weather.util.DateUtil;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WeatherRepositoryTest {
	@Autowired
	private WeatherRepository weatherRepository;

	@Test
	public void testGetInvocationSince_NoInput() {
		String city = "InvalidCity";
		String country = "InvalidCountry";

		Optional<Weather> result = weatherRepository.getWeatherFromToday(city, country, DateUtil.getStartOfDay());

		assertTrue(!result.isPresent());
	}

	@Test
	public void testGetInvocationSince_ValidInputs() {
		String city = "Melbourne";
		String country = "Australi";
		String rawData = "Test Json";
		String description = "Cold";
		LocalDateTime requestOn = LocalDateTime.now();

		Weather weatherOne = Weather.create(city, country, rawData, description, requestOn);
		weatherRepository.save(weatherOne);

		Optional<Weather> result = weatherRepository.getWeatherFromToday(city, country, DateUtil.getStartOfDay());

		assertTrue(result.isPresent());
		assertEquals(result.get().getCity(), city);
		assertEquals(result.get().getCountry(), country);
		assertEquals(result.get().getFlag(), false);
		assertEquals(result.get().getIsActive(), true);
		assertEquals(result.get().getRawData(), rawData);
		assertEquals(result.get().getDescription(), description);
		assertNotNull(result.get().getCreatedBy());
		assertNotNull(result.get().getCreatedOn());
	}

	@Test
	public void testGetInvocationSince_MultipleInputs() {
		String city = "Melbourne";
		String country = "Australi";
		String rawData = "Test Json 1";
		String description = "Cold 1";
		LocalDateTime requestOn = LocalDateTime.now();

		Weather weatherOne = Weather.create(city, country, rawData, description, requestOn);
		weatherRepository.save(weatherOne);

		rawData = "Test Json 2";
		description = "Cold 2";
		requestOn = LocalDateTime.now().minusDays(1);

		Weather weatherTwo = Weather.create(city, country, rawData, description, requestOn);
		weatherRepository.save(weatherTwo);

		rawData = "Test Json 3";
		description = "Cold 3";
		requestOn = LocalDateTime.now().minusDays(2);

		Weather weatherThree = Weather.create(city, country, rawData, description, requestOn);
		weatherRepository.save(weatherThree);

		Optional<Weather> result = weatherRepository.getWeatherFromToday(city, country, DateUtil.getStartOfDay());

		assertTrue(result.isPresent());
		assertEquals(result.get().getCity(), city);
		assertEquals(result.get().getCountry(), country);
		assertEquals(result.get().getFlag(), false);
		assertEquals(result.get().getIsActive(), true);
		assertEquals(result.get().getRawData(), "Test Json 1");
		assertEquals(result.get().getDescription(), "Cold 1");
		assertNotNull(result.get().getCreatedBy());
		assertNotNull(result.get().getCreatedOn());
	}

}
