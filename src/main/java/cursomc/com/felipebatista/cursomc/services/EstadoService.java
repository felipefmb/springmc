package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repository;

}
