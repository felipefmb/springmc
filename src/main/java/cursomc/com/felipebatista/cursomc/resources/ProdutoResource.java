package cursomc.com.felipebatista.cursomc.resources;

import cursomc.com.felipebatista.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService service;
}
