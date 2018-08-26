package cursomc.com.felipebatista.cursomc.resources;

import cursomc.com.felipebatista.cursomc.services.ClienteService;
import cursomc.com.felipebatista.cursomc.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/enderecos")
public class EnderecoResource {

    @Autowired
    private EnderecoService service;

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public ResponseEntity<?> listar(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(service.buscar(id));
    }

}
