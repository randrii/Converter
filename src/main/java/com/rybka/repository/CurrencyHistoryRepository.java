package com.rybka.repository;

import com.rybka.model.CurrencyHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyHistoryRepository extends JpaRepository<CurrencyHistory, Integer> {
    List<CurrencyHistory> findTop5ByOrderByIdDesc();
}