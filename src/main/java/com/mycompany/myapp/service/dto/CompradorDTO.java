package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Comprador} entity.
 */
public class CompradorDTO implements Serializable {

    private Long id;

    private String nombre;

    private String apellidos;

    private String dni;

    private Instant fechaNacimiento;

    private Instant fechaCarnet;

    private VentaDTO venta;

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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Instant getFechaCarnet() {
        return fechaCarnet;
    }

    public void setFechaCarnet(Instant fechaCarnet) {
        this.fechaCarnet = fechaCarnet;
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
        if (!(o instanceof CompradorDTO)) {
            return false;
        }

        CompradorDTO compradorDTO = (CompradorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compradorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompradorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", dni='" + getDni() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", fechaCarnet='" + getFechaCarnet() + "'" +
            ", venta=" + getVenta() +
            "}";
    }
}
