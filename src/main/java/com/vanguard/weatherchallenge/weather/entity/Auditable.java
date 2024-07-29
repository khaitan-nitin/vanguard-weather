package com.vanguard.weatherchallenge.weather.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import com.vanguard.weatherchallenge.weather.listener.AuditEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditEntityListener.class)
public abstract class Auditable {
	@Column(name = "created_by", updatable = false)
	private Long createdBy;

	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_by")
	private Long modifiedBy;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;
}
