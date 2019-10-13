package com.vovamisjul.chatlogic.security.jwt;


import com.vovamisjul.chatlogic.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secret = "SECRETsecret";

    private long validityInMilliseconds = 60*60*1000;

    private final Users users;

    public JwtTokenProvider(Users users) {
        this.users = users;
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String createToken(int id, String type) {

        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("role", type);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

//    Authentication getAuthentication(String token) {
//        UserDetails userDetails = loadUserById(getUserId(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    private String getUserId(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(String header) {
        if (header != null && (header.startsWith("Bearer_") || header.startsWith("Bearer "))) {
            return header.substring("Bearer_".length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return claims.getBody().get("role").equals("agent");
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
