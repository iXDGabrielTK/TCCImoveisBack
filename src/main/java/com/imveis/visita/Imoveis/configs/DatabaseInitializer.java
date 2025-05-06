package com.imveis.visita.Imoveis.configs;

import com.imveis.visita.Imoveis.entities.Role;
import com.imveis.visita.Imoveis.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initDatabase() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("FUNCIONARIO");
        createRoleIfNotExists("VISITANTE");
    }

    private void createRoleIfNotExists(String roleName) {
        boolean exists = roleRepository.existsByNome(roleName);
        if (!exists) {
            roleRepository.save(new Role(roleName));
        }
    }
}
