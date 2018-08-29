package cursomc.com.felipebatista.cursomc.dto;

import cursomc.com.felipebatista.cursomc.domain.Categoria;

import java.io.Serializable;

public class CategoriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;

    public CategoriaDTO() {

    }

    public CategoriaDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
    }

    public Integer getId() {
        return id;
    }

    public CategoriaDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public CategoriaDTO setNome(String nome) {
        this.nome = nome;
        return this;
    }
}
