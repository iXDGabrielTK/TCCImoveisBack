package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.EnderecoDTO;
import com.imveis.visita.Imoveis.dtos.FavoritoDTO;
import com.imveis.visita.Imoveis.dtos.FotoImovelDTO;
import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.Favorito;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.Usuario;
import com.imveis.visita.Imoveis.repositories.FavoritoRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import com.imveis.visita.Imoveis.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ImovelRepository imovelRepository;
    private final UsuarioRepository usuarioRepository;

    public FavoritoService(FavoritoRepository favoritoRepository, ImovelRepository imovelRepository, UsuarioRepository usuarioRepository) {
        this.favoritoRepository = favoritoRepository;
        this.imovelRepository = imovelRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void favoritar(Long usuarioId, Long imovelId) {
        if (favoritoRepository.findByUsuarioIdAndImovelId(usuarioId, imovelId).isPresent()) {
            throw new IllegalArgumentException("Imóvel já favoritado.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Imovel imovel = imovelRepository.findById(imovelId)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado"));

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setImovel(imovel);
        favoritoRepository.save(favorito);
    }

    public void desfavoritar(Long usuarioId, Long imovelId) {
        favoritoRepository.deleteByUsuarioIdAndImovelId(usuarioId, imovelId);
    }

    public List<FavoritoDTO> listarFavoritos(Long usuarioId) {
        List<Favorito> favoritos = favoritoRepository.findAllByUsuarioId(usuarioId);

        return favoritos.stream().map(f -> {
            Imovel imovel = f.getImovel();

            Endereco endereco = imovel.getEnderecoImovel();
            EnderecoDTO enderecoDTO = null;
            if (endereco != null) {
                enderecoDTO = new EnderecoDTO();
                enderecoDTO.setRua(endereco.getRua());
                enderecoDTO.setBairro(endereco.getBairro());
                enderecoDTO.setCidade(endereco.getCidade());
                enderecoDTO.setEstado(endereco.getEstado());
            }

            List<FotoImovelDTO> fotosDTO = new ArrayList<>();
            if (imovel.getFotosImovel() != null && !imovel.getFotosImovel().isEmpty()) {
                FotoImovelDTO primeiraFoto = new FotoImovelDTO();
                primeiraFoto.setUrlFotoImovel(imovel.getFotosImovel().getFirst().getUrlFotoImovel());
                fotosDTO.add(primeiraFoto);
            }

            FavoritoDTO dto = new FavoritoDTO();
            dto.setIdImovel(imovel.getIdImovel());
            dto.setTipoImovel(imovel.getTipoImovel());
            dto.setPrecoImovel(imovel.getPrecoImovel());
            dto.setDescricaoImovel(imovel.getDescricaoImovel());
            dto.setEnderecoImovel(enderecoDTO);
            dto.setFotosImovel(fotosDTO);
            return dto;
        }).toList();
    }

}
