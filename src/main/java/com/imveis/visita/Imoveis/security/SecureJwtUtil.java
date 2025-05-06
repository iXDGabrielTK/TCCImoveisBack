package com.imveis.visita.Imoveis.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Component
@VisibleForTesting
public class SecureJwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecureJwtUtil.class);
    private static final int MIN_SECRET_LENGTH = 64;

    private SecretKey secretKey;

    @Value("${jwt.expiration:3600000}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-expiration:86400000}")
    private Long refreshTokenExpiration;

    @Value("${jwt.secret:}")
    private String configuredSecret;

    @PostConstruct
    public void init() {
        String secret = configuredSecret;

        if (secret == null || secret.length() < MIN_SECRET_LENGTH) {
            logger.warn("JWT secret is not configured or too short! This is insecure for production.");

            if (secret == null || secret.isEmpty()) {
                secret = generateSecureSecret();
                logger.info("Generated a random JWT secret for development use only");
            }

            if (!isDevProfile()) {
                throw new IllegalStateException("JWT secret is insecure or missing in production!");
            }
        }

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isDevProfile() {
        return Optional.ofNullable(System.getProperty("spring.profiles.active"))
                .map(profile -> profile.contains("dev"))
                .orElse(true);
    }

    private String generateSecureSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[MIN_SECRET_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        addFingerprintData(claims);
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        addFingerprintData(claims);
        claims.put("tokenType", "refresh");
        return createToken(claims, userDetails.getUsername(), refreshTokenExpiration);
    }

    private void addFingerprintData(Map<String, Object> claims) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                claims.put("ip", getClientIp(request));

                String userAgent = request.getHeader("User-Agent");
                if (userAgent != null) {
                    claims.put("userAgent", userAgent);
                }

                claims.put("timestamp", System.currentTimeMillis());
            }
        } catch (Exception e) {
            logger.warn("Error adding fingerprint data: {}", e.getMessage());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration != null && expiration.after(new Date());
        } catch (JwtException e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }


    boolean validateFingerprint(String token) {
        try {
            Claims claims = extractAllClaims(token);

            if (!claims.containsKey("ip") && !claims.containsKey("userAgent")) {
                return true;
            }

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                logger.debug("No request context available for fingerprint validation");
                return true;
            }

            HttpServletRequest request = attributes.getRequest();

            if (claims.containsKey("ip")) {
                String tokenIp = claims.get("ip", String.class);
                String currentIp = getClientIp(request);
                if (!tokenIp.equals(currentIp)) {
                    logger.warn("IP mismatch: token={}, request={}", tokenIp, currentIp);
                    return false;
                }
            }

            if (claims.containsKey("userAgent")) {
                String tokenUserAgent = claims.get("userAgent", String.class);
                String currentUserAgent = request.getHeader("User-Agent");
                if (!Objects.equals(tokenUserAgent, currentUserAgent)) {
                    logger.warn("User-Agent mismatch");
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("Fingerprint validation error: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    List<String> extractRoles(String token) {
        Object roles = extractAllClaims(token).get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public long extractExpiration(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    }

    @SuppressWarnings("unused")
    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return "refresh".equals(claims.get("tokenType", String.class));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
