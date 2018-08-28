package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.domain.Categoria;
import cursomc.com.felipebatista.cursomc.domain.Pedido;
import cursomc.com.felipebatista.cursomc.repository.CategoriaRepository;
import cursomc.com.felipebatista.cursomc.repository.PedidoRepository;
import cursomc.com.felipebatista.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public Pedido buscar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + ", Tipo: " + Pedido.class.getName()));
    }

}
