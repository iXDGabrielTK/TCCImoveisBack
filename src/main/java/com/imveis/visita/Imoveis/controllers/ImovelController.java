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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final ImovelService imovelService;

    private final FotoImovelController fotoImovelController;

    public ImovelController(ImovelService imovelService, FotoImovelController fotoImovelController) {
        this.imovelService = imovelService;

        this.fotoImovelController = fotoImovelController;
    }

    @GetMapping
    public ResponseEntity<List<ImovelDTO>> getAllImoveis() {
        try {
            List<Imovel> imoveis = imovelService.findAll();
            List<ImovelDTO> imoveisDTO = imoveis.stream()
                    .map(ImovelDTO::new)
                    .toList();
            return ResponseEntity.ok(imoveisDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImoveisById(@PathVariable BigInteger id) {
        try {
            // Obter o imóvel pelo ID
            Optional<Imovel> imovel = imovelService.findById(id);

            if (imovel.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imovel não encontrada.");
            }else {
                return ResponseEntity.ok(imovel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Optional.empty());
        }
    }

    @PostMapping
    public ResponseEntity<Imovel> criarImovel(@RequestBody ImovelRequest imovelRequest) {
        try {
            Imovel imovel = new Imovel();
            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setHistoricoManutencao(imovelRequest.getHistoricoManutencao());
            imovel.setEnderecoImovel(imovelRequest.getEnderecoImovel());

            // Associar funcionário, se fornecido
            if (imovelRequest.getFuncionarioId() != null) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(imovelRequest.getFuncionarioId());
                imovel.setFuncionario(funcionario);
            }

            // Salva o imóvel
            imovel = imovelService.save(imovel);

            if (imovelRequest.getFotosImovel() != null && !imovelRequest.getFotosImovel().isEmpty()) {
                String urls = String.join(",", imovelRequest.getFotosImovel());
                fotoImovelController.createFotosImovel(urls, imovel.getIdImovel());
            }

            return new ResponseEntity<>(imovel, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imovel> updateImovel(@PathVariable BigInteger id, @RequestBody ImovelRequest imovelRequest) {
        try {
            Imovel imovel = imovelService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado"));

            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setHistoricoManutencao(imovelRequest.getHistoricoManutencao());
            imovel.setEnderecoImovel(imovelRequest.getEnderecoImovel());

            Imovel updatedImovel = imovelService.save(imovel);

            if (imovelRequest.getFotosImovel() != null && !imovelRequest.getFotosImovel().isEmpty()) {
                String urls = String.join(",", imovelRequest.getFotosImovel());
                fotoImovelController.createFotosImovel(urls, imovel.getIdImovel());
            }

            return ResponseEntity.ok(updatedImovel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarImovel(@PathVariable BigInteger id) {
        imovelService.cancelarImovel(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public void deleteImovel(@PathVariable BigInteger id) {
        imovelService.deleteById(id);
    }
}
