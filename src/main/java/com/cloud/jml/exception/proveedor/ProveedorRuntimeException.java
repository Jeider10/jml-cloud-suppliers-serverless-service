package com.cloud.jml.exception.proveedor;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ProveedorRuntimeException extends RuntimeException {

    private final HttpStatus status;

    protected ProveedorRuntimeException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    protected ProveedorRuntimeException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
