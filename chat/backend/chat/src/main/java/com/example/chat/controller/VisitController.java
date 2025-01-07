package com.example.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.chat.dto.VisitCountDTO;
import com.example.chat.service.VisitService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visit")
public class VisitController {

	private final VisitService service;
	
	@PostMapping("/record")
	public ResponseEntity<?> recordVisit(HttpServletRequest request){
		try {
			String address = request.getHeader("X-Forwarded-For");
			if (address == null || address.isEmpty()) {
			    address = request.getRemoteAddr();
			}
			service.recordVisit(address);
			return ResponseEntity.ok().body("방문이 정상적으로 기록되었습니다");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Visit Recording Error: " + e.getMessage());
		}
		
	}
	
	@GetMapping("/count")
	public ResponseEntity<?> getVisitCounts(){
		try {
			VisitCountDTO dto = VisitCountDTO.builder().todayVisits(service.getTodayVisits()).totalVisits(service.getTotalVisits()).build();
			return ResponseEntity.ok().body(dto);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Visit Counting Error: " + e.getMessage());
		}
		
	}
	
}
