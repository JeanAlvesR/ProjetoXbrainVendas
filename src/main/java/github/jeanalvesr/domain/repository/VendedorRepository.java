package github.jeanalvesr.domain.repository;

import github.jeanalvesr.domain.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {

    @Query("SELECT DISTINCT v FROM Vendedor v JOIN FETCH v.vendas vd WHERE vd.data >= :dataIni AND vd.data <= :dataFinal")
    List<Vendedor> buscarVendedoresComVendasEntreDatas(@Param("dataIni") LocalDate dataIni, @Param("dataFinal") LocalDate dataFinal);

}
