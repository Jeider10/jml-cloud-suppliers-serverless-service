package com.cloud.jml.utils;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.exception.proveedor.ProveedorDeletionException;
import com.cloud.jml.exception.proveedor.ProveedorNoEncontradoException;
import com.cloud.jml.exception.proveedor.ProveedorPersistenceException;
import com.cloud.jml.model.ProveedorEntity;
import com.cloud.jml.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component  // 🔹 Anotación para indicar que es un componente de Spring
public class ProveedorUtils {

    private final ProveedorRepository proveedorRepository;

    public ProveedorUtils(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
        log.info("🔥 ProveedorUtils inicializado correctamente.");
    }

    /**
     * 💾 Guarda la orden en BD con manejo de excepciones.
     */
    public ProveedorEntity guardarProveedorBD(ProveedorEntity proveedorEntity) {
        try {
            return proveedorRepository.save(proveedorEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violación de integridad al guardar el proveedor: {}", e.getMessage(), e);
            throw new ProveedorPersistenceException("Error de integridad en base de datos al guardar el proveedor", e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al guardar el proveedor: {}", e.getMessage(), e);
            throw new ProveedorPersistenceException("Error al guardar el proveedor en la base de datos", e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al guardar el proveedor: {}", e.getMessage(), e);
            throw new ProveedorPersistenceException("Error inesperado al registrar el proveedor", e);
        }
    }

    /**
     * 🗑️ Elimina la orden de BD con manejo de excepciones.
     */
    public void eliminarProveedorBD(ProveedorEntity proveedorEntity) {
        try {
            proveedorRepository.delete(proveedorEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violación de integridad al eliminar el proveedor: {}", e.getMessage(), e);
            throw new ProveedorDeletionException("Error de integridad en base de datos al eliminar el proveedor", e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al eliminar el proveedor: {}", e.getMessage(), e);
            throw new ProveedorDeletionException("Error al eliminar el proveedor en la base de datos", e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al eliminar el proveedor: {}", e.getMessage(), e);
            throw new ProveedorDeletionException("Error inesperado al eliminar el proveedor", e);
        }
    }

    /**
     * 🔍 Valida la existencia de un cliente en BD.
     */
    public ProveedorEntity validarExistenciaProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 Validando existencia de Proveedor: {} con Código de Sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());
        Optional<ProveedorEntity> optionalProveedor = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (optionalProveedor.isPresent()) {
            ProveedorEntity proveedorEntity = optionalProveedor.get();
            log.info("✅ Proveedor encontrado con Cdigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            return proveedorEntity;
        } else {
            log.warn("⚠️ Proveedor no encontrado con Código de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorNoEncontradoException(proveedorRequestDTO.getCodigoSucursal());
        }
    }
}
