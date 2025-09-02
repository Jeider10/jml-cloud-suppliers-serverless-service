package com.cloud.jml.exception;

public class ProveedorDuplicadoException extends RuntimeException {

    public ProveedorDuplicadoException(String identificacion) {
        super("El cliente con identificación " + identificacion + " ya existe.");
    }
}
