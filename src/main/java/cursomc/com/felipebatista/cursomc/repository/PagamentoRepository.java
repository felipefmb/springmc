package cursomc.com.felipebatista.cursomc.repository;

import cursomc.com.felipebatista.cursomc.domain.Pagamento;
import cursomc.com.felipebatista.cursomc.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
