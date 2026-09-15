package com.cloud.jml.repository;

import com.cloud.jml.model.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, String> {

    // ─── Activos (eliminado = false) ─────────────────────────────────────────
    Optional<ProveedorEntity> findByCodigoSucursalAndEliminadoFalse(String codigoSucursal);

    List<ProveedorEntity> findAllByEliminadoFalse();

    List<ProveedorEntity> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    List<ProveedorEntity> findByCorreoContainingIgnoreCaseAndEliminadoFalse(String correo);

    List<ProveedorEntity> findByCreadoPorContainingIgnoreCaseAndEliminadoFalse(String creadoPor);

    List<ProveedorEntity> findByFechaCreacionBetweenAndEliminadoFalse(LocalDateTime inicio, LocalDateTime fin);

    List<ProveedorEntity> findByFechaActualizacionBetweenAndEliminadoFalse(LocalDateTime inicio, LocalDateTime fin);

    // ─── Papelera (eliminado = true) ─────────────────────────────────────────
    List<ProveedorEntity> findAllByEliminadoTrue();

    Optional<ProveedorEntity> findByCodigoSucursalAndEliminadoTrue(String codigoSucursal);

    // ─── Verificar duplicado ──────────────────────────────────────────────────
    Optional<ProveedorEntity> findByCodigoSucursal(String codigoSucursal);
}
