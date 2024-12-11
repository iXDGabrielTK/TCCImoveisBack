package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImovelDTO;
import com.imveis.visita.Imoveis.dtos.ImovelRequest;
import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.service.EnderecoService;
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

    private final EnderecoService enderecoService;


    public ImovelController(ImovelService imovelService, FotoImovelController fotoImovelController, EnderecoService enderecoService) {
        this.imovelService = imovelService;

        this.fotoImovelController = fotoImovelController;
        this.enderecoService = enderecoService;
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

            // Salva o imóvel
            imovel = imovelService.save(imovel);

            if (imovelRequest.getFotosImovel() != null && !imovelRequest.getFotosImovel().isEmpty()) {
                fotoImovelController.createFotosImovel(imovelRequest.getFotosImovel(), imovel.getIdImovel());
            }

            return new ResponseEntity<>(imovel, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateImovel(@PathVariable BigInteger id, @RequestBody ImovelRequest imovelRequest) {
        try {
            Optional<Imovel> imovelOptional = imovelService.findById(id);
            if (imovelOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imóvel não encontrado.");
            }

            Imovel imovel = imovelOptional.get();

            // Atualize os campos principais do imóvel
            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setHistoricoManutencao(imovelRequest.getHistoricoManutencao());

            // Atualize ou Reutilize o Endereço
            Endereco novoEndereco = imovelRequest.getEnderecoImovel();
            if (novoEndereco != null) {
                if (imovel.getEnderecoImovel() != null) {
                    // Atualiza o endereço existente
                    Endereco enderecoExistente = imovel.getEnderecoImovel();
                    enderecoExistente.setRua(novoEndereco.getRua());
                    enderecoExistente.setNumero(novoEndereco.getNumero());
                    enderecoExistente.setComplemento(novoEndereco.getComplemento());
                    enderecoExistente.setBairro(novoEndereco.getBairro());
                    enderecoExistente.setCidade(novoEndereco.getCidade());
                    enderecoExistente.setEstado(novoEndereco.getEstado());
                    enderecoExistente.setCep(novoEndereco.getCep());
                    enderecoService.save(enderecoExistente); // Persistir as alterações
                } else {
                    // Caso não exista um endereço associado, cria um novo
                    Endereco enderecoSalvo = enderecoService.save(novoEndereco);
                    imovel.setEnderecoImovel(enderecoSalvo);
                }
            }

            // Atualize a lista de fotos
            List<FotoImovel> novasFotos = imovelRequest.getFotosImovel().stream()
                    .filter(url -> url != null && !url.trim().isEmpty() && url.startsWith("http"))
                    .map(url -> FotoImovel.builder()
                            .imovel(imovel)
                            .urlFotoImovel(url)
                            .build())
                    .toList();

            imovel.getFotosImovel().clear(); // Remove as fotos existentes
            imovel.getFotosImovel().addAll(novasFotos); // Adiciona as novas fotos

            // Salvar as alterações no imóvel
            Imovel updatedImovel = imovelService.save(imovel);

            return ResponseEntity.ok(updatedImovel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o imóvel.");
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
