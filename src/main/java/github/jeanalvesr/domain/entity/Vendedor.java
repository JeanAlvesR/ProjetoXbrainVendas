package github.jeanalvesr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "vendedor")
public class Vendedor {

    @Id
    private Integer id;

    @Column(name= "nome", length = 30)
    private String nome;

    @OneToMany(mappedBy = "vendedor", fetch = FetchType.LAZY )
    private List<Venda> vendas;
}
