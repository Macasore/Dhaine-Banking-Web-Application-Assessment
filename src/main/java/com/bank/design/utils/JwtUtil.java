package com.bank.design.utils;

import ch.qos.logback.core.util.TimeUtil;
import com.bank.design.model.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final String secretKey = "mysecretkeyisyamandmustbelongerthan32charsoritwillfail";

    private final String TOKEN_HEADER = "Authorization";

    private final String TOKEN_PREFIX = "Bearer ";

    private final JwtParser jwtParser;

    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
    }

    public String createToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getAccountName());
        Date tokenCreateTime = new Date();
        long accessTokenValidity = 60;
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims parseJwtClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest httpServletRequest){
        try{
            String token = resolveToken(httpServletRequest);

            if (token != null){
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex){
            httpServletRequest.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex){
            httpServletRequest.setAttribute("invalid", ex.getMessage());
            throw ex;
        }

    }

    public String resolveToken(HttpServletRequest httpServletRequest){
        String bearerToken = httpServletRequest.getHeader(TOKEN_HEADER);

        if (bearerToken != null && bearerToken.contains(TOKEN_PREFIX)){
            return bearerToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public String getAccountName(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

}
