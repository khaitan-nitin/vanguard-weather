package com.vanguard.weatherchallenge.weather.util;

import java.time.LocalDateTime;

public final class DateUtil {
	public static boolean isDateAfterAnHour(LocalDateTime fromDateTime) {
		return fromDateTime.isAfter(LocalDateTime.now().minusHours(1));
	}

	public static LocalDateTime getStartOfDay() {
		LocalDateTime now = LocalDateTime.now();
		return now.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
}
