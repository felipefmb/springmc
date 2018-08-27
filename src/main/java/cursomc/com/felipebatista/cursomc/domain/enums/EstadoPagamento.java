package cursomc.com.felipebatista.cursomc.domain.enums;

import java.util.Arrays;

public enum EstadoPagamento {

    PENDENTE(1,"Pendente"),
    QUITADO(2, "Quitado"),
    CANCELADO(3, "Cancelado");

    private Integer codigo;
    private String descricao;

    private EstadoPagamento(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static EstadoPagamento toEnum(Integer id) {
        return Arrays.asList(EstadoPagamento.values()).stream()
                .filter(f -> f.getCodigo().equals(id))
                .findFirst().orElse(null);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
