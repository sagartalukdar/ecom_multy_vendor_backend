package com.react.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Transaction;

@Repository
public interface TransectionRepository extends JpaRepository<Transaction,Long>{

	public List<Transaction> findBySellerId(Long id);
}
