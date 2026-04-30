package com.cloud.jml.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
public class ProveedorRequestDTO {

    // FIX: Se agregaron validaciones Jakarta Bean Validation para evitar datos invalidos
    @NotNull(message = "El campo 'codigoSucursal' es obligatorio")
    private Long codigoSucursal;

    @NotBlank(message = "El campo 'nombre' es obligatorio")
    @Size(max = 100, message = "El campo 'nombre' no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 20, message = "El campo 'telefono' no puede exceder 20 caracteres")
    private String telefono;

    @Size(max = 200, message = "El campo 'direccion' no puede exceder 200 caracteres")
    private String direccion;

    @Size(max = 100, message = "El campo 'correo' no puede exceder 100 caracteres")
    private String correo;
}
