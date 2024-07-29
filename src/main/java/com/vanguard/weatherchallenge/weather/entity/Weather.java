package com.vanguard.weatherchallenge.weather.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "weather")
public class Weather extends Auditable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

	@Column(name = "raw_data")
	private String rawData;

	@Column(name = "description")
	private String description;

	@Column(name = "requested_on")
	private LocalDateTime requestedOn;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "flag")
	private Boolean flag;

	public static Weather create(String city, String country, String rawData, String description,
			LocalDateTime startOfDay) {
		Weather weather = new Weather();
		weather.setCity(city);
		weather.setCountry(country);
		weather.setDescription(description);
		weather.setRawData(rawData.toString());
		weather.setRequestedOn(startOfDay);
		weather.setIsActive(true);
		weather.setFlag(false);

		return weather;
	}
}
