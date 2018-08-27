package cursomc.com.felipebatista.cursomc.domain;

import cursomc.com.felipebatista.cursomc.domain.enums.EstadoPagamento;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Entity
public class PagamentoComBoleto extends Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dataVencimento;
    private Date dataPagamento;

    public PagamentoComBoleto() {
    }

    public PagamentoComBoleto(Integer id, EstadoPagamento estado, Pedido pedido, Date dataVencimento, Date dataPagamento) {
        super(id, estado, pedido);
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public PagamentoComBoleto setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
        return this;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public PagamentoComBoleto setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
        return this;
    }
}
