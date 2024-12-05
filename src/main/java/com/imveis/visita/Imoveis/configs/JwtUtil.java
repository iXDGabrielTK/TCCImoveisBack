package com.imveis.visita.Imoveis.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey chaveSecreta;

    @Value("${jwt.secret}")
    public void setChaveSecreta(String secret) {
        this.chaveSecreta = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(String nome) {
        return Jwts.builder()
                .setSubject(nome)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(chaveSecreta, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(chaveSecreta).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    public String extrairNome(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(chaveSecreta)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
