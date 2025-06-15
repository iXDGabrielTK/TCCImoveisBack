package com.imveis.visita.Imoveis.controllers;

import com.imveis.visita.Imoveis.dtos.VistoriaDTO;
import com.imveis.visita.Imoveis.dtos.VistoriaRequest;
import com.imveis.visita.Imoveis.entities.Funcionario;
import com.imveis.visita.Imoveis.entities.Imovel;
import com.imveis.visita.Imoveis.entities.Vistoria;
import com.imveis.visita.Imoveis.service.FuncionarioService;
import com.imveis.visita.Imoveis.service.ImovelService;
import com.imveis.visita.Imoveis.service.VistoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vistorias")
public class VistoriaController {

    private final VistoriaService vistoriaService;
    private final ImovelService imovelService;
    private final FuncionarioService funcionarioService;

    @Autowired
    public VistoriaController(VistoriaService vistoriaService, ImovelService imovelService, FuncionarioService funcionarioService){
        this.vistoriaService = vistoriaService;
        this.imovelService = imovelService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public ResponseEntity<List<VistoriaDTO>> getAllVistorias() {
        List<Vistoria> vistorias = vistoriaService.findAll();
        List<VistoriaDTO> dtos = vistorias.stream().map(VistoriaDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
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

    @PostMapping
    public ResponseEntity<?> createVistoria(@RequestBody VistoriaRequest vistoriaRequest) {
        try {
            Imovel imovel = imovelService.findByEndereco(vistoriaRequest.getRua(), vistoriaRequest.getNumero(), vistoriaRequest.getBairro())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado para o endereço fornecido"));

            Funcionario funcionario = funcionarioService.findById(vistoriaRequest.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado para o ID fornecido"));

            Vistoria vistoria = new Vistoria();
            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());
            vistoria.setFuncionario(funcionario);
            vistoria.setImovel(imovel);

            Vistoria novaVistoria = vistoriaService.save(vistoria);

            return ResponseEntity.ok(new VistoriaDTO(novaVistoria));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar vistoria: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateVistoria(@PathVariable Long id, @RequestBody VistoriaRequest vistoriaRequest) {
        try {
            Vistoria vistoria = vistoriaService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vistoria não encontrada"));

            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());

            Vistoria updatedVistoria = vistoriaService.save(vistoria);
            return ResponseEntity.ok(new VistoriaDTO(updatedVistoria));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + e.getMessage());
        } catch (Exception e) {
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
