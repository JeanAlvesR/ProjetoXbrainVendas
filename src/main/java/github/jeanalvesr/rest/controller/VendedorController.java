package github.jeanalvesr.rest.controller;

import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.service.VendedorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    private VendedorService service;

    public VendedorController(VendedorService service) {
        this.service = service;
    }

    @GetMapping
    public List<VendedorDTO> vendedorPorData(String dataIni, String dataFinal){
        return service.vendedorPorData(dataIni, dataFinal);
    }
}