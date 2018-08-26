package cursomc.com.felipebatista.cursomc.domain.enums;

import java.util.Arrays;

public enum TipoCliente {

    PESSOAFISICA(1, "Pessoa Física"),
    PESSOAJURIDICA(2, "Pessoa Jurídica");

    private Integer codigo;
    private String descricao;

    private TipoCliente(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoCliente toEnum(Integer id) {
        return Arrays.asList(TipoCliente.values()).stream()
                .filter(f -> f.equals(id))
                .findFirst().orElse(null);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

}


