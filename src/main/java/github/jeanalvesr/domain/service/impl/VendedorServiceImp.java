package github.jeanalvesr.domain.service.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.Vendedores;
import github.jeanalvesr.domain.service.VendedorService;
import github.jeanalvesr.exception.AtributoFaltanteException;
import github.jeanalvesr.exception.DataException;
import github.jeanalvesr.exception.VendedorExistenteException;
import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.rest.dto.VendedorValorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendedorServiceImp implements VendedorService {
    private final Vendedores vendedores;
    @Override
    public List<VendedorValorDTO> vendedorPorData(String dataInicio, String dataFinal) {
        try {
            if(Objects.isNull(dataInicio) || Objects.isNull(dataFinal)){
                throw new DataException("As datas não podem estar vazias");
            }
            DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate ini = LocalDate.parse(dataInicio, formatar);
            LocalDate fin = LocalDate.parse(dataFinal, formatar);

            if(fin.isBefore(ini)){
                throw new DataException("A data inicial não pode ser inferior a final. ");
            }

            long quantidadeDias = ChronoUnit.DAYS.between(ini, fin);

            List<Vendedor> listaVendedor = vendedores.buscarVendedoresComVendasEntreDatas(ini, fin);

            List<VendedorValorDTO> listaDTO = listaVendedor.stream().map(vendedor -> {
                        VendedorValorDTO vendedorValorDTO = new VendedorValorDTO();
                        vendedorValorDTO.setNome(vendedor.getNome());

                        //recupera as vendas, transforma em uma stream, cria uma lista de valores e utiliza o reduce para somar.
                        vendedorValorDTO.setTotalVendas(vendedor.getVendas().stream().map(Venda::getValor).reduce(BigDecimal.ZERO, BigDecimal::add));

                        BigDecimal resultadoMedia = vendedorValorDTO.getTotalVendas().divide(BigDecimal.valueOf(quantidadeDias), 2, RoundingMode.HALF_UP); //arredonda para 2 casas após a vírgula, utiliza o método de caso der 0,5, ele arredonda para cima.
                        vendedorValorDTO.setMediaDiariaVendas(resultadoMedia);
                        return vendedorValorDTO;
                    })
                    .collect(Collectors.toList());

            return listaDTO;
        } catch (DateTimeParseException e) {
            throw new DataException("A data é inválida: Não segue o padrão dd/MM/yyyy.");
        }

    }

    public Integer saveVendedor(VendedorDTO dto){
        if(Objects.isNull(dto.getNome())){
            throw new AtributoFaltanteException("O nome está vazio");
        } else if (dto.getNome().isEmpty()) {
            throw new AtributoFaltanteException("O nome está vazio");
        }

        Vendedor vendedor = new Vendedor(dto.getNome());

        return vendedores.save(vendedor).getId();
    }

}
