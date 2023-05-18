package github.jeanalvesr.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.jeanalvesr.domain.entity.Venda;
import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.VendaRepository;
import github.jeanalvesr.domain.repository.VendedorRepository;
import github.jeanalvesr.rest.dto.VendedorDTO;
import github.jeanalvesr.rest.dto.VendedorValorDTO;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @BeforeEach
    public void setup(){

        Vendedor vendedor1 = vendedorRepository.save(new Vendedor("Jean Alves"));
        Vendedor vendedor2 = vendedorRepository.save(new Vendedor("Kenji Vitor"));
        vendaRepository.save(new Venda(null, LocalDate.of(2023, 05, 16), BigDecimal.valueOf(300), vendedor1));
        vendaRepository.save(new Venda(null, LocalDate.of(2023, 05, 16), BigDecimal.valueOf(450), vendedor2));

    }


    @Test
    public void testVendedorPorDataInicialDepoisFinalBadRequest() throws Exception {
        String dataIni = "15/05/2023";
        String dataFinal = "17/05/2022";  // Data final inválida (anterior à data inicial)

        mockMvc.perform(MockMvcRequestBuilders.get("/vendedores")
                        .param("dataIni", dataIni)
                        .param("dataFinal", dataFinal))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testVendedorPorDataErradaBadRequest() throws Exception {
        String dataIni = "15-06/2023";  // Data inicial inválida
        String dataFinal = "17-06/2023";  // Data final inválida

        mockMvc.perform(MockMvcRequestBuilders.get("/vendedores")
                        .param("dataIni", dataIni)
                        .param("dataFinal", dataFinal))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testVendedorPorData() throws Exception {
        String dataIni = "15/05/2023";
        String dataFinal = "17/05/2023";

        mockMvc.perform(MockMvcRequestBuilders.get("/vendedores")
                        .param("dataIni", dataIni)
                        .param("dataFinal", dataFinal))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Jean Alves"))
                .andExpect(jsonPath("$[0].totalVendas").value(300))
                .andExpect(jsonPath("$[0].mediaDiariaVendas").value(150))
                .andExpect(jsonPath("$[1].nome").value("Kenji Vitor"))
                .andExpect(jsonPath("$[1].totalVendas").value(450))
                .andExpect(jsonPath("$[1].mediaDiariaVendas").value(225));
    }

    @Test
    public void testSaveVendedor() throws Exception {
        VendedorDTO vendedorDTO = new VendedorDTO();
        vendedorDTO.setNome("Jean Alves");

        mockMvc.perform(MockMvcRequestBuilders.post("/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendedorDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testSaveVendedorBadRequest() throws Exception {
        VendedorDTO vendedorDTO = new VendedorDTO();
        vendedorDTO.setNome("");//nome vazio

        mockMvc.perform(MockMvcRequestBuilders.post("/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendedorDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }


}
