package github.jeanalvesr.service;

import github.jeanalvesr.rest.dto.VendedorDTO;

import java.util.List;

public interface VendedorService {
    public List<VendedorDTO> vendedorPorData(String dataInicial, String dataFinal);

}
