package com.cloud.jml.exception;

public class ProveedorDuplicadoException extends RuntimeException {

    public ProveedorDuplicadoException(Long codigoSucursal) {
        super("El proveedor con Código de Sucursal " + codigoSucursal + " ya existe.");
    }
}
