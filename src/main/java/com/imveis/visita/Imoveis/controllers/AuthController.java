package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.LoginResponse;
import com.imveis.visita.Imoveis.dtos.LogoutRequest;
import com.imveis.visita.Imoveis.dtos.RefreshTokenRequest;
import com.imveis.visita.Imoveis.dtos.SecureLoginRequest;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import com.imveis.visita.Imoveis.security.SecureJwtUtil;
import com.imveis.visita.Imoveis.security.TokenBlacklistService;
import com.imveis.visita.Imoveis.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final PasswordEncoder passwordEncoder;
    private final SecureJwtUtil secureJwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService blacklistService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(PasswordEncoder passwordEncoder, SecureJwtUtil secureJwtUtil, UserDetailsServiceImpl userDetailsService,
                          AuthenticationManager authenticationManager, TokenBlacklistService blacklistService,
                          UsuarioRepository usuarioRepository) {
        this.passwordEncoder = passwordEncoder;
        this.secureJwtUtil = secureJwtUtil;
        this.userDetailsService = userDetailsService;
        this.blacklistService = blacklistService;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody SecureLoginRequest loginRequest) {
        logger.info("🔐 Tentando autenticar usuário: {}", loginRequest.getEmail());

        try {
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


            String raw = "Senha123!";
            String encoded = passwordEncoder.encode(raw);
            System.out.println(encoded);
            logger.info("🔐 Senha123 codificada: {}", encoded);

            // Comparação e logs
            logger.info("🔐 Senha criptografada no banco: {}", usuario.getSenha());
            logger.info("🔐 Senha recebida: {}", loginRequest.getSenha());

            // Codifica a senha recebida só para visualização (não para comparação real)
            String senhaCodificadaTemporaria = passwordEncoder.encode(loginRequest.getSenha());
            logger.info("🔐 Senha recebida codificada agora (será diferente do banco): {}", senhaCodificadaTemporaria);

            // Verifica se a senha enviada bate com a codificada no banco
            boolean senhaConfere = passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha());
            logger.info("🔐 Senha confere com a do banco? {}", senhaConfere);

            if (!senhaConfere) {
                logger.warn("❌ Senha incorreta para usuário {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            // Autenticação formal
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getSenha()
                    )
            );


            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    usuario.getEmail(),
                    usuario.getSenha(),
                    usuario.getAuthorities()
            );

            String accessToken = secureJwtUtil.generateAccessToken(userDetails);
            String refreshToken = secureJwtUtil.generateRefreshToken(userDetails);

            logger.info("✅ Usuário autenticado com sucesso: {}", loginRequest.getEmail());

            return ResponseEntity.ok(new LoginResponse(
                    accessToken,
                    refreshToken,
                    usuario.getId().toString(),
                    usuario.getRoles(),
                    usuario.getNome(),
                    usuario.getEmail()
            ));

        } catch (Exception ex) {
            logger.warn("❌ Login falhou para {}: {}", loginRequest.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (!secureJwtUtil.isRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido para refresh");
        }

        if (refreshToken != null && secureJwtUtil.validateToken(refreshToken)) {
            try {
                long expiration = secureJwtUtil.extractExpiration(refreshToken);
                blacklistService.blacklistToken(refreshToken, expiration);
            } catch (Exception e) {
                logger.warn("Erro ao colocar refresh token na blacklist: {}", e.getMessage());
            }

            String username = secureJwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String newAccessToken = secureJwtUtil.generateAccessToken(userDetails);
            String newRefreshToken = secureJwtUtil.generateRefreshToken(userDetails);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", newAccessToken);
            tokens.put("refresh_token", newRefreshToken);

            return ResponseEntity.ok(tokens);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido ou expirado");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    @RequestBody(required = false) LogoutRequest logoutRequest) {
        boolean accessTokenBlacklisted = false;
        boolean refreshTokenBlacklisted = false;

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);

            try {
                if (secureJwtUtil.validateToken(accessToken)) {
                    long expiration = secureJwtUtil.extractExpiration(accessToken);
                    blacklistService.blacklistToken(accessToken, expiration);
                    accessTokenBlacklisted = true;
                }
            } catch (Exception e) {
                logger.warn("Erro ao colocar access token na blacklist: {}", e.getMessage());
            }
        }

        if (logoutRequest != null && logoutRequest.getRefreshToken() != null) {
            String refreshToken = logoutRequest.getRefreshToken();

            try {
                if (secureJwtUtil.validateToken(refreshToken)) {
                    long expiration = secureJwtUtil.extractExpiration(refreshToken);
                    blacklistService.blacklistToken(refreshToken, expiration);
                    refreshTokenBlacklisted = true;
                }
            } catch (Exception e) {
                logger.warn("Erro ao colocar token na blacklist: {}", e.getMessage());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Logout realizado com sucesso");
        response.put("accessTokenRevoked", accessTokenBlacklisted);
        response.put("refreshTokenRevoked", refreshTokenBlacklisted);

        return ResponseEntity.ok(response);
    }
}
