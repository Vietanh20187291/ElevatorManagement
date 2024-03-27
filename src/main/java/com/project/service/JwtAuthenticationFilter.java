package com.project.service;

import com.project.entity.User;
import com.project.entity.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("===============================");
        System.out.println("doFilterInternal");
        System.out.println("path: " + request.getServletPath());
        if ("/login".equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String jwtToken = cookie.getValue();

                    //   String jwtToken = jwtTokenService.getTokenFromRequest(request);
                    if (jwtTokenService.validateToken(jwtToken)) {
                        if (jwtTokenService.validateToken(jwtToken)) {
                            User user = null;
                            try {
                                user = jwtTokenService.getUserFromToken(jwtToken);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            UserRole userRole = jwtTokenService.getRoleFromToken(jwtToken);
                            List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + userRole.name());
                            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }

                    }

                    filterChain.doFilter(request, response);
                }

            }

        }
    }
}

