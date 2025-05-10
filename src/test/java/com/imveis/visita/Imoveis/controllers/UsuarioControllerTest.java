package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.entities.Visitante;
import com.imveis.visita.Imoveis.service.AuthService;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import com.imveis.visita.Imoveis.service.VisitanteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private VisitanteService visitanteService;

    @MockBean
    private FuncionarioService funcionarioService;

    @MockBean
    private AuthService authService;

    private Funcionario funcionario;

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

        Visitante visitante = new Visitante();
        visitante.setId(BigInteger.valueOf(2));
        visitante.setNome("Ana Pereira");
        visitante.setEmail("ana.pereira");
        visitante.setSenha("senha456");
        visitante.setTelefone("11912345678");
        visitante.setDataAcesso(LocalDateTime.now());
        visitante.setDataCadastro(LocalDateTime.now().minusDays(1));
        visitante.setRoles(new HashSet<>());
    }

    @Test
    void testCriarVisitante() throws Exception {
        var json = """
                {
                  "nome": "Maria Souza",
                  "email": "maria.souza",
                  "senha": "senha789",
                  "telefone": "11912345678",
                  "tipo": "visitante"
                }
                """;

        when(visitanteService.save(any(Visitante.class))).thenAnswer(invocation -> {
            Visitante v = invocation.getArgument(0);
            v.setId(BigInteger.TWO);
            return v;
        });

        doNothing().when(authService).encodePassword(any(Usuario.class));
        doNothing().when(authService).atribuirRoleParaUsuario(any(Usuario.class), eq("VISITANTE"));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Maria Souza")))
                .andExpect(jsonPath("$.email", is("maria.souza")));

        verify(authService).encodePassword(any(Usuario.class));
        verify(authService).atribuirRoleParaUsuario(any(Usuario.class), eq("VISITANTE"));
        verify(visitanteService).save(any(Visitante.class));
    }

    @Test
    void testCriarFuncionario() throws Exception {

        String json = """
                {
                  "nome": "Carlos Oliveira",
                  "email": "carlos.oliveira",
                  "senha": "senha123",
                  "telefone": "44999999999",
                  "tipo": "FUNCIONARIO",
                  "cpf": "11122233344"
                }
                """;

        when(funcionarioService.save(any(Funcionario.class))).thenAnswer(invocation -> {
            Funcionario f = invocation.getArgument(0);
            f.setId(BigInteger.TWO);
            return f;
        });
        doNothing().when(authService).encodePassword(any(Usuario.class));
        doNothing().when(authService).atribuirRoleParaUsuario(any(Usuario.class), eq("FUNCIONARIO"));

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Carlos Oliveira")))
                .andExpect(jsonPath("$.email", is("carlos.oliveira")))
                .andExpect(jsonPath("$.cpf", is("11122233344")));

        verify(authService).encodePassword(any(Usuario.class));
        verify(authService).atribuirRoleParaUsuario(any(Usuario.class), eq("FUNCIONARIO"));
        verify(funcionarioService).save(any(Funcionario.class));
    }

    @Test
    void testGetUsuarioById() throws Exception {
        when(usuarioService.findById(BigInteger.ONE)).thenReturn(Optional.of(funcionario));

        mockMvc.perform(get("/usuarios/{id}", BigInteger.ONE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João Silva")));

        verify(usuarioService).findById(BigInteger.ONE);
    }

    @Test
    void testGetUsuarioByIdNotFound() throws Exception {
        when(usuarioService.findById(BigInteger.valueOf(999))).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/{id}", BigInteger.valueOf(999)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado."));

        verify(usuarioService).findById(BigInteger.valueOf(999));
    }

    @Test
    void testAtualizarUsuario() throws Exception {
        var json = """
                {
                  "nome": "João Atualizado",
                  "email": "joao.atualizado",
                  "senha": "",
                  "telefone": "11999999999",
                  "tipo": "funcionario"
                }
                """;

        Funcionario atualizado = new Funcionario();
        atualizado.setId(BigInteger.ONE);
        atualizado.setNome("João Atualizado");
        atualizado.setEmail("joao.atualizado");
        atualizado.setTelefone("11999999999");
        atualizado.setCpf("12345678900");
        atualizado.setRoles(new HashSet<>());



        when(usuarioService.findById(BigInteger.ONE)).thenReturn(Optional.of(funcionario));
        when(usuarioService.save(any(Usuario.class))).thenReturn(atualizado);

        mockMvc.perform(put("/usuarios/{id}", BigInteger.ONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João Atualizado")))
                .andExpect(jsonPath("$.email", is("joao.atualizado")));

        verify(usuarioService).findById(BigInteger.ONE);
        verify(usuarioService).save(any(Usuario.class));
    }

    @Test
    void testListarUsuariosPorTipo() throws Exception {
        when(usuarioService.findByTipo(Funcionario.class)).thenReturn(List.of(funcionario));

        mockMvc.perform(get("/usuarios/tipo/{tipo}", "funcionario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("João Silva")));

        verify(usuarioService).findByTipo(Funcionario.class);
    }

    @Test
    void testDeletarUsuario() throws Exception {
        doNothing().when(usuarioService).deleteById(BigInteger.ONE);

        mockMvc.perform(delete("/usuarios/{id}", BigInteger.ONE))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário excluído com sucesso!"));

        verify(usuarioService).deleteById(BigInteger.ONE);
    }
}
