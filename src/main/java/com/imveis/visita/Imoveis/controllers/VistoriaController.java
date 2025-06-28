package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.AmbienteVistoriaRequest;
import com.imveis.visita.Imoveis.dtos.VistoriaDTO;
import com.imveis.visita.Imoveis.dtos.VistoriaRequest;
import com.imveis.visita.Imoveis.entities.*;
import com.imveis.visita.Imoveis.infra.s3.S3Service;
import com.imveis.visita.Imoveis.security.UserDetailsImpl;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.ImovelService;
import com.imveis.visita.Imoveis.service.UsuarioService;
import com.imveis.visita.Imoveis.service.VistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
    private final UsuarioService usuarioService;

    @Autowired
    public VistoriaController(VistoriaService vistoriaService, ImovelService imovelService, FuncionarioService funcionarioService, S3Service s3Service, UsuarioService usuarioService){
        this.vistoriaService = vistoriaService;
        this.imovelService = imovelService;
        this.funcionarioService = funcionarioService;
        this.s3Service = s3Service;
        this.usuarioService = usuarioService;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createVistoria(@RequestBody VistoriaRequest vistoriaRequest) {
        try {
            Imovel imovel = imovelService.findById(vistoriaRequest.getImovelId())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado com o ID fornecido"));

            Usuario creatingUser = usuarioService.findById(vistoriaRequest.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para o ID fornecido"));

            Funcionario assignedFuncionario = null;

            if (creatingUser instanceof Funcionario) {
                assignedFuncionario = (Funcionario) creatingUser;
            } else if (creatingUser instanceof Corretor) {
                List<Funcionario> allFuncionarios = funcionarioService.findAll();
                if (!allFuncionarios.isEmpty()) {
                    assignedFuncionario = allFuncionarios.get(0);
                } else {
                    throw new IllegalArgumentException("Nenhum funcionário disponível para associar à vistoria.");
                }
            } else {
                throw new IllegalArgumentException("Apenas funcionários ou corretores podem criar vistorias.");
            }

            if (assignedFuncionario == null) {
                throw new IllegalStateException("Falha ao atribuir funcionário à vistoria.");
            }

            Vistoria vistoria = new Vistoria();
            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());
            vistoria.setFuncionario(assignedFuncionario);
            vistoria.setImovel(imovel);

            if (vistoriaRequest.getAmbientes() != null && !vistoriaRequest.getAmbientes().isEmpty()) {
                Set<AmbienteVistoria> ambientes = vistoriaRequest.getAmbientes().stream().map(ar ->
                        AmbienteVistoria.builder()
                                .nome(ar.getNome())
                                .descricao(ar.getDescricao())
                                .vistoria(vistoria)
                                .build()
                ).collect(Collectors.toSet());
                vistoria.setAmbientes(ambientes);
            }

            Vistoria novaVistoria = vistoriaService.save(vistoria);

            return ResponseEntity.ok(new VistoriaDTO(novaVistoria));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vistoria: " + e.getMessage());
        }
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createVistoriaComFotos(
            @RequestPart("dados") VistoriaRequest vistoriaRequest,
            @RequestPart(value = "fotos", required = false) MultipartFile[] arquivos) {
        try {
            Imovel imovel = imovelService.findById(vistoriaRequest.getImovelId())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado"));

            Usuario creatingUser = usuarioService.findById(vistoriaRequest.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para o ID fornecido"));

            Funcionario assignedFuncionario = null;

            if (creatingUser instanceof Funcionario) {
                assignedFuncionario = (Funcionario) creatingUser;
            } else if (creatingUser instanceof Corretor) {
                List<Funcionario> allFuncionarios = funcionarioService.findAll();
                if (!allFuncionarios.isEmpty()) {
                    assignedFuncionario = allFuncionarios.get(0);
                } else {
                    throw new IllegalArgumentException("Nenhum funcionário disponível para associar à vistoria.");
                }
            } else {
                throw new IllegalArgumentException("Apenas funcionários ou corretores podem criar vistorias.");
            }

            if (assignedFuncionario == null) {
                throw new IllegalStateException("Falha ao atribuir funcionário à vistoria.");
            }

            Vistoria vistoria = new Vistoria();
            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());
            vistoria.setFuncionario(assignedFuncionario);
            vistoria.setImovel(imovel);

            Set<AmbienteVistoria> ambientesSalvos = new java.util.HashSet<>();

            if (vistoriaRequest.getAmbientes() != null) {
                int ambienteIndex = 0;
                for (AmbienteVistoriaRequest ambienteRequest : vistoriaRequest.getAmbientes()) {
                    AmbienteVistoria ambiente = new AmbienteVistoria();
                    ambiente.setNome(ambienteRequest.getNome());
                    ambiente.setDescricao(ambienteRequest.getDescricao());
                    ambiente.setVistoria(vistoria);

                    Set<FotoVistoria> fotos = new java.util.HashSet<>();
                    if (arquivos != null) {
                        for (MultipartFile file : arquivos) {
                            String expectedPrefix = String.format("amb_%d_", ambienteIndex);
                            if (file.getOriginalFilename() != null && file.getOriginalFilename().startsWith(expectedPrefix)) {
                                String url = s3Service.uploadFile("vistoria-fotos/", file);
                                FotoVistoria foto = new FotoVistoria();
                                foto.setUrlFotoVistoria(url);
                                foto.setAmbiente(ambiente);
                                fotos.add(foto);
                            }
                        }
                    }
                    ambiente.setFotos(fotos);
                    ambientesSalvos.add(ambiente);
                    ambienteIndex++;
                }
            }

            vistoria.setAmbientes(ambientesSalvos);
            Vistoria nova = vistoriaService.save(vistoria);

            return ResponseEntity.ok(new VistoriaDTO(nova));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vistoria: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
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

            Set<AmbienteVistoria> ambientesProcessados = new HashSet<>();

            if (vistoriaRequest.getAmbientes() != null) {
                int ambienteIndex = 0;
                for (AmbienteVistoriaRequest ambienteRequest : vistoriaRequest.getAmbientes()) {
                    AmbienteVistoria ambiente;
                    Set<FotoVistoria> fotosDoAmbienteManaged;

                    if (ambienteRequest.getId() != null) {
                        ambiente = vistoria.getAmbientes().stream()
                                .filter(a -> a.getId().equals(ambienteRequest.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Ambiente existente não encontrado com o ID: " + ambienteRequest.getId()));

                        fotosDoAmbienteManaged = ambiente.getFotos();
                        if (fotosDoAmbienteManaged == null) {
                            fotosDoAmbienteManaged = new HashSet<>();
                            ambiente.setFotos(fotosDoAmbienteManaged);
                        }
                        fotosDoAmbienteManaged.clear();

                    } else {
                        // É um novo ambiente
                        ambiente = AmbienteVistoria.builder()
                                .nome(ambienteRequest.getNome())
                                .descricao(ambienteRequest.getDescricao())
                                .vistoria(vistoria)
                                .build();
                        fotosDoAmbienteManaged = new HashSet<>();
                        ambiente.setFotos(fotosDoAmbienteManaged);
                    }

                    ambiente.setNome(ambienteRequest.getNome());
                    ambiente.setDescricao(ambienteRequest.getDescricao());
                    ambiente.setVistoria(vistoria); // Garante a associação

                    // Adiciona fotos existentes que o frontend indicou para manter
                    if (ambienteRequest.getFotosExistentes() != null) {
                        for (com.imveis.visita.Imoveis.dtos.FotoVistoriaDTO fotoExistenteDTO : ambienteRequest.getFotosExistentes()) {
                            FotoVistoria fotoExistente = new FotoVistoria();
                            fotoExistente.setId(fotoExistenteDTO.getId());
                            fotoExistente.setUrlFotoVistoria(fotoExistenteDTO.getUrlFotoVistoria());
                            fotoExistente.setAmbiente(ambiente);
                            fotosDoAmbienteManaged.add(fotoExistente);
                        }
                    }

                    // Adiciona novas fotos carregadas (MultipartFile)
                    if (arquivos != null) {
                        for (MultipartFile file : arquivos) {
                            String expectedPrefix = String.format("amb_%d_", ambienteIndex);
                            if (file.getOriginalFilename() != null && file.getOriginalFilename().startsWith(expectedPrefix)) {
                                String url = s3Service.uploadFile("vistoria-fotos/", file);
                                FotoVistoria novaFoto = new FotoVistoria();
                                novaFoto.setUrlFotoVistoria(url);
                                novaFoto.setAmbiente(ambiente);
                                fotosDoAmbienteManaged.add(novaFoto);
                            }
                        }
                    }

                    ambientesProcessados.add(ambiente);
                    ambienteIndex++;
                }
            }

            vistoria.getAmbientes().clear();
            vistoria.getAmbientes().addAll(ambientesProcessados);

            Vistoria savedVistoria = vistoriaService.save(vistoria);

            Vistoria updatedAndInitializedVistoria = vistoriaService.findById(savedVistoria.getIdVistoria())
                    .orElseThrow(() -> new IllegalStateException("Vistoria atualizada não encontrada após a gravação."));

            return ResponseEntity.ok(new VistoriaDTO(updatedAndInitializedVistoria));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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