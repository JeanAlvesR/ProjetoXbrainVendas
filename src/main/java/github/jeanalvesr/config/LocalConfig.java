package github.jeanalvesr.config;

import github.jeanalvesr.domain.entity.Vendedor;
import github.jeanalvesr.domain.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private VendedorRepository vendedores;
    //Utilizado no inicio nos testes
    @Bean
    public void startDB(){
        Vendedor vendedor = new Vendedor(1, "Jean Alves", null);
        vendedores.save(vendedor);
    }
}
