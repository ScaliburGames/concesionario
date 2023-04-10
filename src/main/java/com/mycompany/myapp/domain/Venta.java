package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TipoPago;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago")
    private TipoPago tipoPago;

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Vendedor> vendedores = new HashSet<>();

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Comprador> compradores = new HashSet<>();

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta", "marcas" }, allowSetters = true)
    private Set<Coche> coches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Venta fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public TipoPago getTipoPago() {
        return this.tipoPago;
    }

    public Venta tipoPago(TipoPago tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Set<Vendedor> getVendedores() {
        return this.vendedores;
    }

    public void setVendedores(Set<Vendedor> vendedors) {
        if (this.vendedores != null) {
            this.vendedores.forEach(i -> i.setVenta(null));
        }
        if (vendedors != null) {
            vendedors.forEach(i -> i.setVenta(this));
        }
        this.vendedores = vendedors;
    }

    public Venta vendedores(Set<Vendedor> vendedors) {
        this.setVendedores(vendedors);
        return this;
    }

    public Venta addVendedores(Vendedor vendedor) {
        this.vendedores.add(vendedor);
        vendedor.setVenta(this);
        return this;
    }

    public Venta removeVendedores(Vendedor vendedor) {
        this.vendedores.remove(vendedor);
        vendedor.setVenta(null);
        return this;
    }

    public Set<Comprador> getCompradores() {
        return this.compradores;
    }

    public void setCompradores(Set<Comprador> compradors) {
        if (this.compradores != null) {
            this.compradores.forEach(i -> i.setVenta(null));
        }
        if (compradors != null) {
            compradors.forEach(i -> i.setVenta(this));
        }
        this.compradores = compradors;
    }

    public Venta compradores(Set<Comprador> compradors) {
        this.setCompradores(compradors);
        return this;
    }

    public Venta addCompradores(Comprador comprador) {
        this.compradores.add(comprador);
        comprador.setVenta(this);
        return this;
    }

    public Venta removeCompradores(Comprador comprador) {
        this.compradores.remove(comprador);
        comprador.setVenta(null);
        return this;
    }

    public Set<Coche> getCoches() {
        return this.coches;
    }

    public void setCoches(Set<Coche> coches) {
        if (this.coches != null) {
            this.coches.forEach(i -> i.setVenta(null));
        }
        if (coches != null) {
            coches.forEach(i -> i.setVenta(this));
        }
        this.coches = coches;
    }

    public Venta coches(Set<Coche> coches) {
        this.setCoches(coches);
        return this;
    }

    public Venta addCoches(Coche coche) {
        this.coches.add(coche);
        coche.setVenta(this);
        return this;
    }

    public Venta removeCoches(Coche coche) {
        this.coches.remove(coche);
        coche.setVenta(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", tipoPago='" + getTipoPago() + "'" +
            "}";
    }
}
