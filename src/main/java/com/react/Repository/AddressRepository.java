package com.react.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.react.Model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
