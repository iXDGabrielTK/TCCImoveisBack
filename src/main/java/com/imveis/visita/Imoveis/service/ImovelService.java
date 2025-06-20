package com.imveis.visita.Imoveis.service;

import com.imveis.visita.Imoveis.dtos.ImovelDTO;
import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.repositories.EnderecoRepository;
import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImovelService {

    private final ImovelRepository imovelRepository;

    private final EnderecoRepository enderecoRepository;

    public ImovelService(ImovelRepository imovelRepository, EnderecoRepository enderecoRepository) {
        this.imovelRepository = imovelRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public Page<ImovelDTO> findAllPaginado(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("idImovel").descending());
        Page<Long> pageIds = imovelRepository.findAllIdsPaginado(pageable);

        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Imovel> imoveis = imovelRepository.findAllByIdInWithFotosAndEndereco(pageIds.getContent());
        List<ImovelDTO> dtos = imoveis.stream().map(ImovelDTO::new).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, pageIds.getTotalElements());
    }

    public Page<ImovelDTO> findDisponiveisPorValorPaginado(double valorMax, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("idImovel").descending());
        Page<Long> pageIds = imovelRepository.findDisponiveisIdsPorValorMax(BigDecimal.valueOf(valorMax), pageable);

        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Imovel> imoveis = imovelRepository.findAllByIdInWithFotosAndEndereco(pageIds.getContent());
        List<ImovelDTO> dtos = imoveis.stream().map(ImovelDTO::new).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, pageIds.getTotalElements());
    }

    public Optional<Imovel> findById(Long id) {
        return imovelRepository.findByIdWithEnderecoAndFotos(id);
    }


    public Optional<ImovelDTO> findDTOById(Long id) {
        return imovelRepository.findByIdWithEnderecoAndFotos(id)
                .map(ImovelDTO::new);
    }

    public Imovel save(Imovel imovel) {
        if (imovel.getEnderecoImovel() != null) {
            Endereco endereco = imovel.getEnderecoImovel();
            if (endereco.getIdEndereco() == null) {
                Endereco enderecoSalvo = enderecoRepository.save(endereco);
                imovel.setEnderecoImovel(enderecoSalvo);
            } else {
                Endereco enderecoExistente = enderecoRepository.findById(endereco.getIdEndereco())
                        .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado."));
                imovel.setEnderecoImovel(enderecoExistente);
            }
        }

        if (imovel.getPrecoImovel() < 0) {
            throw new IllegalArgumentException("O preço do imóvel não pode ser negativo");
        }

        return imovelRepository.save(imovel);
    }

    public void cancelarImovel(Long id) {
        Imovel imovel = imovelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum imóvel encontrado com o ID especificado."));
        imovel.setApagado(true);
        imovelRepository.save(imovel);
    }

    public void deleteById(Long id) {
        imovelRepository.deleteById(id);
    }

    public Page<ImovelDTO> findByImobiliariaPaginado(Long imobiliariaId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("idImovel").descending());
        Page<Imovel> imoveisPage = imovelRepository.findByImobiliariaId(imobiliariaId, pageable);
        return imoveisPage.map(ImovelDTO::new);
    }

}
