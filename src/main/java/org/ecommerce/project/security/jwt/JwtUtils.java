package org.ecommerce.project.security.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

//import java.util.logging.Logger;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.expiration-in-ms}")
    private long jwtExpirationInMs;
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookiesName}")
    private String jwtCookie;
//    Getting JWT From Header
//    public  String getJwtFromHeader(HttpServletRequest request){
//        String bearerToken=request.getHeader("Authorization");
//        logger.debug("Authorization Bearer Token: {}",bearerToken);
//        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
public String getJwtCookies(HttpServletRequest request){
    Cookie cookie= WebUtils.getCookie(request,jwtCookie);
    if (cookie!=null){
        return cookie.getValue();
    }
    return null;
}

    public String getJwtFromHeader(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }


    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal){
        String jwt=generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie=ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();
        return cookie;
    }

//    Generating Token from Username
    public  String generateTokenFromUsername(String username){

        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(new Date((new Date().getTime()+jwtExpirationInMs))).signWith(Key()).compact();
    }
//    Getting Username from JWT Token
    public String getUserNameFromJWTToken(String token){
       return Jwts.parser().verifyWith((SecretKey) Key())
               .build()
               .parseSignedClaims(token)
               .getPayload().getSubject();
    }
//    Generate Signing Key
    private Key Key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();
        return cookie;
    }

//    Validate JWT Token
    public boolean validateJwtToken(String authToken){
        try{
            System.out.println("Validate");
            Jwts.parser().verifyWith((SecretKey) Key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException exception) {
            logger.error("Invalid JWT token: {}", exception.getMessage());
        }
        catch (ExpiredJwtException exception){
            logger.error("Expired JWT token: {}", exception.getMessage());
        }
        catch (IllegalArgumentException exception){
            logger.error("JWT claims string is empty: {}", exception.getMessage());
        }
        catch (UnsupportedJwtException exception){
            logger.error("JWT token is unsupported: {}", exception.getMessage());
        }
        return false;
    }
    
}
