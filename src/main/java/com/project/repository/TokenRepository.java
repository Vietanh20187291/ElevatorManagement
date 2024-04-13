package com.project.repository;
import com.project.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByToken(String token);

    Token findByUserId(Integer userId);

}

