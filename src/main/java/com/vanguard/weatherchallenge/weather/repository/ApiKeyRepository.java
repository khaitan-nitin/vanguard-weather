package com.vanguard.weatherchallenge.weather.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vanguard.weatherchallenge.weather.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
	@Query("SELECT ak FROM ApiKey ak WHERE ak.apiKey = :apiKey AND ak.isActive = TRUE AND ak.flag = FALSE")
	Optional<ApiKey> getApiKey(String apiKey);
}
