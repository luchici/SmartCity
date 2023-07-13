package com.github.luchici.authservice.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHedeare = request.getHeader("Authorization");
        final String jwt;

        if(authHedeare==null ||!authHedeare.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHedeare.substring(7);

        final String username = request.getParameter("username");
        System.out.println("======================================================");
        System.out.println(this.getClass().getSimpleName());
        System.out.println(username);
        System.out.println("======================================================");

    }
}
