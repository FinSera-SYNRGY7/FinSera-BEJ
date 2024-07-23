package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customers, Long> {
    Optional<Customers> findByUsername(String username);

}
