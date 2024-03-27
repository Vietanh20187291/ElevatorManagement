package com.project.service;

import com.project.entity.User;
import com.project.entity.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;
    @Autowired
    private UserService userService;

    public String generateToken(int userId, UserRole role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User getUserFromToken(String token) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            int id = Integer.parseInt(claims.getSubject());
            System.out.println("id "+id);
            return userService.getUserById(id);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
    public String getTokenFromRequest(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    String jwtToken = cookie.getValue();
                    return jwtToken;
                }
            }
        }
        return null;

    }

    public UserRole getRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return UserRole.valueOf(claims.get("role").toString());
    }
}
