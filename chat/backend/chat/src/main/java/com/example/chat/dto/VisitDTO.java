package com.example.chat.dto;

import java.time.LocalDate;

import com.example.chat.entity.VisitEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitDTO {
	private String address;
	private LocalDate visitDate;
	
	public VisitDTO(VisitEntity entity) {
		this.address = entity.getAddress();
		this.visitDate = entity.getVisitDate();
	}
}
