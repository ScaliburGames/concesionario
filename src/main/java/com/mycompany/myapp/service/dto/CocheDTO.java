package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Coche} entity.
 */
public class CocheDTO implements Serializable {

    private Long id;

    private String matricula;

    private String color;

    private Double precio;

    private Boolean vendido;

    private VentaDTO venta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Boolean getVendido() {
        return vendido;
    }

    public void setVendido(Boolean vendido) {
        this.vendido = vendido;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CocheDTO)) {
            return false;
        }

        CocheDTO cocheDTO = (CocheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cocheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CocheDTO{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", color='" + getColor() + "'" +
            ", precio=" + getPrecio() +
            ", vendido='" + getVendido() + "'" +
            ", venta=" + getVenta() +
            "}";
    }
}
