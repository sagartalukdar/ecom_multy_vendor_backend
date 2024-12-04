package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Deal;

@Repository
public interface DealRepository extends JpaRepository<Deal,Long>{

}
