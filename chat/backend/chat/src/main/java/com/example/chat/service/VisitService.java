package com.example.chat.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chat.entity.VisitEntity;
import com.example.chat.repo.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitService {

  private final VisitRepository repository;

  public void recordVisit(String address) {
    LocalDate today = LocalDate.now();
    Optional<VisitEntity> existingVisit = repository.findByAddressAndVisitDate(address, today);

    if (existingVisit.isEmpty()) {
      VisitEntity visit = VisitEntity.builder().address(address).visitDate(today).build();
      repository.save(visit);
    }
  }

  public int getTodayVisits() {
    LocalDate today = LocalDate.now();
    return repository.countVisitsByDate(today);
  }

  public int getTotalVisits() {
    return repository.countTotalVisits();
  }
}

