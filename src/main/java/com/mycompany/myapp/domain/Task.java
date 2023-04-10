package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "salario")
    private Double salario;

    @Column(name = "numero_venta")
    private Long numeroVenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSalario() {
        return this.salario;
    }

    public Task salario(Double salario) {
        this.setSalario(salario);
        return this;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Long getNumeroVenta() {
        return this.numeroVenta;
    }

    public Task numeroVenta(Long numeroVenta) {
        this.setNumeroVenta(numeroVenta);
        return this;
    }

    public void setNumeroVenta(Long numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", salario=" + getSalario() +
            ", numeroVenta=" + getNumeroVenta() +
            "}";
    }
}
