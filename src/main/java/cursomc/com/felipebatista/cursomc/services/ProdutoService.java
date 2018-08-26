package cursomc.com.felipebatista.cursomc.services;

import cursomc.com.felipebatista.cursomc.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

}
