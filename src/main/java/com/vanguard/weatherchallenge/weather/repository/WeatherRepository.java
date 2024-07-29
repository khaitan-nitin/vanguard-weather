package com.vanguard.weatherchallenge.weather.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vanguard.weatherchallenge.weather.entity.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
	@Query("SELECT w FROM Weather w WHERE w.city = :city AND w.country = :country AND w.isActive = TRUE AND w.flag = FALSE AND w.requestedOn >= :requestedOn")
	Optional<Weather> getWeatherFromToday(String city, String country, LocalDateTime requestedOn);
}
