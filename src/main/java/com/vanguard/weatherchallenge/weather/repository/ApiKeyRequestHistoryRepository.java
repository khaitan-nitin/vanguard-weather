package com.vanguard.weatherchallenge.weather.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vanguard.weatherchallenge.weather.entity.ApiKeyRequestHistory;

public interface ApiKeyRequestHistoryRepository extends JpaRepository<ApiKeyRequestHistory, Long> {
	@Query("SELECT akrh FROM ApiKeyRequestHistory akrh WHERE akrh.apiKey = :apiKey AND akrh.createdOn > :since")
	List<ApiKeyRequestHistory> getInvocationSince(String apiKey, LocalDateTime since);
}
