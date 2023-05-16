package github.jeanalvesr.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendedorValorDTO {
    private String nome;
    private BigDecimal totalVendas;
    private BigDecimal mediaDiariaVendas;
}
