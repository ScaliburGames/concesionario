package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coche.
 */
@Entity
@Table(name = "coche")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Coche implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "color")
    private String color;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "vendido")
    private Boolean vendido;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vendedores", "compradores", "coches" }, allowSetters = true)
    private Venta venta;

    @OneToMany(mappedBy = "coche")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coche", "modelos" }, allowSetters = true)
    private Set<Marca> marcas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Coche id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Coche matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getColor() {
        return this.color;
    }

    public Coche color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Coche precio(Double precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Boolean getVendido() {
        return this.vendido;
    }

    public Coche vendido(Boolean vendido) {
        this.setVendido(vendido);
        return this;
    }

    public void setVendido(Boolean vendido) {
        this.vendido = vendido;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Coche venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public Set<Marca> getMarcas() {
        return this.marcas;
    }

    public void setMarcas(Set<Marca> marcas) {
        if (this.marcas != null) {
            this.marcas.forEach(i -> i.setCoche(null));
        }
        if (marcas != null) {
            marcas.forEach(i -> i.setCoche(this));
        }
        this.marcas = marcas;
    }

    public Coche marcas(Set<Marca> marcas) {
        this.setMarcas(marcas);
        return this;
    }

    public Coche addMarcas(Marca marca) {
        this.marcas.add(marca);
        marca.setCoche(this);
        return this;
    }

    public Coche removeMarcas(Marca marca) {
        this.marcas.remove(marca);
        marca.setCoche(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coche)) {
            return false;
        }
        return id != null && id.equals(((Coche) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coche{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", color='" + getColor() + "'" +
            ", precio=" + getPrecio() +
            ", vendido='" + getVendido() + "'" +
            "}";
    }
}
