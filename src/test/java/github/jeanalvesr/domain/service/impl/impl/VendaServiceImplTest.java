package github.jeanalvesr.domain.service.impl.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.VendaRepository;
import github.jeanalvesr.domain.repository.VendedorRepository;
import github.jeanalvesr.domain.service.impl.VendaServiceImpl;
import github.jeanalvesr.exception.AtributoFaltanteException;
import github.jeanalvesr.exception.DataException;
import github.jeanalvesr.exception.VendedorNaoExistenteException;
import github.jeanalvesr.rest.dto.VendaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
class VendaServiceImplTest {

    public static final BigDecimal VALOR = BigDecimal.valueOf(100);

    @InjectMocks
    private VendaServiceImpl vendaService;

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSalvarVendaComSucesso() {
        // Dados de teste
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setId(1);
        vendaDTO.setData("01/01/2023");
        vendaDTO.setValor(VALOR);
        vendaDTO.setIdVendedor(1);
        vendaDTO.setNomeVendedor("Jean Alves");

        Venda vendaSalva = new Venda();
        vendaSalva.setId(1);
        vendaSalva.setData(LocalDate.of(2023, 1, 1));
        vendaSalva.setValor(VALOR);
        Vendedor vendedor = new Vendedor("Jean Alves");
        vendaSalva.setVendedor(vendedor);

        // Mocks e comportamentos esperados
        when(vendedorRepository.findById(1)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaSalva);

        // Executar o método a ser testado
        VendaDTO resultDTO = vendaService.salvar(vendaDTO);

        // Verificar os resultados
        Assertions.assertNotNull(resultDTO.getId());
        Assertions.assertEquals("Jean Alves", resultDTO.getNomeVendedor());

        verify(vendedorRepository, times(1)).findById(1);
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
        when(vendedorRepository.findById(2)).thenReturn(Optional.empty());

        // Executar o método a ser testado e verificar a exceção
        VendedorNaoExistenteException exception = Assertions.assertThrows(VendedorNaoExistenteException.class, () -> vendaService.salvar(vendaDTO));

        Assertions.assertEquals("O ID 2 do vendedor não existe no banco", exception.getMessage());

        verify(vendedorRepository, times(1)).findById(2);
        verify(vendaRepository, never()).save(any(Venda.class));
    }


    @Test
    public void testSalvarValorDaCompraNulo() {
        VendaDTO dto = new VendaDTO();

        dto.setId(1);
        dto.setData("15/05/2023");
        dto.setValor(null);
        dto.setIdVendedor(1);

        assertThrows(AtributoFaltanteException.class, () -> vendaService.salvar(dto));
    }

    @Test
    public void testSalvarIdVendedorNulo() {
        VendaDTO dto = new VendaDTO();

        dto.setId(1);
        dto.setData("15/05/2023");
        dto.setValor(VALOR);
        dto.setIdVendedor(null);

        assertThrows(AtributoFaltanteException.class, () -> vendaService.salvar(dto));
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
        DataException exception = assertThrows(DataException.class, () -> vendaService.salvar(vendaDTO));

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
        when(vendedorRepository.findById(1)).thenReturn(Optional.empty());

        // Executar o método a ser testado e verificar a exceção
        DataException exception = assertThrows(DataException.class, () -> vendaService.salvar(vendaDTO));

        Assertions.assertEquals("A data inválida: Não segue o padrão dd/MM/yyyy.", exception.getMessage());
    }
}