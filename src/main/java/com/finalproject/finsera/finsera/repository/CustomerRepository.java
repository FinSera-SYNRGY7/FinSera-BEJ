package com.finalproject.finsera.finsera.repository;

import com.finalproject.finsera.finsera.model.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.Principal;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {
    Optional<Customers> findByUsername(String username);
//    Optional<Customers> updateMpin(Principal principal, String newMpin);
}
