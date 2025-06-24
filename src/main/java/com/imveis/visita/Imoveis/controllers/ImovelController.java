package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.FotoImovelDTO;
import com.imveis.visita.Imoveis.dtos.ImovelDTO;
import com.imveis.visita.Imoveis.dtos.ImovelRequest;
import com.imveis.visita.Imoveis.entities.Endereco;
import com.imveis.visita.Imoveis.entities.FotoImovel;
import com.imveis.visita.Imoveis.entities.Imobiliaria;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.infra.s3.S3StorageService;
import com.imveis.visita.Imoveis.repositories.ImobiliariaRepository;
import com.imveis.visita.Imoveis.service.EnderecoService;
import com.imveis.visita.Imoveis.service.ImovelService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("CallToPrintStackTrace")
@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final ImovelService imovelService;
    private final EnderecoService enderecoService;
    private final ImobiliariaRepository imobiliariaRepository;
    private final S3StorageService s3StorageService;

    public ImovelController(ImovelService imovelService,
                            EnderecoService enderecoService,
                            ImobiliariaRepository imobiliariaRepository,
                            S3StorageService s3StorageService) {
        this.imovelService = imovelService;
        this.enderecoService = enderecoService;
        this.imobiliariaRepository = imobiliariaRepository;
        this.s3StorageService = s3StorageService; // Atribuir
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelDTO> getImovelById(@PathVariable Long id) {
        return imovelService.findDTOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> getAllImoveis(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "8") int size) {
        try {
            return ResponseEntity.ok(imovelService.findAllPaginado(page, size));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar imóveis");
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Imovel> criarImovel(
            @RequestPart("dados") ImovelRequest imovelRequest,
            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos) {

        try {
            Imovel imovel = new Imovel();
            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setHistoricoManutencao(imovelRequest.getHistoricoManutencao());
            imovel.setEnderecoImovel(imovelRequest.getEnderecoImovel());

            if (imovelRequest.getImobiliariaId() != null) {
                Imobiliaria imobiliaria = imobiliariaRepository.findById(imovelRequest.getImobiliariaId())
                        .orElseThrow(() -> new IllegalArgumentException("Imobiliária não encontrada com o ID fornecido."));
                imovel.setImobiliaria(imobiliaria);
            }

            imovel = imovelService.save(imovel);

            processAndAddPhotos(imovel, fotos);

            imovel = imovelService.save(imovel);

            return new ResponseEntity<>(imovel, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<?> getImoveisPorPoderDeCompra(@RequestParam double valorMax,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "8") int size) {
        return ResponseEntity.ok(imovelService.findDisponiveisPorValorPaginado(valorMax, page, size));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<?> updateImovel(
            @PathVariable Long id,
            @RequestPart("dados") ImovelRequest imovelRequest,
            @RequestPart(value = "fotos", required = false) List<MultipartFile> novasFotos) {
        try {
            Optional<Imovel> imovelOptional = imovelService.findById(id);
            if (imovelOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imóvel não encontrado.");
            }

            Imovel imovel = imovelOptional.get();

            Imobiliaria imobiliaria = imovel.getImobiliaria();

            imovel.setTipoImovel(imovelRequest.getTipoImovel());
            imovel.setDescricaoImovel(imovelRequest.getDescricaoImovel());
            imovel.setStatusImovel(imovelRequest.getStatusImovel());
            imovel.setTamanhoImovel(imovelRequest.getTamanhoImovel());
            imovel.setPrecoImovel(imovelRequest.getPrecoImovel());
            imovel.setHistoricoManutencao(imovelRequest.getHistoricoManutencao());

            Endereco novoEndereco = imovelRequest.getEnderecoImovel();
            if (novoEndereco != null) {
                if (novoEndereco.getIdEndereco() != null) {
                    Endereco enderecoExistente = enderecoService.findById(novoEndereco.getIdEndereco())
                            .orElseThrow(() -> new IllegalArgumentException("Endereço existente não encontrado."));
                    enderecoExistente.setRua(novoEndereco.getRua());
                    enderecoExistente.setNumero(novoEndereco.getNumero());
                    enderecoExistente.setComplemento(novoEndereco.getComplemento());
                    enderecoExistente.setBairro(novoEndereco.getBairro());
                    enderecoExistente.setCidade(novoEndereco.getCidade());
                    enderecoExistente.setEstado(novoEndereco.getEstado());
                    enderecoExistente.setCep(novoEndereco.getCep());
                    enderecoService.save(enderecoExistente);
                    imovel.setEnderecoImovel(enderecoExistente);
                } else {
                    Endereco enderecoSalvo = enderecoService.save(novoEndereco);
                    imovel.setEnderecoImovel(enderecoSalvo);
                }
            }

            // Lógica de remoção de fotos existentes
            Set<Long> idsFotosParaManter = imovelRequest.getFotosImovel().stream()
                    .map(FotoImovelDTO::getId)
                    .filter(java.util.Objects::nonNull) // Garante que IDs nulos não causem problemas (já estava!)
                    .collect(Collectors.toSet());

// Remova as fotos da coleção do imóvel que NÃO estão na lista 'idsFotosParaManter'
// O orphanRemoval=true na entidade FotoImovel vai lidar com a exclusão no banco
// A condição 'foto.getIdFotosImovel() != null' é crucial aqui para evitar NPE em fotos sem ID
            imovel.getFotosImovel().removeIf(foto -> foto.getIdFotosImovel() != null && !idsFotosParaManter.contains(foto.getIdFotosImovel()));

            processAndAddPhotos(imovel, novasFotos);

            imovel.setImobiliaria(imobiliaria);

            Imovel updatedImovel = imovelService.save(imovel);

            return ResponseEntity.ok(new ImovelDTO(updatedImovel));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar o imóvel.");
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarImovel(@PathVariable Long id) {
        imovelService.cancelarImovel(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public void deleteImovel(@PathVariable Long id) {
        imovelService.deleteById(id);
    }

    /**
     * Lida com o upload de novas fotos para o S3 e as adiciona à coleção do Imovel.
     * Assume que o imóvel já tem um ID.
     *
     * @param imovel O objeto Imovel ao qual as fotos serão associadas.
     * @param novasFotos A lista de MultipartFiles a serem carregados.
     * @throws IOException Se houver erro de I/O durante o upload.
     * @throws S3Exception Se houver erro na interação com o S3.
     */
    private void processAndAddPhotos(Imovel imovel, List<MultipartFile> novasFotos) throws IOException, S3Exception {
        if (novasFotos != null && !novasFotos.isEmpty()) {
            String folderPath = "imoveis-fotos/" + imovel.getIdImovel();
            List<String> urlsNovasFotos = new java.util.ArrayList<>();

            for (MultipartFile file : novasFotos) {
                String url = s3StorageService.uploadFile(file, folderPath);
                urlsNovasFotos.add(url);
            }

            urlsNovasFotos.forEach(url -> imovel.getFotosImovel().add(
                    FotoImovel.builder().imovel(imovel).urlFotoImovel(url).build()));
        }
    }

    @GetMapping("/por-imobiliaria/{idImobiliaria}")
    public ResponseEntity<Page<ImovelDTO>> getImoveisByImobiliaria(
            @PathVariable Long idImobiliaria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        try {
            Page<ImovelDTO> imoveis = imovelService.findByImobiliariaPaginado(idImobiliaria, page, size);
            return ResponseEntity.ok(imoveis);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}