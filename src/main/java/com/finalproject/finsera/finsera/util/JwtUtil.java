package com.finalproject.finsera.finsera.util;

import com.finalproject.finsera.finsera.exception.GlobalExceptionHandlers;
import com.finalproject.finsera.finsera.model.entity.Customers;
import com.finalproject.finsera.finsera.repository.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
public class JwtUtil {

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String generateToken(Authentication authentication) {
        String username;
        Long userId;
        Customers customers = new Customers();

        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
            username = userPrincipal.getUsername();
            customers.setUsername(userPrincipal.getUsername());
            customers.setIdCustomers(userPrincipal.getIdCustomers());

            userId = userPrincipal.getIdCustomers();
            log.info("Generating token for user : {}", userId);
        } else {
            throw new IllegalArgumentException("Unsupported principal type");
        }

        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .claim("userId", customers.getIdCustomers())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenFromUsername(String username){
        Customers customers = customerRepository.findByUsername(username).get();

        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("userId", customers.getIdCustomers())
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String getUsername(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()).build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public Long getId(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()).build()
                .parseClaimsJws(jwt)
                .getBody()
                .get("userId", Long.class);
    }

    public String getMpinByToken(String token) {
        String jwt = token.replace("Bearer", "").trim();
        log.info("JWT : " + jwt);
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()).build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
