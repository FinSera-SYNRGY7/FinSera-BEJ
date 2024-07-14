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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Entering in loadUserByUsername Method...");
        Customers customers = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Username not found " + username
                ));
        return UserDetailsImpl.build(customers);
    }
}
