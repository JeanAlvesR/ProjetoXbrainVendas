package github.jeanalvesr.rest.controller;

import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.rest.dto.VendedorValorDTO;
import github.jeanalvesr.domain.service.VendedorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    private VendedorService service;

    public VendedorController(VendedorService service) {
        this.service = service;
    }

    @GetMapping
    public List<VendedorValorDTO> vendedorPorData(String dataIni, String dataFinal){
        return service.vendedorPorData(dataIni, dataFinal);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Integer saveVendedor(@RequestBody VendedorDTO dto){
        return service.saveVendedor(dto);
    }
}