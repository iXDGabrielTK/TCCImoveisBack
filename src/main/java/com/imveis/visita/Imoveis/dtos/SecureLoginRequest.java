package com.imveis.visita.Imoveis.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Secure login request DTO with input validation
 */
@Getter
@Setter
public class SecureLoginRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 255, message = "Email não pode exceder 255 caracteres")
    private String email;

    @Setter
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 100, message = "Senha deve ter entre 8 e 100 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Senha deve conter pelo menos um número, uma letra maiúscula, uma letra minúscula, um caractere especial e não deve conter espaços"
    )
    private String senha;

    // Default constructor for JSON deserialization
    public SecureLoginRequest() {
    }

    public SecureLoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        // Trim and sanitize input
        if (email != null) {
            email = email.trim();
            // Basic XSS prevention
            email = email.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        // Don't include a password in toString for security
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", senha='[PROTECTED]'" +
                '}';
    }
}