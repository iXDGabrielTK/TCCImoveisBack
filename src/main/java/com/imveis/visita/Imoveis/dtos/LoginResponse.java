package com.imveis.visita.Imoveis.dtos;

import com.imveis.visita.Imoveis.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    private String access_token;
    private String refresh_token;
    private String usuario_Id;
    private Set<Role> tipo;
    private String nome;
    private String email;

    public LoginResponse(String access_token, String refresh_token, String usuario_Id, Set<Role> tipo, String nome, String email) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.usuario_Id = usuario_Id;
        this.tipo = tipo;
        this.nome = nome;
        this.email = email;
    }
}
