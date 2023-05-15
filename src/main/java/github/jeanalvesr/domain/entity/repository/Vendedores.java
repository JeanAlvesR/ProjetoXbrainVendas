package github.jeanalvesr.domain.entity.repository;

import github.jeanalvesr.domain.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Vendedores extends JpaRepository<Vendedor, Integer> {
}
