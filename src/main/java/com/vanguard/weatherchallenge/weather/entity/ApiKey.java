package com.vanguard.weatherchallenge.weather.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vanguard.weatherchallenge.weather.constant.ApiKeyConstant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "api_key")
public class ApiKey extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "apiKey", unique = true, nullable = false)
	private String apiKey;

	@Column(name = "rate_limit")
	private int rateLimit = ApiKeyConstant.RATE_LIMIT;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "flag")
	private Boolean flag;
}