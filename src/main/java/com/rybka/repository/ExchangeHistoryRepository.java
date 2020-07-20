package com.rybka.repository;

import com.rybka.model.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Integer> {
    List<ExchangeHistory> findTop5ByOrderByDateDesc();
}
