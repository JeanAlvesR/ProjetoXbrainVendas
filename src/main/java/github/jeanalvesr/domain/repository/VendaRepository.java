package github.jeanalvesr.domain.repository;

import github.jeanalvesr.domain.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Integer> {
}
