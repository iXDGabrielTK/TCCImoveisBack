package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.ImovelDTO;
import com.imveis.visita.Imoveis.dtos.ImovelRequest;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.repositories.CorretorRepository;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import com.imveis.visita.Imoveis.service.EnderecoService;
import com.imveis.visita.Imoveis.service.ImovelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;

@SuppressWarnings("CallToPrintStackTrace")
@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final ImovelService imovelService;

    private final FotoImovelController fotoImovelController;

    private final EnderecoService enderecoService;

    private final CorretorRepository corretorRepository;

    private final ImobiliariaRepository imobiliariaRepository;

    public ImovelController(ImovelService imovelService,
                            FotoImovelController fotoImovelController,
                            EnderecoService enderecoService,
                            CorretorRepository corretorRepository,
                            ImobiliariaRepository imobiliariaRepository) {
        this.imovelService = imovelService;
        this.fotoImovelController = fotoImovelController;
        this.enderecoService = enderecoService;
        this.corretorRepository = corretorRepository;
        this.imobiliariaRepository = imobiliariaRepository;
    }

    @GetMapping
    public ResponseEntity<List<ImovelDTO>> getAllImoveis() {
        try {
            List<ImovelDTO> imoveisDTO = imovelService.findAllDTOs();
            return ResponseEntity.ok(imoveisDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelDTO> getImovelById(@PathVariable BigInteger id) {
        return imovelService.findDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tem que testar esse endpoint quando o front estiver pronto
    @GetMapping("/disponiveis")
    public ResponseEntity<List<ImovelDTO>> getImoveisPorPoderDeCompra(@RequestParam double valorMax){
        List<ImovelDTO> disponiveis = imovelService.findDisponiveisPorValor(valorMax);
        return ResponseEntity.ok(disponiveis);
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

            imovel = imovelService.save(imovel);

            if (imovelRequest.getFotosImovel() != null && !imovelRequest.getFotosImovel().isEmpty()) {
                fotoImovelController.createFotosImovel(imovelRequest.getFotosImovel(), imovel.getIdImovel());
            }

            if (imovelRequest.getIdsCorretores() != null) {
                Set<Corretor> corretores = new HashSet<>(corretorRepository.findAllById(imovelRequest.getIdsCorretores()));
                imovel.setCorretores(corretores);
            }

            if (imovelRequest.getIdsImobiliarias() != null) {
                List<Imobiliaria> imobiliarias = imobiliariaRepository.findAllById(imovelRequest.getIdsImobiliarias());
                imovel.setImobiliarias(imobiliarias);
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

            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setHistoricoManutencao(imovelRequest.getHistoricoManutencao());

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
                    enderecoService.save(enderecoExistente);
                } else {
                    Endereco enderecoSalvo = enderecoService.save(novoEndereco);
                    imovel.setEnderecoImovel(enderecoSalvo);
                }
            }

            List<FotoImovel> novasFotos = imovelRequest.getFotosImovel().stream()
                    .filter(url -> url != null && !url.trim().isEmpty() && url.startsWith("http"))
                    .map(url -> FotoImovel.builder()
                            .imovel(imovel)
                            .urlFotoImovel(url)
                            .build())
                    .toList();

            imovel.getFotosImovel().clear();
            imovel.getFotosImovel().addAll(novasFotos);

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
