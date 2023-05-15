package github.jeanalvesr.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendaDTO {
    private Integer id;
    private String data;
    private BigDecimal valor;
    private Integer idVendedor;
    private String nomeVendedor;
}
