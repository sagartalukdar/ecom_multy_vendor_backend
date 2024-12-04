package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long>{

	public WishList findByUserId(Long userId);
}
