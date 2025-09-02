package com.cloud.jml.exception;

public class ProveedorNoEncontradoException extends RuntimeException {

    public ProveedorNoEncontradoException(String identificacion) {
        super("No se encontró cliente con identificación: " + identificacion);
    }
}
