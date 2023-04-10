package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Marca} entity.
 */
public class MarcaDTO implements Serializable {

    private Long id;

    private String nombre;

    private CocheDTO coche;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CocheDTO getCoche() {
        return coche;
    }

    public void setCoche(CocheDTO coche) {
        this.coche = coche;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarcaDTO)) {
            return false;
        }

        MarcaDTO marcaDTO = (MarcaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, marcaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MarcaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", coche=" + getCoche() +
            "}";
    }
}
