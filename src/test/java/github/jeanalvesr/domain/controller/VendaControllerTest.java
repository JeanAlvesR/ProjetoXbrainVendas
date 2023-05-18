package github.jeanalvesr.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.VendedorRepository;
import github.jeanalvesr.rest.dto.VendaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendedorRepository vendedorRepository;

    private Vendedor vendedor;

    @BeforeEach
    public void setup(){
        vendedor = vendedorRepository.save(new Vendedor("Jean Alves"));
    }

    @Test
    public void testSaveVenda() throws Exception {
        VendaDTO vendaDTO = criaVendaDTO("15/05/2023", BigDecimal.valueOf(100), vendedor.getId(), "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("15/05/2023"))
                .andExpect(jsonPath("$.valor").value(100))
                .andExpect(jsonPath("$.idVendedor").value(vendedor.getId()))
                .andExpect(jsonPath("$.nomeVendedor").value("Jean Alves"));
    }

    @Test
    public void testSaveVendaRequisicaoInvalida() throws Exception {
        // Caso em que falta o valor
        VendaDTO vendaDTO1 = criaVendaDTO("15/05/2023", null, vendedor.getId(), "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO1)))
                .andExpect(status().isBadRequest());

        // Caso em que falta o idVendedor
        VendaDTO vendaDTO2 = criaVendaDTO("15/05/2023", BigDecimal.valueOf(100), null, "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO2)))
                .andExpect(status().isBadRequest());

        // Caso em que falta a data
        VendaDTO vendaDTO3 = criaVendaDTO(null, BigDecimal.valueOf(100), vendedor.getId(), "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveVendaIdVendedorInvalido() throws Exception {
        Integer idVendedorInvalido = 99; // ID de um vendedor que não existe no banco de dados
        VendaDTO vendaDTO = criaVendaDTO("15/05/2023", BigDecimal.valueOf(100), idVendedorInvalido, "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveVendaDataInvalida() throws Exception {
        // Data inválida: dia posterior à data atual
        LocalDate dataInvalida = LocalDate.now().plusDays(1);

        // Teste com valor, idVendedor e data inválida
        VendaDTO vendaDTO = criaVendaDTO(dataInvalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                BigDecimal.valueOf(100), vendedor.getId(), "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO)))
                .andExpect(status().isBadRequest());


        // Teste com valor, idVendedor e estrutura de data inválida
        vendaDTO = criaVendaDTO("2023/05/15", BigDecimal.valueOf(100), vendedor.getId(), "Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendaDTO)))
                .andExpect(status().isBadRequest());
    }

    private VendaDTO criaVendaDTO(String data, BigDecimal valor, Integer idVendedor, String nomeVendedor) {
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setData(data);
        vendaDTO.setValor(valor);
        vendaDTO.setIdVendedor(idVendedor);
        vendaDTO.setNomeVendedor(nomeVendedor);
        return vendaDTO;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
