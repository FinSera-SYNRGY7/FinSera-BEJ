package com.finalproject.finsera.finsera.util;

import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
//@AllArgsConstructor
//public class AuthUtil {
//    private CustomerRepository userRepository;
//
//    public User getUserEntityByUsername(String username) {
//        Optional<Customers> userOptional = userRepository.findByUsername(username);
////        if (userOptional.isEmpty()) {
////            throw new ("user tidak ditemukan");
////        }
//        return userOptional.;
//    }
//}