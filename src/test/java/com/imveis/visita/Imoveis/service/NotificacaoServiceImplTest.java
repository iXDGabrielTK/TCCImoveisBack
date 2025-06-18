package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.NotificacaoProposta;
import com.imveis.visita.Imoveis.entities.Proposta;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.NotificacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceImplTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoServiceImpl notificacaoService;

    @Test
    void criarNotificacaoProposta_deveSalvarComSucesso() {
        // Arrange
        Proposta propostaMock = mock(Proposta.class);
        Usuario usuarioMock = mock(Usuario.class);
        Imovel imovelMock = mock(Imovel.class);

        // Apenas os stubbings realmente usados:
        when(propostaMock.getValorFinanciamento()).thenReturn(new BigDecimal("200000"));
        when(propostaMock.getUsuario()).thenReturn(usuarioMock);
        when(propostaMock.getImovel()).thenReturn(imovelMock);
        when(usuarioMock.getNome()).thenReturn("Gabriel");
        when(imovelMock.getIdImovel()).thenReturn(2L);

        ArgumentCaptor<NotificacaoProposta> captor = ArgumentCaptor.forClass(NotificacaoProposta.class);

        // Act
        notificacaoService.criarNotificacaoProposta(propostaMock, usuarioMock);

        // Assert
        verify(notificacaoRepository).save(captor.capture());
        NotificacaoProposta resultado = captor.getValue();

        assertEquals("Gabriel enviou uma proposta de R$ 200000 para o imóvel ID 2", resultado.getResumo());
        assertEquals(propostaMock, resultado.getProposta());
        assertNotNull(resultado.getDataCriacao());
        assertFalse(resultado.isLida());
    }
}