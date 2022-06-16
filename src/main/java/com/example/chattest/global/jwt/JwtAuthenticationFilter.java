package com.example.chattest.global.jwt;

import com.example.chattest.global.jwt.member.MemberDetailsService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;

    String HEADER_STRING = "X-AUTH-TOKEN";
    String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING); // header에서 X-AUTH-TOKEN 가져옴
        log.info(header);
        String accessToken = (header != null && header.startsWith(TOKEN_PREFIX)) ? header.replace(TOKEN_PREFIX,"") : null;
        log.info(accessToken);

        // Request Header 에 Access Token (Authorization) 이 담긴 경우
        if (!ObjectUtils.isEmpty(accessToken)) {
            // Access Token 이 만료된 경우
            if(jwtTokenProvider.isTokenExpired(accessToken)) {
                throw new JwtException("access token is expired");
            }

            // Access Token 이 유효한 경우
            if(jwtTokenProvider.validateToken(accessToken) && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtTokenProvider.getUsernameFromToken(accessToken);
                UserDetails userDetails = memberDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }
        filterChain.doFilter(request, response);
    }
}
