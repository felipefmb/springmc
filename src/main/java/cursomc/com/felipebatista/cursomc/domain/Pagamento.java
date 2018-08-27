package cursomc.com.felipebatista.cursomc.domain;

import cursomc.com.felipebatista.cursomc.domain.enums.EstadoPagamento;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id

    private Integer id;
    private Integer estado;

    @OneToOne
    @JoinColumn(name="pedido_id")
    @MapsId
    private Pedido pedido;

    public Pagamento() {
    }

    public Pagamento(Integer id, EstadoPagamento estado, Pedido pedido) {
        this.id = id;
        this.estado = estado.getCodigo();
        this.pedido = pedido;
    }

    public Integer getId() {
        return id;
    }

    public Pagamento setId(Integer id) {
        this.id = id;
        return this;
    }

    public EstadoPagamento getEstado() {
        return EstadoPagamento.toEnum(estado);
    }

    public Pagamento setEstado(EstadoPagamento estado) {
        this.estado = estado.getCodigo();
        return this;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Pagamento setPedido(Pedido pedido) {
        this.pedido = pedido;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}