package com.vanguard.weatherchallenge.weather.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vanguard.weatherchallenge.weather.constant.ApiKeyConstant;
import com.vanguard.weatherchallenge.weather.entity.ApiKey;
import com.vanguard.weatherchallenge.weather.entity.ApiKeyRequestHistory;
import com.vanguard.weatherchallenge.weather.exception.ApiKeyException;
import com.vanguard.weatherchallenge.weather.repository.ApiKeyRepository;
import com.vanguard.weatherchallenge.weather.repository.ApiKeyRequestHistoryRepository;
import com.vanguard.weatherchallenge.weather.service.ApiKeyService;

public class ApiKeyServiceTest {

	@Mock
	private ApiKeyRepository apiKeyRepository;

	@Mock
	private ApiKeyRequestHistoryRepository apiKeyRequestHistoryRepository;

	@InjectMocks
	private ApiKeyService apiKeyService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testIsRateLimitNotExceeded() throws ApiKeyException {
		String validApiKey = "askfhkalajslfla";

		ApiKey apiKey = new ApiKey();
		apiKey.setApiKey(validApiKey);
		when(apiKeyRepository.getApiKey(validApiKey)).thenReturn(Optional.of(apiKey));

		List<ApiKeyRequestHistory> recentRequests = Collections.emptyList();
		when(apiKeyRequestHistoryRepository.getInvocationSince(validApiKey, LocalDateTime.now().minusHours(1)))
				.thenReturn(recentRequests);

		boolean result = apiKeyService.isRateLimitExceeded(validApiKey);
		assertFalse(result);
	}

	@Test
	public void testIsRateLimitExceeded() throws ApiKeyException {
		String validApiKey = "askfhkalajslfla";

		ApiKey apiKey = new ApiKey();
		apiKey.setApiKey(validApiKey);
		when(apiKeyRepository.getApiKey(validApiKey)).thenReturn(Optional.of(apiKey));

		List<ApiKeyRequestHistory> recentRequests = Collections.nCopies(ApiKeyConstant.RATE_LIMIT,
				new ApiKeyRequestHistory());
		when(apiKeyRequestHistoryRepository.getInvocationSince(eq(validApiKey), any(LocalDateTime.class)))
				.thenReturn(recentRequests);

		boolean result = apiKeyService.isRateLimitExceeded(validApiKey);
		assertTrue(result);
	}

	@Test
	public void testSave() {
		String apiKey = "askfhkalajslfla";

		apiKeyService.save(apiKey);

		verify(apiKeyRequestHistoryRepository, times(1)).save(argThat(history -> history.getApiKey().equals(apiKey)));
	}

	@Test
	public void testIsRateLimitExceededWithInvalidApiKey() {
		String invalidApiKey = "invalidApiKey";

		when(apiKeyRepository.getApiKey(invalidApiKey)).thenReturn(Optional.empty());

		assertThrows(ApiKeyException.class, () -> apiKeyService.isRateLimitExceeded(invalidApiKey));
	}

	@Test
	public void testSaveWithInvalidApiKey() {
		String invalidApiKey = "invalidApiKey";

		apiKeyService.save(invalidApiKey);

		verify(apiKeyRequestHistoryRepository, times(1))
				.save(argThat(history -> history.getApiKey().equals(invalidApiKey)));
	}
}