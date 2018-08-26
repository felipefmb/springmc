package cursomc.com.felipebatista.cursomc.repository;


import cursomc.com.felipebatista.cursomc.domain.Categoria;
import cursomc.com.felipebatista.cursomc.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
