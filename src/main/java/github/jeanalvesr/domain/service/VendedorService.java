package github.jeanalvesr.domain.service;

import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.rest.dto.VendedorValorDTO;

import java.util.List;

public interface VendedorService {
    public List<VendedorValorDTO> vendedorPorData(String dataInicial, String dataFinal);

    public Integer saveVendedor(VendedorDTO dto);

}
