package com.cloud.jml.exception;

public class ProveedorNoEncontradoException extends RuntimeException {

    public ProveedorNoEncontradoException(Long nic) {
        super("No se encontró proveedor con NIC: " + nic);
    }
}
