package github.jeanalvesr.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "venda")
public class Venda {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "data")
    private LocalDate data;

    @Column(name = "valor", precision = 20, scale = 2)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Vendedor vendedor;
}
