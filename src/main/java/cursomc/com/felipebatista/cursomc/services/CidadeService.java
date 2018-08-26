package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

}
