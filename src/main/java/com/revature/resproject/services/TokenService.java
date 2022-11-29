package com.revature.resproject.services;

import com.revature.resproject.dtos.responses.Principal;
import com.revature.resproject.models.Role;
import com.revature.resproject.utils.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class TokenService {
    private JwtConfig jwtConfig;

    public TokenService() {
        super();
    }

    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Principal subject) {
        long now = System.currentTimeMillis();
        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(String.valueOf(subject.getId()))
                .setIssuer("yolp")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration()))
                .setSubject(subject.getUsername())
                .claim("role", subject.getRole())
                .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return tokenBuilder.compact();
    }

    public Principal extractRequesterDetails(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
            return new Principal(Integer.parseInt(claims.getId()), claims.getSubject(), Role.valueOf(claims.get("role", String.class)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
