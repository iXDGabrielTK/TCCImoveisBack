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

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vistorias")
public class VistoriaController {

    private final VistoriaService vistoriaService;
    private final ImovelService imovelService;
    private final FuncionarioService funcionarioService;
    //private final FotoVistoriaController fotoVistoriaController;

    @Autowired
    public VistoriaController(VistoriaService vistoriaService, ImovelService imovelService, FuncionarioService funcionarioService){
        this.vistoriaService = vistoriaService;
        this.imovelService = imovelService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping
    public List<Vistoria> getAllVistorias(){
        return vistoriaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVistoriaById(@PathVariable BigInteger id) {
        try {
            Optional<Vistoria> vistoria = vistoriaService.findById(id);
            if (vistoria.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vistoria não encontrada.");
            }
            return ResponseEntity.ok(vistoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao buscar vistoria: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createVistoria(@RequestBody VistoriaRequest vistoriaRequest) {
        try {
            // Buscar o imóvel pela rua, número e bairro
            Imovel imovel = imovelService.findByEndereco(vistoriaRequest.getRua(), vistoriaRequest.getNumero(), vistoriaRequest.getBairro())
                    .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado para o endereço fornecido"));

            // Buscar o funcionário pelo ID
            Funcionario funcionario = funcionarioService.findById(vistoriaRequest.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado para o ID fornecido"));

            // Criar a entidade de Vistoria
            Vistoria vistoria = new Vistoria();
            vistoria.setTipoVistoria(vistoriaRequest.getTipoVistoria());
            vistoria.setLaudoVistoria(vistoriaRequest.getLaudoVistoria());
            vistoria.setDataVistoria(vistoriaRequest.getDataVistoria());
            vistoria.setFuncionario(funcionario); // Associar o funcionário à vistoria
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
    public ResponseEntity<?> updateVistoria(@PathVariable BigInteger id, @RequestBody VistoriaRequest vistoriaRequest) {
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
    public ResponseEntity<Void> cancelarVistoria(@PathVariable BigInteger id) {
        vistoriaService.cancelarVistoria(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public void deleteVistoria(@PathVariable BigInteger id){
        vistoriaService.deleteById(id);
    }

}
