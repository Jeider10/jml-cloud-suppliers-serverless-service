package com.cloud.jml.exception.proveedor;

import org.springframework.http.HttpStatus;

public class ProveedorDeletionException extends ProveedorRuntimeException {

    public ProveedorDeletionException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "🗑️ [ELIMINACION] " + message);
    }

    public ProveedorDeletionException(String message, Throwable cause) {
        super(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "🗑️ [ELIMINACION] " + message +
                        (cause != null ? " | 💥 Causa: " + cause.getMessage() : "")
        );
    }

    // 🔒 Violacion de integridad referencial (por constraints o dependencias)
    public static ProveedorDeletionException integrityViolation(Throwable cause) {
        return new ProveedorDeletionException(
                "❌ [INTEGRIDAD] No se pudo eliminar el proveedor debido a una violacion de integridad referencial",
                cause
        );
    }

    // ⚙️ Error de acceso a datos
    public static ProveedorDeletionException dataAccessError(Throwable cause) {
        return new ProveedorDeletionException(
                "❌ [DATOS] Error de acceso a la base de datos al intentar eliminar el proveedor",
                cause
        );
    }

    // 💥 Error inesperado
    public static ProveedorDeletionException unexpected(Throwable cause) {
        return new ProveedorDeletionException(
                "💥 [INESPERADO] Ocurrio un error inesperado al intentar eliminar el proveedor",
                cause
        );
    }
}
