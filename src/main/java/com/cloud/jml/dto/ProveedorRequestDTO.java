package com.cloud.jml.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class ProveedorRequestDTO {

    private Long codigoSucursal;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;
}
