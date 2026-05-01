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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public LocalDateTime parsearFechaInicio(String fecha) {
        log.info("📅 [FECHA] Intentando parsear fecha de inicio: {}", fecha);

        if (fecha == null || fecha.isBlank()) {
            log.error("⚠️ [ERROR] La fecha de inicio recibida es nula o vacia");
            throw new IllegalArgumentException("La fecha de inicio es obligatoria");
        }
        try {
            LocalDateTime res = LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("✅ [PARSEADO] Fecha inicio procesada (Formato Completo): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDateTime res = LocalDateTime.parse(fecha);
            log.info("✅ [PARSEADO] Fecha inicio procesada (ISO): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDate localDate = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime res = localDate.atStartOfDay();
            log.info("✅ [PARSEADO] Fecha inicio procesada (Solo Fecha -> 00:00:00): {}", res);
            return res;
        } catch (DateTimeParseException e) {
            log.error("❌ [FORMATO INVALIDO] No se pudo parsear la fecha de inicio: {}", fecha);
            throw new IllegalArgumentException("Formato de fecha de inicio invalido. Use yyyy-MM-dd o yyyy-MM-dd HH:mm:ss");
        }
    }

    public LocalDateTime parsearFechaFin(String fecha) {
        log.info("📅 [FECHA] Intentando parsear fecha de fin: {}", fecha);

        if (fecha == null || fecha.isBlank()) {
            log.error("⚠️ [ERROR] La fecha de fin recibida es nula o vacia");
            throw new IllegalArgumentException("La fecha de fin es obligatoria");
        }
        try {
            LocalDateTime res = LocalDateTime.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("✅ [PARSEADO] Fecha fin procesada (Formato Completo): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDateTime res = LocalDateTime.parse(fecha);
            log.info("✅ [PARSEADO] Fecha fin procesada (ISO): {}", res);
            return res;
        } catch (DateTimeParseException ignored) {}

        try {
            LocalDate localDate = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime res = localDate.atTime(23, 59, 59);
            log.info("✅ [PARSEADO] Fecha fin procesada (Solo Fecha -> 23:59:59): {}", res);
            return res;
        } catch (DateTimeParseException e) {
            log.error("❌ [FORMATO INVALIDO] No se pudo parsear la fecha de fin: {}", fecha);
            throw new IllegalArgumentException("Formato de fecha de fin invalido. Use yyyy-MM-dd o yyyy-MM-dd HH:mm:ss");
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
