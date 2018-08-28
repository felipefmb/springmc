package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.domain.Cliente;
import cursomc.com.felipebatista.cursomc.domain.Endereco;
import cursomc.com.felipebatista.cursomc.repository.ClienteRepository;
import cursomc.com.felipebatista.cursomc.repository.EnderecoRepository;
import cursomc.com.felipebatista.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    public Endereco find(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! ID: " + id + ", Tipo: " + Endereco.class.getName()));
    }

}
