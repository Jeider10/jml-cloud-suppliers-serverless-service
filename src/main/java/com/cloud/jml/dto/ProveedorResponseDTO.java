package com.cloud.jml.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class ProveedorResponseDTO {

    // Getters y Setters
    private Long codigoSucursal;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
