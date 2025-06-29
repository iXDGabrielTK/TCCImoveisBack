package com.imveis.visita.Imoveis.configs;

import com.imveis.visita.Imoveis.security.JwtAuthorizationFilter;
import com.imveis.visita.Imoveis.security.SecureJwtUtil;
import com.imveis.visita.Imoveis.security.TokenBlacklistService;
import com.imveis.visita.Imoveis.security.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração de segurança para a aplicação.
 * Define as regras de autenticação, autorização e CORS.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecureSecurityConfig {

    private final SecureJwtUtil secureJwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenBlacklistService blacklistService;

    public SecureSecurityConfig(SecureJwtUtil secureJwtUtil, UserDetailsServiceImpl userDetailsService, TokenBlacklistService blacklistService) {
        this.secureJwtUtil = secureJwtUtil;
        this.userDetailsService = userDetailsService;
        this.blacklistService = blacklistService;
    }

    @PostConstruct
    public void init() {
        System.out.println("⚙️ SecureSecurityConfig ATIVO");
    }

    /**
     * Configuração de CORS para permitir requisições de origens específicas.
     *
     * @return A configuração de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSourceSecurity() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://686094cf97cef849433a8e77--tccfrontimoveis.netlify.app/"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configuração do filtro de segurança.
     *
     * @param http O objeto HttpSecurity
     * @return O SecurityFilterChain configurado
     * @throws Exception Se ocorrer um erro durante a configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSourceSecurity()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios/login", "/usuarios").permitAll()
                        .requestMatchers("/usuarios/refresh-token/**", "/usuarios/logout/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers("/home/**").hasAnyRole("FUNCIONARIO", "VISITANTE", "CORRETOR")
                        .requestMatchers("/imoveis/**").permitAll()
                        .requestMatchers("/agendamentos/**").authenticated()
                        .requestMatchers("/relatorios/**").hasRole("FUNCIONARIO")
                        .requestMatchers("/api/funcionario/**").hasAuthority("FUNCIONARIO")
                        .requestMatchers("/corretores/**").hasRole("VISITANTE")
                        .requestMatchers("/propostas").hasAnyRole("VISITANTE", "FUNCIONARIO", "CORRETOR")
                        .requestMatchers("/simulacoes").hasAnyRole("VISITANTE", "FUNCIONARIO", "CORRETOR")
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtAuthorizationFilter(secureJwtUtil, userDetailsService, blacklistService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuração do AuthenticationProvider para autenticação de usuários.
     *
     * @return O DaoAuthenticationProvider configurado
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configuração do PasswordEncoder para codificação de senhas.
     *
     * @return O PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
