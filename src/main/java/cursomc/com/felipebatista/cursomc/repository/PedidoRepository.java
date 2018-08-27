package cursomc.com.felipebatista.cursomc.repository;

import cursomc.com.felipebatista.cursomc.domain.Pedido;
import cursomc.com.felipebatista.cursomc.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
