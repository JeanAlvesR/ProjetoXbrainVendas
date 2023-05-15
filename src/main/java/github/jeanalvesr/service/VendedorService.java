package github.jeanalvesr.service;

import java.util.List;

public interface VendedorService {
    public List<VendedorDTO> vendedorPorData(String dataInicial, String dataFinal);

}
