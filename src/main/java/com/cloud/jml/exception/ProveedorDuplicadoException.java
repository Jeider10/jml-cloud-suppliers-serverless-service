package com.cloud.jml.exception;

public class ProveedorDuplicadoException extends RuntimeException {

    public ProveedorDuplicadoException(Long nic) {
        super("El proveedor con NIC " + nic + " ya existe.");
    }
}
