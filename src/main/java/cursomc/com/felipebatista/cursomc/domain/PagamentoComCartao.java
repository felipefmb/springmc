package cursomc.com.felipebatista.cursomc.domain;

import cursomc.com.felipebatista.cursomc.domain.enums.EstadoPagamento;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class PagamentoComCartao extends Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer numeroParcelas;

    public PagamentoComCartao() {
    }

    public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroParcelas) {
        super(id, estado, pedido);
        this.numeroParcelas = numeroParcelas;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public PagamentoComCartao setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
        return this;
    }
}
