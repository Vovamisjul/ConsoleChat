package com.vovamisjul.chatlogic.security.jwt;


import com.vovamisjul.chatlogic.Users;
import com.vovamisjul.database.DataBaseController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secret = "SECRETsecret";

    private long validityInMilliseconds = 60*60*1000;

    private DataBaseController dataBaseController;

    private final Users users;

    public JwtTokenProvider(Users users, DataBaseController dataBaseController) {
        this.users = users;
        this.dataBaseController = dataBaseController;
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String createToken(int id, String type, String password) {

        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("role", type);
        claims.put("password", password);

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

    private String resolveToken(String header) {
        if (header != null && (header.startsWith("Bearer_") || header.startsWith("Bearer "))) {
            return header.substring("Bearer_".length());
        }
        return null;
    }

    public JwtUser validateTokenAgent(String token) {
        try {
            token = resolveToken(token);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Claims body = claims.getBody();
            if (body.getExpiration().before(new Date())) {
                return null;
            }
            if  (dataBaseController.checkUser((String)body.get("sub"), (String)body.get("password"), (String)body.get("role"))
                    && body.get("role").equals("agent"))
                return new JwtUser((String)body.get("role"), (String)body.get("password"), (String)body.get("sub"));
            return null;
        } catch (JwtException | IllegalArgumentException | SQLException e) {
            return null;
        }
    }

    public JwtUser validateTokenAll(String token) {
        try {
            token = resolveToken(token);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Claims body = claims.getBody();
            if (body.getExpiration().before(new Date())) {
                return null;
            }
            return new JwtUser((String)body.get("role"), (String)body.get("password"), (String)body.get("sub"));
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
