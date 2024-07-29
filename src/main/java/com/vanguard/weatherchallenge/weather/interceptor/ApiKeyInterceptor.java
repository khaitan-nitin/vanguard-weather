package com.vanguard.weatherchallenge.weather.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.vanguard.weatherchallenge.weather.constant.ApiKeyConstant;
import com.vanguard.weatherchallenge.weather.exception.ApiKeyException;
import com.vanguard.weatherchallenge.weather.exception.QuotaExceededException;
import com.vanguard.weatherchallenge.weather.service.ApiKeyService;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

	@Autowired
	private ApiKeyService apiKeyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String apiKey = request.getHeader(ApiKeyConstant.X_API_KEY);

		if (apiKey == null) {
			throw new ApiKeyException("API key is missing");
		}

		if (apiKeyService.isRateLimitExceeded(apiKey)) {
			apiKeyService.save(apiKey);

			throw new QuotaExceededException("Rate limit exceeded");
		}

		apiKeyService.save(apiKey);

		return true;
	}
}
