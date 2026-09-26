package com.cloud.jml.service;

import com.cloud.jml.dto.ProveedorPapeleraResponseDTO;
import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.exception.proveedor.ProveedorDuplicadoException;
import com.cloud.jml.exception.proveedor.ProveedorNoEncontradoException;
import com.cloud.jml.model.ProveedorEntity;
import com.cloud.jml.repository.ProveedorRepository;
import com.cloud.jml.utils.proveedor.ProveedorMapper;
import com.cloud.jml.utils.proveedor.ProveedorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper mapper;
    private final ProveedorUtils proveedorUtils;

    public ProveedorService(ProveedorRepository proveedorRepository, ProveedorMapper mapper, ProveedorUtils proveedorUtils) {
        this.proveedorRepository = proveedorRepository;
        this.mapper = mapper;
        this.proveedorUtils = proveedorUtils;
        log.info("🔥 ProveedorService inicializado correctamente.");
    }

    // ─── Listar activos ───────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarProveedores() {
        log.info("🔍 [CONSULTA] Recuperando todos los proveedores activos");

        List<ProveedorEntity> entidades = proveedorRepository.findAllByEliminadoFalse();

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron proveedores activos");
            return List.of();
        }

        List<ProveedorResponseDTO> respuesta = entidades.stream()
                .map(mapper::mapEntityToResponseDto)
                .toList();

        log.info("✅ [FINALIZADO] Total de proveedores activos retornados: {}", respuesta.size());

        return respuesta;
    }

    // ─── Crear ────────────────────────────────────────────────────────────────
    @Transactional
    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [SOLICITUD] Creando proveedor: {}", proveedorRequestDTO.getNombre());

        Optional<ProveedorEntity> existente = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());
        if (existente.isPresent() && !existente.get().isEliminado()) {
            log.warn("❌ [DUPLICADO] Proveedor activo ya existe con codigo: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorDuplicadoException(proveedorRequestDTO.getCodigoSucursal());
        }

        ProveedorEntity entidad = mapper.mapRequestDtoToEntity(proveedorRequestDTO);
        ProveedorEntity guardado = proveedorUtils.guardarProveedorBD(entidad);

        log.info("💾 [PERSISTENCIA] Proveedor creado: {}", guardado.getCodigoSucursal());

        return mapper.mapEntityToResponseDto(guardado);
    }

    // ─── Buscar por codigo sucursal ───────────────────────────────────────────
    @Transactional(readOnly = true)
    public ProveedorResponseDTO obtenerProveedorPorCodigoSucursal(String codigoSucursal) {
        log.info("🔍 [CONSULTA] Buscando proveedor con codigo de sucursal: {}", codigoSucursal);

        ProveedorEntity entidad = proveedorRepository.findByCodigoSucursalAndEliminadoFalse(codigoSucursal)
                .orElseThrow(() -> {
                    log.warn("❌ [RESULTADO] Proveedor no encontrado: {}", codigoSucursal);
                    return new ProveedorNoEncontradoException(codigoSucursal);
                });

        log.info("✅ [FINALIZADO] Proveedor encontrado: {}", codigoSucursal);

        return mapper.mapEntityToResponseDto(entidad);
    }

    // ─── Buscar por nombre ────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorNombre(String nombre) {
        log.info("🔍 [CONSULTA] Buscando proveedores por nombre: {}", nombre);

        List<ProveedorEntity> entidades = proveedorRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron proveedores con nombre: {}", nombre);
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToResponseDto).toList();
    }

    // ─── Buscar por correo ────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorCorreo(String correo) {
        log.info("🔍 [CONSULTA] Buscando proveedores por correo: {}", correo);

        List<ProveedorEntity> entidades = proveedorRepository.findByCorreoContainingIgnoreCaseAndEliminadoFalse(correo);

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron proveedores con correo: {}", correo);
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToResponseDto).toList();
    }

    // ─── Buscar por creadoPor ─────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorCreadoPor(String creadoPor) {
        log.info("🔍 [CONSULTA] Buscando proveedores por creadoPor: {}", creadoPor);

        List<ProveedorEntity> entidades = proveedorRepository.findByCreadoPorContainingIgnoreCaseAndEliminadoFalse(creadoPor);

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron proveedores creados por: {}", creadoPor);
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToResponseDto).toList();
    }

    // ─── Buscar por fecha de creacion ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorFechaCreacion(String fechaInicio, String fechaFin) {
        log.info("🔍 [CONSULTA] Buscando proveedores por rango de fecha de creacion: {} - {}", fechaInicio, fechaFin);

        LocalDateTime inicio = proveedorUtils.parsearFechaInicio(fechaInicio);
        LocalDateTime fin = proveedorUtils.parsearFechaFin(fechaFin);

        List<ProveedorEntity> entidades = proveedorRepository.findByFechaCreacionBetweenAndEliminadoFalse(inicio, fin);

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron proveedores en el rango de fechas: {} - {}", inicio, fin);
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToResponseDto).toList();
    }

    // ─── Buscar por fecha de actualizacion ───────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorFechaActualizacion(String fechaInicio, String fechaFin) {
        log.info("🔍 [CONSULTA] Buscando proveedores por rango de fecha de actualizacion: {} - {}", fechaInicio, fechaFin);

        LocalDateTime inicio = proveedorUtils.parsearFechaInicio(fechaInicio);
        LocalDateTime fin = proveedorUtils.parsearFechaFin(fechaFin);

        List<ProveedorEntity> entidades = proveedorRepository.findByFechaActualizacionBetweenAndEliminadoFalse(inicio, fin);

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron proveedores en el rango de fecha de actualizacion.");
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToResponseDto).toList();
    }

    // ─── Actualizar ───────────────────────────────────────────────────────────
    @Transactional
    public ProveedorResponseDTO actualizarProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [SOLICITUD] Actualizando proveedor con codigo: {}", proveedorRequestDTO.getCodigoSucursal());

        ProveedorEntity entidad = proveedorUtils.validarExistenciaProveedor(proveedorRequestDTO);
        mapper.actualizarDatosProveedor(proveedorRequestDTO, entidad);
        ProveedorEntity actualizado = proveedorUtils.guardarProveedorBD(entidad);

        log.info("✅ [FINALIZADO] Proveedor actualizado: {}", actualizado.getCodigoSucursal());

        return mapper.mapEntityToResponseDto(actualizado);
    }

    // ─── Soft delete (a papelera) ─────────────────────────────────────────────
    @Transactional
    public void eliminarProveedor(String codigoSucursal, String eliminadoPorId, String eliminadoPorNombre) {
        log.info("🔍 [SOLICITUD] Enviando a papelera proveedor con codigo: {}", codigoSucursal);

        ProveedorEntity entidad = proveedorRepository.findByCodigoSucursalAndEliminadoFalse(codigoSucursal)
                .orElseThrow(() -> new ProveedorNoEncontradoException(codigoSucursal));

        entidad.setEliminado(true);
        entidad.setFechaEliminacion(LocalDateTime.now());
        entidad.setEliminadoPorId(eliminadoPorId);
        entidad.setEliminadoPorNombre(eliminadoPorNombre);

        proveedorUtils.guardarProveedorBD(entidad);

        log.info("🗑️ [PAPELERA] Proveedor {} enviado a papelera por: {}", codigoSucursal, eliminadoPorNombre);
    }

    // ─── Listar papelera ──────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorPapeleraResponseDTO> listarPapelera() {
        log.info("🔍 [CONSULTA] Listando proveedores en papelera");

        List<ProveedorEntity> entidades = proveedorRepository.findAllByEliminadoTrue();

        if (entidades.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No hay proveedores en papelera");
            return List.of();
        }

        List<ProveedorPapeleraResponseDTO> respuesta = entidades.stream()
                .map(mapper::mapEntityToPapeleraDto)
                .toList();

        log.info("✅ [FINALIZADO] Total de proveedores en papelera: {}", respuesta.size());

        return respuesta;
    }

    // ─── Restaurar desde papelera ─────────────────────────────────────────────
    @Transactional
    public ProveedorResponseDTO restaurarProveedor(String codigoSucursal) {
        log.info("🔍 [SOLICITUD] Restaurando proveedor con codigo: {}", codigoSucursal);

        ProveedorEntity entidad = proveedorRepository.findByCodigoSucursalAndEliminadoTrue(codigoSucursal)
                .orElseThrow(() -> {
                    log.warn("❌ [RESULTADO] Proveedor no encontrado en papelera: {}", codigoSucursal);
                    return new ProveedorNoEncontradoException(codigoSucursal);
                });

        entidad.setEliminado(false);
        entidad.setFechaEliminacion(null);
        entidad.setEliminadoPorId(null);
        entidad.setEliminadoPorNombre(null);
        entidad.setFechaActualizacion(LocalDateTime.now());

        ProveedorEntity restaurado = proveedorUtils.guardarProveedorBD(entidad);

        log.info("✅ [FINALIZADO] Proveedor restaurado: {}", restaurado.getCodigoSucursal());

        return mapper.mapEntityToResponseDto(restaurado);
    }

    // ─── Eliminar definitivamente ─────────────────────────────────────────────
    @Transactional
    public void eliminarDefinitivo(String codigoSucursal) {
        log.info("🔍 [SOLICITUD] Eliminando definitivamente proveedor con codigo: {}", codigoSucursal);

        ProveedorEntity entidad = proveedorRepository.findByCodigoSucursalAndEliminadoTrue(codigoSucursal)
                .orElseThrow(() -> {
                    log.warn("❌ [RESULTADO] Proveedor no encontrado en papelera: {}", codigoSucursal);
                    return new ProveedorNoEncontradoException(codigoSucursal);
                });

        proveedorUtils.eliminarProveedorBD(entidad);

        log.info("🗑️ [ELIMINADO] Proveedor eliminado definitivamente: {}", codigoSucursal);
    }

    // ─── Filtrar papelera por fecha de eliminacion ────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorPapeleraResponseDTO> listarPapeleraPorFecha(String fechaInicio, String fechaFin) {

        LocalDateTime inicio = proveedorUtils.parsearFechaInicio(fechaInicio);
        LocalDateTime fin = proveedorUtils.parsearFechaFin(fechaFin);

        List<ProveedorEntity> entidades = proveedorRepository.findByFechaEliminacionBetweenAndEliminadoTrue(inicio, fin);

        if (entidades.isEmpty()) {
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToPapeleraDto).toList();
    }

    // ─── Filtrar papelera por quien elimino ───────────────────────────────────
    @Transactional(readOnly = true)
    public List<ProveedorPapeleraResponseDTO> listarPapeleraPorEliminadoPor(String eliminadoPorId) {

        List<ProveedorEntity> entidades = proveedorRepository.findByEliminadoPorIdContainingIgnoreCaseAndEliminadoTrue(eliminadoPorId);

        if (entidades.isEmpty()) {
            return List.of();
        }

        return entidades.stream().map(mapper::mapEntityToPapeleraDto).toList();
    }
}
