package com.vanguard.weatherchallenge.weather.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vanguard.weatherchallenge.weather.entity.ApiKeyRequestHistory;
import com.vanguard.weatherchallenge.weather.repository.ApiKeyRequestHistoryRepository;
import com.vanguard.weatherchallenge.weather.util.DateUtil;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApiKeyRequestHistoryRepositoryTest {
	@Autowired
	private ApiKeyRequestHistoryRepository apiKeyRequestHistoryRepository;

	@Test
	public void testGetInvocationSince_ValidInputs() {
		String apiKey = "askfhkalajslfla";

		ApiKeyRequestHistory apiKeyRequestHistoryOne = new ApiKeyRequestHistory();
		apiKeyRequestHistoryOne.setApiKey(apiKey);
		apiKeyRequestHistoryRepository.save(apiKeyRequestHistoryOne);

		ApiKeyRequestHistory apiKeyRequestHistoryTwo = new ApiKeyRequestHistory();
		apiKeyRequestHistoryTwo.setApiKey(apiKey);
		apiKeyRequestHistoryRepository.save(apiKeyRequestHistoryTwo);

		List<ApiKeyRequestHistory> result = apiKeyRequestHistoryRepository.getInvocationSince(apiKey,
				DateUtil.getStartOfDay());

		assertEquals(result.size(), 2);
	}

	@Test
	public void testGetInvocationSince_InvalidApiKey() {
		String apiKey = "InvalidApiKey";
		LocalDateTime since = LocalDateTime.now().minusDays(1);

		List<ApiKeyRequestHistory> result = apiKeyRequestHistoryRepository.getInvocationSince(apiKey, since);

		assertEquals(result.size(), 0);
	}

	@Test
	public void testGetInvocationSince_NullApiKey() {
		LocalDateTime since = LocalDateTime.now().minusDays(1);

		List<ApiKeyRequestHistory> result = apiKeyRequestHistoryRepository.getInvocationSince(null, since);

		assertEquals(result.size(), 0);
	}

	@Test
	public void testGetInvocationSince_NullDate() {
		String apiKey = "askfhkalajslfla";

		ApiKeyRequestHistory apiKeyRequestHistoryOne = new ApiKeyRequestHistory();
		apiKeyRequestHistoryOne.setApiKey(apiKey);
		apiKeyRequestHistoryRepository.save(apiKeyRequestHistoryOne);

		List<ApiKeyRequestHistory> result = apiKeyRequestHistoryRepository.getInvocationSince(apiKey, null);

		assertEquals(result.size(), 0);
	}

}
