package com.cloud.jml.utils.proveedor;

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
@Component  // 🔹 Anotacion para indicar que es un componente de Spring
public class ProveedorUtils {

    private final ProveedorRepository proveedorRepository;

    public ProveedorUtils(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
        log.info("🔥 ProveedorUtils inicializado correctamente.");
    }

    public ProveedorEntity validarExistenciaProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [SOLICITUD] Validando existencia de proveedor: {} con codigo de sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());
        Optional<ProveedorEntity> optionalProveedor = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (optionalProveedor.isPresent()) {
            ProveedorEntity proveedorEntity = optionalProveedor.get();
            log.info("✅ [FINALIZADO] Proveedor encontrado con codigo de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

            return proveedorEntity;
        } else {
            log.warn("❌ [RESULTADO] Proveedor no encontrado con codigo de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorNoEncontradoException(proveedorRequestDTO.getCodigoSucursal());
        }
    }

    public ProveedorEntity guardarProveedorBD(ProveedorEntity proveedorEntity) {
        try {
            return proveedorRepository.save(proveedorEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violacion de integridad al guardar el proveedor: {}", e.getMessage(), e);
            throw ProveedorPersistenceException.integrityViolation(e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al guardar el proveedor: {}", e.getMessage(), e);
            throw ProveedorPersistenceException.dataAccessError(e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al guardar el proveedor: {}", e.getMessage(), e);
            throw ProveedorPersistenceException.unexpected(e);
        }
    }

    public void eliminarProveedorBD(ProveedorEntity proveedorEntity) {
        try {
            proveedorRepository.delete(proveedorEntity);

        } catch (DataIntegrityViolationException e) {
            log.error("🚨 Violacion de integridad al eliminar el proveedor: {}", e.getMessage(), e);
            throw ProveedorDeletionException.integrityViolation(e);

        } catch (DataAccessException e) {
            log.error("🚨 Error de acceso a datos al eliminar el proveedor: {}", e.getMessage(), e);
            throw ProveedorDeletionException.dataAccessError(e);

        } catch (Exception e) {
            log.error("🚨 Error inesperado al eliminar el proveedor: {}", e.getMessage(), e);
            throw ProveedorDeletionException.unexpected(e);
        }
    }
}
