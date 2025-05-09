package com.imveis.visita.Imoveis.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final SecureJwtUtil secureJwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenBlacklistService blacklistService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthorizationFilter(SecureJwtUtil secureJwtUtil, UserDetailsServiceImpl userDetailsService, TokenBlacklistService blacklistService) {
        this.secureJwtUtil = secureJwtUtil;
        this.userDetailsService = userDetailsService;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ Ignorar preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getServletPath();
        if (path.equals("/usuarios/login") || path.equals("/usuarios/refresh-token") || path.equals("/usuarios") || path.equals("/usuarios/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (blacklistService.isBlacklisted(token)) {
                sendErrorResponse(response, "Token revoked or logged out");
                return;
            }

            if (secureJwtUtil.validateToken(token)) {
                if (!secureJwtUtil.isDevProfile() && !secureJwtUtil.validateFingerprint(token)) {
                    sendErrorResponse(response, "Invalid token fingerprint");
                    return;
                }

                String username = secureJwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", message);
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
