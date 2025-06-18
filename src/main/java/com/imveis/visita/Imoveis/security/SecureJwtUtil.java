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

/**
 * Classe que gera e valida os tokens, além de verificar fingerprint
 */
@Component
@VisibleForTesting
public class SecureJwtUtil {

    // Logger para registrar mensagens de depuração e erro.
    private static final Logger logger = LoggerFactory.getLogger(SecureJwtUtil.class);
    // Tamanho mínimo da chave secreta
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

        // Verifica se a chave secreta é nula ou muito curta
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
        // Assinatura digital do token usando algoritmo HMAC SHA-256
        // (Garante que o conteúdo do token não foi alterado — uma assinatura digital)
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isDevProfile() {
        return Optional.ofNullable(System.getProperty("spring.profiles.active"))
                .map(profile -> profile.contains("dev"))
                .orElse(true);
    }

    /**
     * Gera uma chave secreta aleatória para uso em desenvolvimento.
     * Esta chave deve ser substituída por uma chave segura em produção.
     *
     * @return Chave secreta gerada
     */
    private String generateSecureSecret() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[MIN_SECRET_LENGTH];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Gera um token de acesso JWT para o usuário fornecido.
     *
     * @param userDetails Detalhes do usuário
     * @return Token de acesso JWT
     */
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        addFingerprintData(claims);
        return createToken(claims, userDetails.getUsername(), accessTokenExpiration);
    }

    /**
     * Gera um token de atualização JWT para o usuário fornecido.
     *
     * @param userDetails Detalhes do usuário
     * @return Token de atualização JWT
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        addFingerprintData(claims);
        claims.put("tokenType", "refresh");
        return createToken(claims, userDetails.getUsername(), refreshTokenExpiration);
    }

    /**
     * Adiciona dados de impressão digital ao token JWT.
     *
     * @param claims Mapa de reivindicações do token
     */
    private void addFingerprintData(Map<String, Object> claims) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // Adiciona o IP do cliente e o User-Agent às reivindicações
                claims.put("ip", getClientIp(request));

                // Adiciona o User-Agent do cliente às reivindicações
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

    /**
     * Obtém o endereço IP do cliente a partir do cabeçalho X-Forwarded-For ou do endereço remoto.
     *
     * @param request Requisição HTTP
     * @return Endereço IP do cliente
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Cria um token JWT com as reivindicações fornecidas.
     *
     * @param claims     Reivindicações do token
     * @param subject    Assunto do token
     * @param expiration Tempo de expiração do token em milissegundos
     * @return Token JWT gerado
     */
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

    /**
     * Valida o token JWT fornecido.
     *
     * @param token Token JWT a ser validado
     * @return true se o token for válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            if (expiration == null || expiration.before(new Date())) {
                throw new JwtException("Token expirado");
            }
            return true;
        } catch (JwtException e) {
            logger.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Valida a impressão digital do token JWT.
     *
     * @param token Token JWT a ser validado
     * @return true se a impressão digital for válida, false caso contrário
     */
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

    /**
     * Extrai o nome de usuário do token JWT.
     *
     * @param token Token JWT
     * @return Nome de usuário extraído
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrai a data de expiração do token JWT.
     *
     * @param token Token JWT
     * @return Data de expiração em milissegundos desde a época
     */
    public long extractExpiration(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    }

    /**
     * Verifica se o token JWT é um token de atualização.
     *
     * @param token Token JWT
     * @return true se for um token de atualização, false caso contrário
     */
    @SuppressWarnings("unused")
    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        return "refresh".equals(claims.get("tokenType", String.class));
    }

    /**
     * Extrai as funções (roles) do token JWT.
     *
     * @param token Token JWT
     * @return Lista de funções extraídas
     */
    List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Object roles = claims.get("roles");
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Extrai todas as reivindicações do token JWT.
     *
     * @param token Token JWT
     * @return Reivindicações extraídas
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
