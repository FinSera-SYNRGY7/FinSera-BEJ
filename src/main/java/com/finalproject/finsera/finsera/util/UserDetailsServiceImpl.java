package com.finalproject.finsera.finsera.util;

import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//        logger.debug("Entering in loadUserByUsername Method...");
//        Customers customers = customerRepository.findById(Long.valueOf(id))
//                .orElseThrow(() -> new UsernameNotFoundException(
//                    "Username not found "
//                ));
        Customers getId = customerRepository.findByUsername(id).get();
        Customers customers = customerRepository.findById(getId.getIdCustomers())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Username not found " + id
                ));
        return UserDetailsImpl.build(customers);
    }

}
