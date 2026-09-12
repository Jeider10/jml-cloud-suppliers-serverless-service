package com.cloud.jml.exception.proveedor;

import org.springframework.http.HttpStatus;

public class ProveedorDuplicadoException extends ProveedorRuntimeException {

    public ProveedorDuplicadoException(String codigoSucursal) {
        super(
                HttpStatus.CONFLICT,
                "⚠️ [DUPLICADO] Proveedor duplicado detectado con codigo de sucursal: " + codigoSucursal);
    }
}
