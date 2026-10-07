package com.cloud.jml.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedores")
public class ProveedorEntity {

    @Id
    @Column(name = "codigo_sucursal", nullable = false)
    private String codigoSucursal;

    @Column(nullable = false)
    private String nombre;

    private String telefono;
    private String direccion;
    private String correo;

    @Column(name = "creado_por", length = 150)
    private String creadoPor;

    @Column(name = "actualizado_por", length = 150)
    private String actualizadoPor;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ─── Soft delete (papelera) ───────────────────────────────────────────────
    @Column(name = "eliminado", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean eliminado = false;

    @Column(name = "fecha_eliminacion")
    private LocalDateTime fechaEliminacion;

    // Fecha hasta la que el registro se conserva en papelera antes de poder eliminarse definitivamente.
    // Se calcula al hacer el soft delete: fechaEliminacion + 2 meses.
    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Column(name = "eliminado_por_id", length = 150)
    private String eliminadoPorId;

    @Column(name = "eliminado_por_nombre", length = 200)
    private String eliminadoPorNombre;

    // Rol del usuario que envio el registro a papelera
    @Column(name = "eliminado_por_rol", length = 100)
    private String eliminadoPorRol;

    // Motivo de eliminacion (opcional)
    @Column(name = "motivo", length = 500)
    private String motivo;
}
