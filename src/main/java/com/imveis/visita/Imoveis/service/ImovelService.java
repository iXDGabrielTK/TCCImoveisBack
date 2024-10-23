package com.imveis.visita.Imoveis.service;


import com.imveis.visita.Imoveis.repositories.ImovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.imveis.visita.Imoveis.entities.Imovel;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImovelService {

    private final ImovelRepository imovelRepository;

    @Autowired
    public ImovelService(ImovelRepository imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    public List<Imovel> findAll() {
        return imovelRepository.findAll();
    }

    public Imovel save(@org.jetbrains.annotations.NotNull Imovel imovel) {
        if (imovel.getPrecoImovel() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo");
        }
        return imovelRepository.save(imovel);
    }
}
