package github.jeanalvesr.service.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.Vendas;
import github.jeanalvesr.domain.repository.Vendedores;
import github.jeanalvesr.domain.service.impl.VendaServiceImpl;
import github.jeanalvesr.exception.DataException;
import github.jeanalvesr.exception.VendedorNaoExistenteException;
import github.jeanalvesr.rest.dto.VendaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
class VendaServiceImplTest {

    public static final BigDecimal VALOR = BigDecimal.valueOf(100);
    private VendaServiceImpl vendaService;

    @Mock
    private Vendas vendaRepository;

    @Mock
    private Vendedores vendedores;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        vendaService = new VendaServiceImpl(vendaRepository, vendedores);
    }

    @Test
    public void testSalvarVendaComSucesso() {
        // Dados de teste
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setId(1);
        vendaDTO.setData("01/01/2023");
        vendaDTO.setValor(VALOR);
        vendaDTO.setIdVendedor(1);

        Venda vendaSalva = new Venda();
        vendaSalva.setId(1);
        vendaSalva.setData(LocalDate.of(2023, 1, 1));
        vendaSalva.setValor(VALOR);
        Vendedor vendedor = new Vendedor();
        vendedor.setId(1);
        vendedor.setNome("Nome do Vendedor");
        vendaSalva.setVendedor(vendedor);

        // Mocks e comportamentos esperados
        when(vendedores.findById(1)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaSalva);

        // Executar o método a ser testado
        VendaDTO resultDTO = vendaService.salvar(vendaDTO);

        // Verificar os resultados
        Assertions.assertNotNull(resultDTO.getId());
        Assertions.assertEquals("Nome do Vendedor", resultDTO.getNomeVendedor());

        verify(vendedores, times(1)).findById(1);
        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    public void testSalvarVendaComIdVendedorInexistente() {
        // Dados de teste
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setId(1);
        vendaDTO.setData("01/01/2023");
        vendaDTO.setValor(VALOR);
        vendaDTO.setIdVendedor(2);

        // Mocks e comportamentos esperados
        when(vendedores.findById(2)).thenReturn(Optional.empty());

        // Executar o método a ser testado e verificar a exceção
        VendedorNaoExistenteException exception = Assertions.assertThrows(VendedorNaoExistenteException.class, () -> vendaService.salvar(vendaDTO));

        Assertions.assertEquals("O ID 2 do vendedor não existe no banco", exception.getMessage());

        verify(vendedores, times(0)).findById(2);
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    public void testSalvarVendaComDataNoFuturo() {
        // Dados de teste
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setId(1);
        vendaDTO.setData("01/01/2024"); // Data no futuro
        vendaDTO.setValor(VALOR);
        vendaDTO.setIdVendedor(1);

        // Executar o método a ser testado e verificar a exceção
        DataException exception = Assertions.assertThrows(DataException.class, () -> vendaService.salvar(vendaDTO));

        Assertions.assertEquals("A venda não pode ser no futuro. Respeitar a data de hoje como limite.", exception.getMessage());
    }

    @Test
    public void testSalvarVendaComDataInvalida() {
        // Dados de teste
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setId(1);
        vendaDTO.setData("2023/01/01"); // Data com formato inválido
        vendaDTO.setValor(VALOR);
        vendaDTO.setIdVendedor(1);

        // Mocks e comportamentos esperados
        when(vendedores.findById(1)).thenReturn(Optional.empty());

        // Executar o método a ser testado e verificar a exceção
        DataException exception = Assertions.assertThrows(DataException.class, () -> vendaService.salvar(vendaDTO));

        Assertions.assertEquals("A data inválida: Não segue o padrão dd/MM/yyyy.", exception.getMessage());
    }
}