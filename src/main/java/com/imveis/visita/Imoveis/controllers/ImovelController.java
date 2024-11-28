package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImovelDTO;
import com.imveis.visita.Imoveis.dtos.ImovelRequest;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.service.ImovelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @GetMapping
    public ResponseEntity<List<ImovelDTO>> getAllImoveis() {
        List<Imovel> imoveis = imovelService.findAll();
        List<ImovelDTO> imoveisDTO = imoveis.stream()
                .map(ImovelDTO::new)
                .toList();
        return ResponseEntity.ok(imoveisDTO);
    }

    @GetMapping("/{id}")
    public Optional<Imovel> getImoveisById(@PathVariable BigInteger id) {
        return imovelService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Imovel> criarImovel(@RequestBody ImovelRequest imovelRequest) {
        try {
            Imovel imovel = new Imovel();
            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setEnderecoImovel(imovelRequest.getEnderecoImovel());

            // ALTERAÇÃO: Associar funcionário se funcionarioId for fornecido
            if (imovelRequest.getFuncionarioId() != null) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(imovelRequest.getFuncionarioId());
                imovel.setFuncionario(funcionario);
            }

            imovel = imovelService.save(imovel); // Salva o imóvel no banco
            return new ResponseEntity<>(imovel, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteImovel(@PathVariable BigInteger id) {
        imovelService.deleteById(id);
    }
}
