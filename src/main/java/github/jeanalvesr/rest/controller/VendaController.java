package github.jeanalvesr.rest.controller;


import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.rest.dto.VendaDTO;
import github.jeanalvesr.service.VendaService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void save(@RequestBody VendaDTO dto) {
        service.salvar(dto);
    }

}