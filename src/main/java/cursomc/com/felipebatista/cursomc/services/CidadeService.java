package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.domain.Categoria;
import cursomc.com.felipebatista.cursomc.domain.Cidade;
import cursomc.com.felipebatista.cursomc.repository.CategoriaRepository;
import cursomc.com.felipebatista.cursomc.repository.CidadeRepository;
import cursomc.com.felipebatista.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

    public Cidade find(Integer id) {

        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + ", Tipo: " + Cidade.class.getName()));
    }

}
