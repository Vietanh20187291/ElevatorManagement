package com.project.service;
import com.project.entity.Token;
import com.project.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token createToken(String token, Integer userId, LocalDateTime expiresAt) {
        Token newToken = new Token();
        newToken.setToken(token);
        newToken.setUserId(userId);
        newToken.setCreatedAt(LocalDateTime.now());
        newToken.setExpiresAt(expiresAt);
        return tokenRepository.save(newToken);
    }

    public Token getTokenByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Token getTokenByUserId(Integer userId) {
        return tokenRepository.findByUserId(userId);
    }

    // Other methods for token management
}

