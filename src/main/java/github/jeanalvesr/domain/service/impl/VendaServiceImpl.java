package github.jeanalvesr.domain.service.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.Vendas;
import github.jeanalvesr.domain.repository.Vendedores;
import github.jeanalvesr.exception.AtributoFaltanteException;
import github.jeanalvesr.exception.DataException;
import github.jeanalvesr.exception.VendedorNaoExistenteException;
import github.jeanalvesr.rest.dto.VendaDTO;
import github.jeanalvesr.domain.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//constrói um construtor com todos os atributos obrigatórios, isto é, os finals. No construtor, o Spring entende que precisa injetá-los.
public class VendaServiceImpl implements VendaService {
    private final Vendas repository;
    private final Vendedores vendedores;

    @Override
    @Transactional //Garante que a transação seja atómica.
    public VendaDTO salvar(VendaDTO dto) {
        if (Objects.isNull(dto.getData()) || Objects.isNull(dto.getValor()) || Objects.isNull(dto.getIdVendedor())) {
            throw new AtributoFaltanteException("Um dos atributos é nulo.");
        }

        Venda venda = new Venda();

        DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate data = LocalDate.parse(dto.getData(), formatar);

            if (data.isAfter(LocalDate.now())) {
                throw new DataException("A venda não pode ser no futuro. Respeitar a data de hoje como limite.");
            }

            venda.setData(data);
        } catch (DateTimeParseException e) {
            throw new DataException("A data inválida: Não segue o padrão dd/MM/yyyy.");
        }

        venda.setValor(dto.getValor());

        Optional<Vendedor> aux = vendedores.findById(dto.getId());

        if (aux.isEmpty()) {
            throw new VendedorNaoExistenteException("O ID " + dto.getIdVendedor() + " do vendedor não existe no banco");
        }

        venda.setVendedor(aux.get());

        venda = repository.save(venda);
        dto.setId(venda.getId());
        dto.setNomeVendedor(venda.getVendedor().getNome());
        return dto;
    }

}
