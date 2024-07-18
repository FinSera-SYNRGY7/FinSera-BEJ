package com.finalproject.finsera.finsera.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.finsera.finsera.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws RuntimeException, ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(!isUserApiEndpoint(request) && StringUtils.isEmpty(authHeader) || !isUserApiEndpoint(request) && !StringUtils.startsWith(authHeader, "Bearer ")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorDetails.put("message", "Token invalid");

            ObjectMapper objectMapper = new ObjectMapper();
            OutputStream out = response.getOutputStream();
            objectMapper.writeValue(out, errorDetails);
            out.flush();
            return;

        } else if(!isUserApiEndpoint(request) && StringUtils.isNotEmpty(authHeader) || !isUserApiEndpoint(request) && StringUtils.startsWith(authHeader, "Bearer ")) {
            jwt = authHeader.substring(7);
            try{
                userEmail = jwtService.extractUserName(jwt);
                if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.userDetailsService()
                            .loadUserByUsername(userEmail);
                    if(jwtService.isTokenValid(jwt, userDetails)){
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        context.setAuthentication(authToken);
                        SecurityContextHolder.setContext(context);

                    }
                }
            }catch (Exception e){
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                errorDetails.put("message",e.getMessage());

                ObjectMapper objectMapper = new ObjectMapper();
                OutputStream out = response.getOutputStream();
                objectMapper.writeValue(out, errorDetails);
                out.flush();
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    private boolean isUserApiEndpoint(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI.startsWith("/api/v1/auth/");
    }
}