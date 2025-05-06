package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthService authService;

    @Mock
    private VisitanteService visitanteService;

    private Funcionario funcionario;
    private Visitante visitante;
    private List<Usuario> usuarios;

    @BeforeEach
    void setUp() {
        funcionario = new Funcionario();
        funcionario.setId(BigInteger.ONE);
        funcionario.setNome("João Silva");
        funcionario.setEmail("joao.silva");
        funcionario.setSenha("senha123");
        funcionario.setTelefone("11987654321");
        funcionario.setCpf("12345678900");
        funcionario.setRoles(new HashSet<>());

        visitante = new Visitante();
        visitante.setId(BigInteger.valueOf(2));
        visitante.setNome("Ana Pereira");
        visitante.setEmail("ana.pereira");
        visitante.setSenha("senha456");
        visitante.setTelefone("11912345678");
        visitante.setDataAcesso(LocalDateTime.now());
        visitante.setDataCadastro(LocalDateTime.now().minusDays(1));
        visitante.setRoles(new HashSet<>());

        usuarios = new ArrayList<>();
        usuarios.add(funcionario);
        usuarios.add(visitante);
    }

    @Test
    void testFindAll() {
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.findAll();

        assertEquals(2, result.size());
        assertEquals(funcionario, result.get(0));
        assertEquals(visitante, result.get(1));
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(usuarioRepository.findById(BigInteger.ONE)).thenReturn(Optional.of(funcionario));

        Optional<Usuario> result = usuarioService.findById(BigInteger.ONE);

        assertTrue(result.isPresent());
        assertEquals(funcionario, result.get());
        verify(usuarioRepository, times(1)).findById(BigInteger.ONE);
    }

    @Test
    void testSaveFuncionario() {
        Funcionario newFuncionario = new Funcionario();
        newFuncionario.setNome("Carlos Oliveira");
        newFuncionario.setEmail("carlos.oliveira");
        newFuncionario.setSenha("senha789");
        newFuncionario.setTelefone("11987654321");
        newFuncionario.setCpf("11122233344");
        newFuncionario.setRoles(new HashSet<>());

        when(usuarioRepository.save(any(Funcionario.class))).thenReturn(newFuncionario);
        doNothing().when(authService).encodePassword(any(Usuario.class));
        doNothing().when(authService).atribuirRoleParaUsuario(any(Usuario.class), eq("FUNCIONARIO"));

        Usuario result = usuarioService.save(newFuncionario);

        assertEquals(newFuncionario, result);
        verify(authService, times(1)).encodePassword(newFuncionario);
        verify(usuarioRepository, times(1)).save(newFuncionario);
        verify(authService, times(1)).atribuirRoleParaUsuario(newFuncionario, "FUNCIONARIO");
    }

    @Test
    void testSaveVisitante() {
        Visitante newVisitante = new Visitante();
        newVisitante.setNome("Maria Souza");
        newVisitante.setEmail("maria.souza");
        newVisitante.setSenha("senha101112");
        newVisitante.setTelefone("11912345678");
        newVisitante.setRoles(new HashSet<>());

        when(visitanteService.save(any(Visitante.class))).thenReturn(newVisitante);
        doNothing().when(authService).encodePassword(any(Usuario.class));

        Usuario result = usuarioService.save(newVisitante);

        assertEquals(newVisitante, result);
        assertNotNull(newVisitante.getDataCadastro());
        verify(authService, times(1)).encodePassword(newVisitante);
        verify(visitanteService, times(1)).save(newVisitante);
    }

    @Test
    void testDeleteById() {
        doNothing().when(usuarioRepository).deleteById(BigInteger.ONE);

        usuarioService.deleteById(BigInteger.ONE);

        verify(usuarioRepository, times(1)).deleteById(BigInteger.ONE);
    }

    @Test
    void testFindByTipo() {
        List<Usuario> funcionarios = new ArrayList<>();
        funcionarios.add(funcionario);
        when(usuarioRepository.findByTipo(Funcionario.class)).thenReturn(funcionarios);

        List<Usuario> result = usuarioService.findByTipo(Funcionario.class);

        assertEquals(1, result.size());
        assertEquals(funcionario, result.getFirst());
        verify(usuarioRepository, times(1)).findByTipo(Funcionario.class);
    }
}
