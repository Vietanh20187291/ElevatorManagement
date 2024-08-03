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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;
    @Autowired
    private UserService userService;

    private Map<String, Long> blacklist = new ConcurrentHashMap<>();
    private Map<Integer, Set<String>> userTokensMap = new ConcurrentHashMap<>();

    public String generateToken(int userId, UserRole role, int buildingId) {

        // Lấy ra HashSet tương ứng với userId
        Set<String> tokens = userTokensMap.get(userId);

        // Kiểm tra nếu HashSet không null và không rỗng
        if (tokens != null && !tokens.isEmpty()) {
//            System.out.println("Tokens for user " + userId + ":");
            for (String t : tokens) {
//                System.out.println(t);
                addToBlacklist(t);
            }
        } else {
            System.out.println("No tokens found for user " + userId);
        }

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", role)
                .claim("buildingId", buildingId)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        userTokensMap.computeIfAbsent(userId, k -> new HashSet<>()).add(token);


//        System.out.println("token 1: "+token);
//        System.out.println("user id "+userId);
        return token;
    }



    public boolean validateToken(String token) {
        try {
//            System.out.println("validate"+token);
//            System.out.println("result"+isBlacklisted(token));
            if (isBlacklisted(token)) {
//                System.out.println("deny");
                return false;
            }
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
//            System.out.println("id "+id);
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
    public int getBuildingIdFromToken(String token) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return (int) claims.get("buildingId");
        } catch (Exception e) {
            throw new Exception("Failed to extract buildingId from token: " + e.getMessage());
        }
    }

//    public List<String> getAllUserTokens(int userId, List<String> allTokens) {
//        List<String> userTokens = new ArrayList<>();
//        for (String token : allTokens) {
//            try {
//                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//                if (Integer.parseInt(claims.getSubject()) == userId) {
//                    userTokens.add(token);
//                }
//            } catch (Exception ignored) {
//            }
//        }
//        return userTokens;
//    }

//    public Set<String> getUserTokens(int userId) {
//        Set<String> tokens = userTokensMap.get(userId);
//
//        // Kiểm tra nếu HashSet không null và không rỗng
//        if (tokens != null && !tokens.isEmpty()) {
//            System.out.println("Tokens for user " + userId + ":");
//            for (String t : tokens) {
//                System.out.println(t);
//            }
//        } else {
//            System.out.println("No tokens found for user " + userId);
//        }
//
////        return userTokensMap.getOrDefault(userId, Collections.emptySet());
//        return tokens;
//    }
    public void addToBlacklist(String token) {
//        System.out.println("add blacklist: "+token);
        synchronized (blacklist) {
            blacklist.put(token, getExpirationTime(token));
        }
        for (Map.Entry<String, Long> entry : blacklist.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    public boolean isBlacklisted(String token) {
//        System.out.println("check token "+token);
        for (Map.Entry<String, Long> entry : blacklist.entrySet()) {
            System.out.println("Black List: Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        synchronized (blacklist) {
            if (!blacklist.containsKey(token)) return false;
            long expirationTime = blacklist.get(token);
            return System.currentTimeMillis() > expirationTime;
        }
    }

    public void cleanBlacklist() {
        synchronized (blacklist) {
            Iterator<Map.Entry<String, Long>> iterator = blacklist.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                if (System.currentTimeMillis() > entry.getValue()) {
                    iterator.remove();
                }
            }
        }
    }
    private long getExpirationTime(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return claims.getExpiration().getTime();
        } catch (Exception e) {
            return 0; // Return 0 if unable to parse expiration time
        }
    }

}
