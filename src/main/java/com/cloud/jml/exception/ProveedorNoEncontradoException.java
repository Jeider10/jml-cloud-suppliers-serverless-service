package com.cloud.jml.exception;

public class ProveedorNoEncontradoException extends RuntimeException {

    public ProveedorNoEncontradoException(Long codigoSucursal) {
        super("No se encontró proveedor con Código de Sucursal: " + codigoSucursal);
    }
}
