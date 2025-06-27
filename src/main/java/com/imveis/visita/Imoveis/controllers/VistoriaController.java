package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.AmbienteVistoriaRequest;
import com.imveis.visita.Imoveis.dtos.VistoriaDTO;
import com.imveis.visita.Imoveis.dtos.VistoriaRequest;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.infra.s3.S3Service;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.ImovelService;
import com.imveis.visita.Imoveis.service.VistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vistorias")
public class VistoriaController {

    private final VistoriaService vistoriaService;
    private final ImovelService imovelService;
    private final FuncionarioService funcionarioService;
    private final S3Service s3Service;

    @Autowired
    public VistoriaController(VistoriaService vistoriaService, ImovelService imovelService, FuncionarioService funcionarioService, S3Service s3Service){
        this.vistoriaService = vistoriaService;
        this.imovelService = imovelService;
        this.funcionarioService = funcionarioService;
        this.s3Service = s3Service;
    }

    @GetMapping
    public ResponseEntity<List<VistoriaDTO>> getAllVistorias() {
        List<Vistoria> vistorias = vistoriaService.findAll();
        List<VistoriaDTO> dtos = vistorias.stream().map(VistoriaDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/por-corretor")
    public ResponseEntity<List<VistoriaDTO>> getVistoriasPorCorretorAfiliado(
            @AuthenticationPrincipal UserDetailsImpl usuario
    ) {
        try {
            List<Vistoria> vistorias = vistoriaService.findAllByCorretorAfiliado(usuario.getId());
            List<VistoriaDTO> dtos = vistorias.stream().map(VistoriaDTO::new).toList();
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<?> getVistoriaById(@PathVariable Long id) {
        try {
            Optional<Vistoria> vistoria = vistoriaService.findById(id);
            return vistoria.map(v -> ResponseEntity.ok(new VistoriaDTO(v)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar vistoria: " + e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // <-- ADICIONADO anteriormente
    public ResponseEntity<?> createVistoria(@RequestBody VistoriaRequest vistoriaRequest) {
        try {
            Imovel imovel = imovelService.findById(vistoriaRequest.getImovelId())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado com o ID fornecido"));

            Funcionario funcionario = funcionarioService.findById(vistoriaRequest.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado para o ID fornecido"));

            Vistoria vistoria = new Vistoria();
            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());
            vistoria.setFuncionario(funcionario);
            vistoria.setImovel(imovel);

            if (vistoriaRequest.getAmbientes() != null && !vistoriaRequest.getAmbientes().isEmpty()) {
                // MUDANÇA: Altera List para Set e .toList() para .collect(Collectors.toSet())
                Set<AmbienteVistoria> ambientes = vistoriaRequest.getAmbientes().stream().map(ar ->
                        AmbienteVistoria.builder()
                                .nome(ar.getNome())
                                .descricao(ar.getDescricao())
                                .vistoria(vistoria)
                                .build()
                ).collect(Collectors.toSet()); // CORREÇÃO AQUI

                vistoria.setAmbientes(ambientes); // Agora espera um Set
            }

            Vistoria novaVistoria = vistoriaService.save(vistoria);

            return ResponseEntity.ok(new VistoriaDTO(novaVistoria));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vistoria: " + e.getMessage());
        }
    }

    // VistoriaController.java (trecho atualizado para upload)

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // <-- ALTERADO anteriormente
    public ResponseEntity<?> createVistoriaComFotos(
            @RequestPart("dados") VistoriaRequest vistoriaRequest,
            @RequestPart(value = "fotos", required = false) MultipartFile[] arquivos) {
        try {
            Imovel imovel = imovelService.findById(vistoriaRequest.getImovelId())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado"));

            Funcionario funcionario = funcionarioService.findById(vistoriaRequest.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

            Vistoria vistoria = new Vistoria();
            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());
            vistoria.setFuncionario(funcionario);
            vistoria.setImovel(imovel);

            // MUDANÇA: Altera ArrayList para HashSet para ambientesSalvos
            Set<AmbienteVistoria> ambientesSalvos = new java.util.HashSet<>();

            if (vistoriaRequest.getAmbientes() != null) {
                int ambienteIndex = 0;
                for (AmbienteVistoriaRequest ambienteRequest : vistoriaRequest.getAmbientes()) {
                    AmbienteVistoria ambiente = new AmbienteVistoria();
                    ambiente.setNome(ambienteRequest.getNome());
                    ambiente.setDescricao(ambienteRequest.getDescricao());
                    ambiente.setVistoria(vistoria);

                    // Fotos desse ambiente - MUDANÇA: Altera ArrayList para HashSet
                    Set<FotoVistoria> fotos = new java.util.HashSet<>();
                    if (arquivos != null) {
                        int fotoIndex = 0;
                        for (MultipartFile file : arquivos) {
                            String expectedPrefix = String.format("amb_%d_", ambienteIndex);
                            if (file.getOriginalFilename() != null && file.getOriginalFilename().startsWith(expectedPrefix)) {
                                String url = s3Service.uploadFile("vistoria-fotos/", file);
                                FotoVistoria foto = new FotoVistoria();
                                foto.setUrlFotoVistoria(url);
                                foto.setAmbiente(ambiente);
                                fotos.add(foto);
                            }
                            fotoIndex++;
                        }
                    }
                    ambiente.setFotos(fotos); // Agora espera um Set
                    ambientesSalvos.add(ambiente);
                    ambienteIndex++;
                }
            }

            vistoria.setAmbientes(ambientesSalvos); // Agora espera um Set
            Vistoria nova = vistoriaService.save(vistoria);

            return ResponseEntity.ok(new VistoriaDTO(nova));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vistoria: " + e.getMessage());
        }
    }




    @PutMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateVistoriaComFotos(
            @PathVariable Long id,
            @RequestPart("dados") VistoriaRequest vistoriaRequest,
            @RequestPart(value = "fotos", required = false) MultipartFile[] arquivos) {
        try {
            Vistoria vistoria = vistoriaService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vistoria não encontrada com o ID: " + id));

            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());

            Set<AmbienteVistoria> ambientesAtualizados = new HashSet<>();
            if (vistoriaRequest.getAmbientes() != null) {
                int ambienteIndex = 0;
                for (AmbienteVistoriaRequest ambienteRequest : vistoriaRequest.getAmbientes()) {
                    AmbienteVistoria ambiente;
                    if (ambienteRequest.getId() != null) {
                        // Ambiente existente - busque-o ou crie um novo para evitar problemas de anexo
                        ambiente = vistoria.getAmbientes().stream()
                                .filter(a -> a.getId().equals(ambienteRequest.getId()))
                                .findFirst()
                                .orElseGet(() -> new AmbienteVistoria()); // Ou throw exception se não encontrar
                        ambiente.setNome(ambienteRequest.getNome());
                        ambiente.setDescricao(ambienteRequest.getDescricao());
                        ambiente.setVistoria(vistoria); // Garante a relação
                    } else {
                        // Novo ambiente
                        ambiente = AmbienteVistoria.builder()
                                .nome(ambienteRequest.getNome())
                                .descricao(ambienteRequest.getDescricao())
                                .vistoria(vistoria)
                                .build();
                    }

                    Set<FotoVistoria> fotosDoAmbiente = new HashSet<>();

                    if (ambienteRequest.getFotosExistentes() != null) {
                        for (com.imveis.visita.Imoveis.dtos.FotoVistoriaDTO fotoExistenteDTO : ambienteRequest.getFotosExistentes()) {
                            // Crie uma entidade FotoVistoria a partir do DTO da foto existente
                            // e associe ao ambiente
                            FotoVistoria fotoExistente = new FotoVistoria();
                            fotoExistente.setId(fotoExistenteDTO.getId()); // Mantenha o ID existente
                            fotoExistente.setUrlFotoVistoria(fotoExistenteDTO.getUrlFotoVistoria());
                            fotoExistente.setAmbiente(ambiente);
                            fotosDoAmbiente.add(fotoExistente);
                        }
                    }

                    if (arquivos != null) {
                        for (MultipartFile file : arquivos) {
                            String expectedPrefix = String.format("amb_%d_", ambienteIndex);
                            if (file.getOriginalFilename() != null && file.getOriginalFilename().startsWith(expectedPrefix)) {
                                String url = s3Service.uploadFile("vistoria-fotos/", file);
                                FotoVistoria novaFoto = new FotoVistoria();
                                novaFoto.setUrlFotoVistoria(url);
                                novaFoto.setAmbiente(ambiente);
                                fotosDoAmbiente.add(novaFoto);
                            }
                        }
                    }
                    ambiente.setFotos(fotosDoAmbiente);
                    ambientesAtualizados.add(ambiente);
                    ambienteIndex++;
                }
            }

            vistoria.setAmbientes(ambientesAtualizados); // Sobrescreve a coleção de ambientes

            Vistoria updatedVistoria = vistoriaService.save(vistoria); // Salva a vistoria com as mudanças

            return ResponseEntity.ok(new VistoriaDTO(updatedVistoria)); // Retorna o DTO atualizado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Para depuração
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar vistoria: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarVistoria(@PathVariable Long id) {
        vistoriaService.cancelarVistoria(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public void deleteVistoria(@PathVariable Long id) {
        vistoriaService.deleteById(id);
    }

}