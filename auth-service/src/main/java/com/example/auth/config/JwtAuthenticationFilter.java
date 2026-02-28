package com.example.auth.config;

import com.example.auth.security.CustomUserDetailsService;
import com.example.auth.security.JwtUtils;
import com.example.auth.utils.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            final String header = request.getHeader(AppConstants.AUTH_HEADER);
            if(header == null || !header.startsWith(AppConstants.TOKEN_PREFIX)){
                filterChain.doFilter(request, response);
                return;
            }
            String token = header.substring(AppConstants.TOKEN_PREFIX.length());

            if(jwtUtils.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null){
                String username = jwtUtils.getUsernameFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        } catch (Exception ex){
            log.error("JWT Authentication failed: {}", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}