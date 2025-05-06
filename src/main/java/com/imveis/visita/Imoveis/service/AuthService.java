package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Role;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.RoleRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class AuthService {

    private static UsuarioRepository usuarioRepository;
    private static RoleRepository roleRepository;
    private static PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        AuthService.usuarioRepository = usuarioRepository;
        AuthService.roleRepository = roleRepository;
        AuthService.passwordEncoder = passwordEncoder;
    }

    public void atribuirRoleParaUsuario(Usuario usuario, String roleName) {
        Optional<Role> roleOpt = roleRepository.findByNome(roleName);
        if (roleOpt.isPresent()) {
            if (usuario.getRoles() == null) {
                usuario.setRoles(new HashSet<>());
            }
            usuario.getRoles().add(roleOpt.get());
            usuarioRepository.save(usuario);
        }
    }

    public void encodePassword(Usuario usuario) {
        System.out.println(">> CRIPTOGRAFANDO senha: " + usuario.getSenha());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }

    @SuppressWarnings("unused")
    public boolean verificarSenha(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}