package com.vanguard.weatherchallenge.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vanguard.weatherchallenge.weather.entity.ApiKey;
import com.vanguard.weatherchallenge.weather.repository.ApiKeyRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApiKeyRepositoryTest {

	@Autowired
	private ApiKeyRepository apiKeyRepository;

	@Test
	public void testGetApiKey_ValidKey_ReturnsApiKey() {
		String validApiKey = "askfhkalajslfla" + Timestamp.valueOf(LocalDateTime.now());

		ApiKey apiKey = new ApiKey();
		apiKey.setApiKey(validApiKey);
		apiKey.setIsActive(true);
		apiKey.setFlag(false);
		apiKeyRepository.save(apiKey);

		Optional<ApiKey> result = apiKeyRepository.getApiKey(validApiKey);

		assertTrue(result.isPresent());
		assertEquals(validApiKey, result.get().getApiKey());
		assertTrue(result.get().getCreatedBy() != null);
		assertTrue(result.get().getCreatedOn() != null);
		assertTrue(result.get().getId() != null);
	}
}
