package com.cloud.jml.dto;

import java.time.LocalDateTime;

public class ProveedorDTO {

    private Long nic;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public ProveedorDTO() {
        // Constructor
    }

    public ProveedorDTO(Long nic, String nombre, String telefono, String direccion, String correo, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.nic = nic;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters
    public Long getNic() {
        return nic;
    }

    public void setNic(Long nic) {
        this.nic = nic;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
