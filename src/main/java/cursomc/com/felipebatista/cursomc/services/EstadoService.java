package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.domain.Cliente;
import cursomc.com.felipebatista.cursomc.domain.Estado;
import cursomc.com.felipebatista.cursomc.repository.ClienteRepository;
import cursomc.com.felipebatista.cursomc.repository.EstadoRepository;
import cursomc.com.felipebatista.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repository;

    public Estado find(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + ", Tipo: " + Estado.class.getName()));
    }

}
