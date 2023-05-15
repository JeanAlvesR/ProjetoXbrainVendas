package github.jeanalvesr.service.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.Vendedores;
import github.jeanalvesr.exception.RegraNegocioException;
import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.service.VendedorService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendedorServiceImp implements VendedorService {
    private final Vendedores vendedores;
    @Override
    public List<VendedorDTO> vendedorPorData(String dataInicio, String dataFinal) {
        try {
            DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ini = LocalDate.parse(dataInicio, formatar);
            LocalDate fin = LocalDate.parse(dataFinal, formatar);

            if(fin.isBefore(ini)){
                throw new RegraNegocioException("A data inicial não pode ser inferior a final. ");
            }

            long quantidadeDias = ChronoUnit.DAYS.between(ini, fin);

            List<Vendedor> listaVendedor = vendedores.buscarVendedoresComVendasEntreDatas(ini, fin);

            List<VendedorDTO> listaDTO = listaVendedor.stream().map( vendedor -> {
                        VendedorDTO vendedorDTO = new VendedorDTO();
                        vendedorDTO.setNome(vendedor.getNome());

                        //recupera as vendas, transforma em uma stream, cria uma lista de valores e utiliza o reduce para somar.
                        vendedorDTO.setTotalVendas(vendedor.getVendas().stream().map(Venda::getValor).reduce(BigDecimal.ZERO, BigDecimal::add));

                        BigDecimal resultadoMedia = vendedorDTO.getTotalVendas().divide(BigDecimal.valueOf(quantidadeDias), 2, RoundingMode.HALF_UP); //arredonda para 2 casas após a vírgula, utiliza o método de caso der 0,5, ele arredonda para cima.
                        vendedorDTO.setMediaDiariaVendas(resultadoMedia);
                        return vendedorDTO;
                    })
                    .collect(Collectors.toList());

            return listaDTO;
        } catch (DateTimeParseException e) {
            throw new RegraNegocioException("A data é inválida: Não segue o padrão dd/MM/yyyy.");
        }

    }

}
