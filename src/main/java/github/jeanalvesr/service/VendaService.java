package github.jeanalvesr.service;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.rest.dto.VendaDTO;

public interface VendaService {

    void salvar(VendaDTO dto);

}
