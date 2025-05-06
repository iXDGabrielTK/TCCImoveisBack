package com.imveis.visita.Imoveis.security;

import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println("Buscando por email: " + login);
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(login);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o login: " + login);
        }

        Usuario usuario = usuarioOpt.get();

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                true, true, true, true,
                getAuthorities(usuario)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        var authorities = usuario.getRoles().stream()
                .map(role -> {
                    System.out.println("Atribuindo papel: " + role.getNome());
                    return new SimpleGrantedAuthority("ROLE_" + role.getNome());
                })
                .collect(Collectors.toList());

        System.out.println("Authorities finais: " + authorities);
        return authorities;
    }

}