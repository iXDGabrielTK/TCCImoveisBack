package com.imveis.visita.Imoveis.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecureJwtUtilTest {

    private final String SECRET_KEY = "EsteÉUmTextoMuitoLongoQueContémMaisDeSessentaEQuatroCaracteresSemQualquerEspaço";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    @InjectMocks
    private SecureJwtUtil secureJwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(secureJwtUtil, "secretKey", secretKey);
        ReflectionTestUtils.setField(secureJwtUtil, "accessTokenExpiration", 3600000L); // 1 hour
        ReflectionTestUtils.setField(secureJwtUtil, "refreshTokenExpiration", 86400000L); // 1 day

        userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                );
            }

            @Override
            public String getPassword() {
                return "password";
            }

            @Override
            public String getUsername() {
                return "testuser";
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    @Test
    void testGerarToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Mozilla/5.0");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String token = secureJwtUtil.generateAccessToken(userDetails);

        assertNotNull(token);
        assertTrue(secureJwtUtil.validateToken(token));
        assertEquals("testuser", secureJwtUtil.extractUsername(token));

        List<String> roles = secureJwtUtil.extractRoles(token);
        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testGerarRefreshToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Mozilla/5.0");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String refreshToken = secureJwtUtil.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
        assertTrue(secureJwtUtil.validateToken(refreshToken));
        assertEquals("testuser", secureJwtUtil.extractUsername(refreshToken));

        long refreshExpiration = secureJwtUtil.extractExpiration(refreshToken);
        long now = System.currentTimeMillis();
        assertTrue(refreshExpiration > now + 3600000L);

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testValidarToken() {
        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000L))
                .signWith(secretKey)
                .compact();

        assertTrue(secureJwtUtil.validateToken(token));

        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 7200000L))
                .setExpiration(new Date(System.currentTimeMillis() - 3600000L))
                .signWith(secretKey)
                .compact();

        assertFalse(secureJwtUtil.validateToken(expiredToken));
    }

    @Test
    void testValidarFingerprint() {
        // 1 - Requisição original válida
        MockHttpServletRequest request1 = new MockHttpServletRequest();
        request1.setRemoteAddr("127.0.0.1");
        request1.addHeader("User-Agent", "Mozilla/5.0");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request1));

        String token = secureJwtUtil.generateAccessToken(userDetails);
        assertTrue(secureJwtUtil.validateFingerprint(token));

        // 2 - Alterar IP
        MockHttpServletRequest request2 = new MockHttpServletRequest();
        request2.setRemoteAddr("192.168.1.1");
        request2.addHeader("User-Agent", "Mozilla/5.0");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request2));

        assertFalse(secureJwtUtil.validateFingerprint(token));

        // 3 - Alterar User-Agent
        MockHttpServletRequest request3 = new MockHttpServletRequest();
        request3.setRemoteAddr("127.0.0.1");
        request3.addHeader("User-Agent", "Chrome/123");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request3));

        assertFalse(secureJwtUtil.validateFingerprint(token));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void testExtrairNome() {
        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000L))
                .signWith(secretKey)
                .compact();

        String username = secureJwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testExtrairRoles() {
        List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        String token = Jwts.builder()
                .setSubject("testuser")
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000L))
                .signWith(secretKey)
                .compact();

        List<String> extractedRoles = secureJwtUtil.extractRoles(token);
        assertNotNull(extractedRoles);
        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ROLE_USER"));
        assertTrue(extractedRoles.contains("ROLE_ADMIN"));
    }

    @Test
    void testExtrairExpiracao() {
        long expirationTime = System.currentTimeMillis() + 3600000L;
        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationTime))
                .signWith(secretKey)
                .compact();

        long extractedExpiration = secureJwtUtil.extractExpiration(token);
        assertTrue(Math.abs(expirationTime - extractedExpiration) < 1000);
    }
}
