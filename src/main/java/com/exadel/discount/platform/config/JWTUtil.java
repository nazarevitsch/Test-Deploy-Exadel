package com.exadel.discount.platform.config;

import com.exadel.discount.platform.domain.UserRole;
import com.exadel.discount.platform.domain.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value(value = "${jwt.access.secret.key}")
    private String ACCESS_SECRET_KEY;

    @Value(value = "${jwt.access.available.time}")
    private int ACCESS_AVAILABLE_TIME;

    @Value(value = "${jwt.refresh.secret.key}")
    private String REFRESH_SECRET_KEY;

    @Value(value = "${jwt.refresh.available.time}")
    private int REFRESH_AVAILABLE_TIME;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateTOKEN(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", ((MyUserDetails)userDetails).getUserId());
        claims.put("admin", ((MyUserDetails)userDetails).getUserRole() == UserRole.ADMINISTRATOR);
        return createToken(claims, userDetails.getUsername());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(ACCESS_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_AVAILABLE_TIME))
                .signWith(SignatureAlgorithm.HS256, ACCESS_SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", ((MyUserDetails)userDetails).getUserId());
        claims.put("admin", ((MyUserDetails)userDetails).getUserRole() == UserRole.ADMINISTRATOR);
        return createRefreshToken(claims, userDetails.getUsername());
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_AVAILABLE_TIME))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY).compact();
    }

    public Boolean validateRefreshToken(String token, UserDetails userDetails) {
        final String username = extractUsernameRefreshToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpiredRefreshToken(token);
    }

    public String extractUsernameRefreshToken(String token) {
        return extractClaimRefreshToken(token, Claims::getSubject);
    }

    public <T> T extractClaimRefreshToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaimsRefreshToken(String token) {
        return Jwts.parser().setSigningKey(REFRESH_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Date extractExpirationRefreshToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpiredRefreshToken(String token) {
        return extractExpirationRefreshToken(token).before(new Date());
    }
}
