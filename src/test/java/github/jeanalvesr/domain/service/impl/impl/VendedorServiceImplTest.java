package github.jeanalvesr.domain.service.impl.impl;

import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.VendaRepository;
import github.jeanalvesr.domain.repository.VendedorRepository;
import github.jeanalvesr.domain.service.impl.VendedorServiceImpl;
import github.jeanalvesr.exception.AtributoFaltanteException;
import github.jeanalvesr.exception.DataException;
import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.rest.dto.VendedorValorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VendedorServiceImplTest {

    @InjectMocks
    private VendedorServiceImpl vendedorService;

    @Mock
    private VendedorRepository vendedorRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Testes da parte de recuperar VendedorValorDTO

    @Test
    public void testVendedorPorData() {
        // Dados de entrada
        String dataInicio = "15/05/2023";
        String dataFinal = "17/05/2023";

        // Comportamento esperado
        Vendedor vendedor1 = new Vendedor("Jean Alves");
        Vendedor vendedor2 = new Vendedor("Kenji Vitor");

        Venda venda1 = new Venda();
        venda1.setValor(BigDecimal.valueOf(100));

        Venda venda2 = new Venda();
        venda2.setValor(BigDecimal.valueOf(200));

        Venda venda3 = new Venda();
        venda3.setValor(BigDecimal.valueOf(150));

        Venda venda4 = new Venda();
        venda4.setValor(BigDecimal.valueOf(300));

        vendedor1.setVendas(Arrays.asList(venda1, venda2));
        vendedor2.setVendas(Arrays.asList(venda3, venda4));

        when(vendedorRepository.buscarVendedoresComVendasEntreDatas(any(), any())).thenReturn(Arrays.asList(vendedor1, vendedor2));

        // Execução do método
        List<VendedorValorDTO> resultado = vendedorService.vendedorPorData(dataInicio, dataFinal);

        // Verificações
        assertEquals(2, resultado.size());

        VendedorValorDTO vendedor1DTO = resultado.get(0);
        assertEquals("Jean Alves", vendedor1DTO.getNome());
        assertEquals(BigDecimal.valueOf(300), vendedor1DTO.getTotalVendas());
        assertEquals(BigDecimal.valueOf(150).setScale(2, RoundingMode.DOWN), vendedor1DTO.getMediaDiariaVendas());



        VendedorValorDTO vendedor2DTO = resultado.get(1);
        assertEquals("Kenji Vitor", vendedor2DTO.getNome());
        assertEquals(BigDecimal.valueOf(450), vendedor2DTO.getTotalVendas());
        assertEquals(BigDecimal.valueOf(225).setScale(2, RoundingMode.DOWN), vendedor2DTO.getMediaDiariaVendas());

        verify(vendedorRepository, times(1)).buscarVendedoresComVendasEntreDatas(any(), any());
    }


    @Test
    public void testVendedorPorDataDataExceptionDataNull() {
        // Dados de entrada
        String dataInicio = null;
        String dataFinal = null;

        // Execução do método e verificação da exceção
        assertThrows(DataException.class, () -> {
            vendedorService.vendedorPorData(dataInicio, dataFinal);
        });
    }

    @Test
    public void testVendedorPorDataDataExceptionFormatoInvalido() {
        // Dados de entrada
        String dataInicio = "2023/05/17";
        String dataFinal = "2023/05/20";

        // Execução do método e verificação da exceção
        assertThrows(DataException.class, () -> {
            vendedorService.vendedorPorData(dataInicio, dataFinal);
        });
    }

    @Test
    public void testVendedorPorDataDataExceptionDataInicioNaFrenteDataFinal() {
        // Dados de entrada
        String dataInicio = "20/05/2023";
        String dataFinal = "17/05/2023";

        // Execução do método e verificação da exceção
        assertThrows(DataException.class, () -> {
            vendedorService.vendedorPorData(dataInicio, dataFinal);
        });
    }

    //Testes da parte de salvar vendedor
    @Test
    public void testSaveVendedor() {
        // Dados de entrada
        VendedorDTO dto = new VendedorDTO();
        dto.setNome("Jean Alves");

        // Comportamento esperado
        Vendedor vendedorSalvo = new Vendedor("Jean Alves");
        vendedorSalvo.setId(1);

        // Mock do repositório
        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorSalvo);

        // Execução do método
        Integer idVendedorSalvo = vendedorService.saveVendedor(dto);

        // Verificações
        assertEquals(1, idVendedorSalvo);
        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
    }

    @Test
    public void testSaveVendedorAtributoFaltanteException() {
        // Dados de entrada
        VendedorDTO dto = new VendedorDTO();
        dto.setNome(null);

        // Execução do método e verificação da exceção
        assertThrows(AtributoFaltanteException.class, () -> {
            vendedorService.saveVendedor(dto);
        });

        // Verificações adicionais
        verify(vendedorRepository, never()).save(any(Vendedor.class));
    }

    @Test
    public void testSaveVendedorAtributoFaltanteExceptionEmpty() {
        // Dados de entrada
        VendedorDTO dto = new VendedorDTO();
        dto.setNome("");

        // Execução do método e verificação da exceção
        assertThrows(AtributoFaltanteException.class, () -> {
            vendedorService.saveVendedor(dto);
        });

        // Verificações adicionais
        verify(vendedorRepository, never()).save(any(Vendedor.class));
    }
}