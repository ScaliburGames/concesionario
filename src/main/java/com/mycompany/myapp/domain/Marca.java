package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Marca.
 */
@Entity
@Table(name = "marca")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Marca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "venta", "marcas" }, allowSetters = true)
    private Coche coche;

    @OneToMany(mappedBy = "marca")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "marca" }, allowSetters = true)
    private Set<Modelo> modelos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Marca id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Marca nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Coche getCoche() {
        return this.coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    public Marca coche(Coche coche) {
        this.setCoche(coche);
        return this;
    }

    public Set<Modelo> getModelos() {
        return this.modelos;
    }

    public void setModelos(Set<Modelo> modelos) {
        if (this.modelos != null) {
            this.modelos.forEach(i -> i.setMarca(null));
        }
        if (modelos != null) {
            modelos.forEach(i -> i.setMarca(this));
        }
        this.modelos = modelos;
    }

    public Marca modelos(Set<Modelo> modelos) {
        this.setModelos(modelos);
        return this;
    }

    public Marca addModelos(Modelo modelo) {
        this.modelos.add(modelo);
        modelo.setMarca(this);
        return this;
    }

    public Marca removeModelos(Modelo modelo) {
        this.modelos.remove(modelo);
        modelo.setMarca(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Marca)) {
            return false;
        }
        return id != null && id.equals(((Marca) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Marca{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
