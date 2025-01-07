package com.example.chat.repo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chat.entity.VisitEntity;

public interface VisitRepository extends JpaRepository<VisitEntity, Integer> {
    Optional<VisitEntity> findByAddressAndVisitDate(String address, LocalDate visitDate);

    @Query("SELECT COUNT(v) FROM VisitEntity v WHERE v.visitDate = :visitDate")
    int countVisitsByDate(@Param("visitDate") LocalDate visitDate);

    @Query("SELECT COUNT(v) FROM VisitEntity v")
    int countTotalVisits();
}

