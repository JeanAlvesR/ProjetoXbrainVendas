package github.jeanalvesr.service.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.Vendas;
import github.jeanalvesr.domain.repository.Vendedores;
import github.jeanalvesr.exception.RegraNegocioException;
import github.jeanalvesr.rest.dto.VendaDTO;
import github.jeanalvesr.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor //constrói um construtor com todos os atributos obrigatórios, isto é, os finals
public class VendaServiceImpl implements VendaService {
    private final Vendas repository;
    private final Vendedores vendedores;

    @Override
    @Transactional //Garante que a transação seja atómica.
    public Venda salvar(VendaDTO dto) throws RegraNegocioException{
        if (Objects.isNull(dto.getId()) || Objects.isNull(dto.getData()) || Objects.isNull(dto.getValor()) || Objects.isNull(dto.getIdVendedor()) || Objects.isNull(dto.getNomeVendedor())) {
            throw new RegraNegocioException("Um dos atributos é nulo.");
        }
        if(repository.findById(dto.getId()).isPresent()){
            throw new RegraNegocioException("O ID informado da venda já existe no banco.");
        }

        Venda venda = new Venda();
        venda.setId(dto.getId());



        DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate data = LocalDate.parse(dto.getData(), formatar);
            venda.setData(data);
        } catch (DateTimeParseException e) {
            throw new RegraNegocioException("A data inválida: Não segue o padrão dd/MM/yyyy.");
        }

        venda.setValor(dto.getValor());

        Optional<Vendedor> aux = vendedores.findById(dto.getId());

        if(aux.isEmpty()){
            Vendedor vendedor = new Vendedor();
            vendedor.setId(dto.getIdVendedor());
            vendedor.setNome(dto.getNomeVendedor());
            vendedores.save(vendedor);
            venda.setVendedor(vendedor);
        }
        else{
            venda.setVendedor(aux.get());
        }

        repository.save(venda);

        return venda;
    }
}
