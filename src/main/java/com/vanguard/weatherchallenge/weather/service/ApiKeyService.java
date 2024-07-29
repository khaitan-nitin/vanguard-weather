package com.vanguard.weatherchallenge.weather.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vanguard.weatherchallenge.weather.constant.ApiKeyConstant;
import com.vanguard.weatherchallenge.weather.entity.ApiKey;
import com.vanguard.weatherchallenge.weather.entity.ApiKeyRequestHistory;
import com.vanguard.weatherchallenge.weather.exception.ApiKeyException;
import com.vanguard.weatherchallenge.weather.repository.ApiKeyRepository;
import com.vanguard.weatherchallenge.weather.repository.ApiKeyRequestHistoryRepository;

@Service
public class ApiKeyService {

	@Autowired
	private ApiKeyRepository apiKeyRepository;

	@Autowired
	private ApiKeyRequestHistoryRepository apiKeyRequestHistoryRepository;

	public boolean isRateLimitExceeded(String key) throws ApiKeyException {
		ApiKey apiKey = getApiKey(key);

		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
		List<ApiKeyRequestHistory> recentInvocations = apiKeyRequestHistoryRepository
				.getInvocationSince(apiKey.getApiKey(), oneHourAgo);

		return recentInvocations.size() >= ApiKeyConstant.RATE_LIMIT;
	}

	private ApiKey getApiKey(String key) throws ApiKeyException {
		Optional<ApiKey> apiKey = apiKeyRepository.getApiKey(key);
		if (!apiKey.isPresent()) {
			throw new ApiKeyException("Invalid API Key");
		}
		return apiKey.get();
	}

	public void save(String apiKey) {
		ApiKeyRequestHistory requestHistory = new ApiKeyRequestHistory();
		requestHistory.setApiKey(apiKey);

		apiKeyRequestHistoryRepository.save(requestHistory);
	}
}
