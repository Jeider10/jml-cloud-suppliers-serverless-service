package com.cloud.jml.service;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.exception.proveedor.ProveedorDuplicadoException;
import com.cloud.jml.exception.proveedor.ProveedorNoEncontradoException;
import com.cloud.jml.model.ProveedorEntity;
import com.cloud.jml.repository.ProveedorRepository;
import com.cloud.jml.utils.ProveedorMapper;
import com.cloud.jml.utils.ProveedorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarProveedores() {
        log.info("🔍 [CONSULTA] Recuperando todos los proveedores desde la base de datos");

        List<ProveedorEntity> proveedoresEntity = proveedorRepository.findAll();

        if (proveedoresEntity.isEmpty()) {
            log.warn("⚠️ [RESULTADO] No se encontraron Proveedores registrados en la base de datos");
            return List.of();
        }

        log.info("📦 [MAPEO] Transformando {} entidades de proveedores a DTOs", proveedoresEntity.size());

        // convertir a stream
        Stream<ProveedorEntity> streamProveedores = proveedoresEntity.stream();

        // mapear entidades a DTOs
        Stream<ProveedorResponseDTO> streamDto = streamProveedores.map(mapper::mapEntityToResponseDto);

        // recolectar en lista
        List<ProveedorResponseDTO> proveedoresResponse = streamDto.toList();

        log.info("✅ [FINALIZADO] Total de proveedores mapeados y retornados: {}", proveedoresResponse.size());

        return proveedoresResponse;
    }

    @Transactional
    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [CONSULTA] Inicio de creación de proveedor: {}", proveedorRequestDTO.getNombre());

        Optional<ProveedorEntity> proveedorExistente = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (proveedorExistente.isPresent()) {
            log.warn("❌ [ERROR] Proveedor duplicado detectado: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorDuplicadoException(proveedorRequestDTO.getCodigoSucursal());
        }

        log.info("📦 [MAPEO] Transformando DTO a entidad de proveedor");
        ProveedorEntity proveedorEntity = mapper.mapRequestDtoToEntity(proveedorRequestDTO);
        log.info("📦 [MAPEO] Proveedor: {} mapeado a entidad con código de sucursal: {}", proveedorEntity.getNombre(), proveedorEntity.getCodigoSucursal());

        ProveedorEntity guardarProveedor = proveedorUtils.guardarProveedorBD(proveedorEntity);
        log.info("💾 [PERSISTENCIA] Proveedor: {} guardado exitosamente con código de sucursal: {}", guardarProveedor.getNombre(), guardarProveedor.getCodigoSucursal());

        log.info("📦 [MAPEO] Transformando entidad de proveedor a DTO. (crearProveedor)");
        ProveedorResponseDTO proveedorResponseDTO = mapper.mapEntityToResponseDto(guardarProveedor);
        log.info("📦 [MAPEO] Proveedor: {} mapeado a DTO con código de sucursal: {}",
                proveedorResponseDTO.getNombre(), proveedorResponseDTO.getCodigoSucursal());

        log.info("✅ [FINALIZADO] Proveedor creado exitosamente: {} con código de sucursal: {}", proveedorResponseDTO.getNombre(), proveedorResponseDTO.getCodigoSucursal());

        return proveedorResponseDTO;
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDTO obtenerProveedorPorCodigoSucursal(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [CONSULTA] Iniciando búsqueda de proveedor por código de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        Optional<ProveedorEntity> optionalProveedor = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (optionalProveedor.isEmpty()) {
            log.warn("❌ [RESULTADO] Proveedor no encontrado con código de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            return null;
        }

        ProveedorEntity proveedorEntity = optionalProveedor.get();
        log.info("📦 [ENCONTRADO] Proveedor encontrado -> código de sucursal: {}, nombre: {}",
                proveedorEntity.getCodigoSucursal(), proveedorEntity.getNombre());

        log.info("📦 [MAPEO] Transformando entidad de proveedor a DTO. (obtenerProveedorPorCodigoSucursal)");
        ProveedorResponseDTO proveedorResponseDTO = mapper.mapEntityToResponseDto(proveedorEntity);
        log.info("📦 [MAPEO] Proveedor mapeado a DTO. código de sucursal: {}", proveedorResponseDTO.getCodigoSucursal());

        log.info("✅ [FINALIZADO] Proveedor encontrado y mapeado a DTO. código de sucursal: {}", proveedorResponseDTO.getCodigoSucursal());

        return proveedorResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorNombre(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [CONSULTA] Iniciando búsqueda de proveedor por nombre: {}", proveedorRequestDTO.getNombre());

        List<ProveedorEntity> proveedorEntity = proveedorRepository.findByNombreContainingIgnoreCase(proveedorRequestDTO.getNombre());

        if (proveedorEntity.isEmpty()) {
            log.warn("❌ [RESULTADO] No se encontraron proveedores con nombre: {}", proveedorRequestDTO.getNombre());
            return List.of();
        }

        log.info("📦 [MAPEO] Transformando {} entidades de proveedores a DTOs. (nombre: {})", proveedorEntity.size(), proveedorRequestDTO.getNombre());

        // convertir a stream
        Stream<ProveedorEntity> streamProveedores = proveedorEntity.stream();

        // mapear entidades a DTOs
        Stream<ProveedorResponseDTO> streamDto = streamProveedores.map(mapper::mapEntityToResponseDto);

        // recolectar en lista
        List<ProveedorResponseDTO> proveedorResponse = streamDto.toList();

        log.info("✅ [FINALIZADO] Proveedores encontrados con nombre: {}. Total encontrados: {}", proveedorRequestDTO.getNombre(), proveedorResponse.size());

        return proveedorResponse;
    }

    @Transactional
    public ProveedorResponseDTO actualizarProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [CONSULTA] Inicio de actualización de proveedor: {} con código de sucursal: {}",
                proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());

        // Paso 1: Validar existencia
        ProveedorEntity proveedorEntity = proveedorUtils.validarExistenciaProveedor(proveedorRequestDTO);

        // Paso 2: Actualizar datos
        mapper.actualizarDatosProveedor(proveedorRequestDTO, proveedorEntity);

        // Paso 3: Guardar cambios en la BD
        ProveedorEntity actualizado = proveedorUtils.guardarProveedorBD(proveedorEntity);
        log.info("💾 [PERSISTENCIA] Proveedor actualizado: {} con código de sucursal: {}", actualizado.getNombre(), actualizado.getCodigoSucursal());

        // Paso 4: Mapear a DTO
        log.info("📦 [MAPEO] Transformando entidad de proveedor a DTO. (actualizarProveedor)");
        ProveedorResponseDTO proveedorResponseDTO = mapper.mapEntityToResponseDto(actualizado);
        log.info("📦 [MAPEO] Proveedor mapeado a DTO. código de sucursal: {}, nombre: {}, dirección: {}",
                proveedorResponseDTO.getCodigoSucursal(), proveedorResponseDTO.getNombre(), proveedorResponseDTO.getDireccion());

        log.info("✅ [FINALIZADO] Actualización de proveedor completada: {} con código de sucursal: {}", proveedorResponseDTO.getNombre(), proveedorResponseDTO.getCodigoSucursal());

        return proveedorResponseDTO;
    }

    @Transactional
    public void eliminarProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("🔍 [CONSULTA] Inicio de eliminación de proveedor con código de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        Optional<ProveedorEntity> proveedorExistente = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (proveedorExistente.isPresent()) {
            ProveedorEntity proveedorEntity = proveedorExistente.get();
            log.info("📦 [ENCONTRADO] Proveedor encontrado: {} con código de sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());

            proveedorUtils.eliminarProveedorBD(proveedorEntity);
            log.info("🗑️ [ELIMINADO] Proveedor eliminado correctamente -> {} con código de sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());
        } else {
            log.warn("❌ [NO ENCONTRADO] Proveedor no encontrado con código de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorNoEncontradoException(proveedorRequestDTO.getCodigoSucursal());
        }
    }
}
