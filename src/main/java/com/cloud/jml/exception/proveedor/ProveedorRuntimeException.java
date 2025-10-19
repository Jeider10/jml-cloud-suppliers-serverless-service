package com.cloud.jml.exception.proveedor;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProveedorRuntimeException extends RuntimeException {

    private final HttpStatus status;

    public ProveedorRuntimeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
