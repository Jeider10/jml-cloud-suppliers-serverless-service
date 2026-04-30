package com.cloud.jml.exception.proveedor;

import org.springframework.http.HttpStatus;

public class ProveedorPersistenceException extends ProveedorRuntimeException {

    public ProveedorPersistenceException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "💾 [PERSISTENCIA] " + message);
    }

    public ProveedorPersistenceException(String message, Throwable cause) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "💾 [PERSISTENCIA] " + message +
                        (cause != null ? " | 💥 Causa: " + cause.getMessage() : "")
        );
    }

    // 🔒 Violacion de integridad (constraint, duplicado, etc.) al guardar
    public static ProveedorPersistenceException integrityViolation(Throwable cause) {
        return new ProveedorPersistenceException(
                "❌ [INTEGRIDAD] Violacion de integridad en base de datos al guardar el proveedor",
                cause
        );
    }

    // ⚙️ Error tecnico de acceso a datos
    public static ProveedorPersistenceException dataAccessError(Throwable cause) {
        return new ProveedorPersistenceException(
                "❌ [DATOS] Error de acceso a datos al intentar guardar el proveedor",
                cause
        );
    }

    // 💥 Error inesperado
    public static ProveedorPersistenceException unexpected(Throwable cause) {
        return new ProveedorPersistenceException(
                "💥 [INESPERADO] Ocurrio un error inesperado al registrar el proveedor",
                cause
        );
    }
}
