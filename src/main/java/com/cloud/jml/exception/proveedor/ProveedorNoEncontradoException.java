package com.cloud.jml.exception.proveedor;

import org.springframework.http.HttpStatus;

public class ProveedorNoEncontradoException extends ProveedorRuntimeException {

    public ProveedorNoEncontradoException(Long codigoSucursal) {
        super(
                HttpStatus.NOT_FOUND,
                "❌ [CONSULTA] Proveedor no encontrado con código de sucursal: " + codigoSucursal);
    }
}
