package com.vanguard.weatherchallenge.weather.listener;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.vanguard.weatherchallenge.weather.entity.Auditable;

public class AuditEntityListener {
	@PrePersist
	public void setCreatedOn(Auditable auditable) {
		auditable.setCreatedOn(LocalDateTime.now());
		auditable.setCreatedBy(getCurrentUser());
	}

	@PreUpdate
	public void setModifiedOn(Auditable auditable) {
		auditable.setModifiedOn(LocalDateTime.now());
		auditable.setModifiedBy(getCurrentUser());
	}

	private Long getCurrentUser() {
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if (principal instanceof UserDetails) {
//			return ((UserDetails) principal).getUsername();
//		} else {
//			return principal.toString();
//		}

		return 1l;
	}
}
