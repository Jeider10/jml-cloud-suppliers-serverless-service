package com.cloud.jml.service;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.exception.ProveedorDuplicadoException;
import com.cloud.jml.exception.ProveedorNoEncontradoException;
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

    @Transactional
    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Inicio de creación de Proveedor: {}", proveedorRequestDTO.getNombre());

        // Verificar si ya existe por códigoSucursal
        Optional<ProveedorEntity> byCodigoSucursal = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());
        if (byCodigoSucursal.isPresent()) {
            log.warn("⚠️ Proveedor duplicado: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorDuplicadoException(proveedorRequestDTO.getCodigoSucursal());
        }

        // Mapeo de DTO a Entity
        ProveedorEntity proveedorEntity = mapper.mapRequestDtoToEntity(proveedorRequestDTO);

        // Guardamos en la base de datos
        ProveedorEntity guardado = proveedorRepository.save(proveedorEntity);
        log.info("✅ Proveedor guardado con ID: {}", guardado.getId());

        ProveedorResponseDTO proveedorResponseDTO = mapper.mapEntityToResponseDto(guardado);
        log.info("📌 Finaliza creación de Proveedor: {}", proveedorResponseDTO.getNombre());

        return proveedorResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listarProveedores() {

        // Paso 1: Obtener entidades desde la BD
        List<ProveedorEntity> proveedoresEntity = proveedorRepository.findAll();

        // Paso 2: Convertir a Stream
        Stream<ProveedorEntity> streamProveedores = proveedoresEntity.stream();

        // Paso 3: Mapear cada entidad a DTO
        Stream<ProveedorResponseDTO> streamDto = streamProveedores.map(mapper::mapEntityToResponseDto);

        // Paso 4: Convertir a lista final
        List<ProveedorResponseDTO> proveedoresResponse = streamDto.toList();

        log.info("📌 Finaliza búsqueda de todos los Proveedores. Total encontrados: {}", proveedoresResponse.size());
        return proveedoresResponse;
    }

    @Transactional(readOnly = true)
    public Optional<ProveedorResponseDTO> obtenerProveedorPorCodigoSucursal(Long codigoSucursal) {
        log.info("📌 Inicio de búsqueda de Proveedor por Codigo de Sucursal: {}", codigoSucursal);

        Optional<ProveedorEntity> optionalProveedorEntity = proveedorRepository.findByCodigoSucursal(codigoSucursal);

        if (optionalProveedorEntity.isPresent()) {
            Optional<ProveedorResponseDTO> proveedorResponseDTO = Optional.of(mapper.mapEntityToResponseDto(optionalProveedorEntity.get()));
            log.info("✅ Proveedor encontrado con Codigo de Sucursal: {}", codigoSucursal);
            return proveedorResponseDTO;
        } else {
            Optional<ProveedorResponseDTO> responseDTO = Optional.empty();
            log.info("⚠️ Proveedor no encontrado con Codigo de Sucursal: {}", codigoSucursal);
            return responseDTO;
        }
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerProveedorPorNombre(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Inicio de búsqueda de Proveedor por nombre: {}", proveedorRequestDTO.getNombre());

        // Paso 1: Buscar entidades por nombre
        List<ProveedorEntity> proveedoresEntity = proveedorRepository.findByNombreContainingIgnoreCase(proveedorRequestDTO.getNombre());

        // Paso 2: Validar si está vacío
        if (proveedoresEntity.isEmpty()) {
            log.warn("⚠️ No se encontraron proveedores con nombre: {}", proveedorRequestDTO.getNombre());
            return List.of(); // Retorna lista vacía
        }

        // Paso 3: Convertir a Stream
        Stream<ProveedorEntity> streamProveedores = proveedoresEntity.stream();

        // Paso 4: Mapear cada entidad a DTO
        Stream<ProveedorResponseDTO> streamDto = streamProveedores.map(mapper::mapEntityToResponseDto);

        // Paso 5: Convertir a lista final
        List<ProveedorResponseDTO> proveedoresResponse = streamDto.toList();

        log.info("📌 Finaliza búsqueda de Proveedor por nombre: {}. Total encontrados: {}",
                proveedorRequestDTO.getNombre(), proveedoresResponse.size());
        return proveedoresResponse;
    }

    @Transactional
    public ProveedorResponseDTO actualizarProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Inicio de actualización de Proveedor: {} con Codigo de Sucursal: {}",
                proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());

        // Paso 1: Validar existencia
        ProveedorEntity proveedorEntity = proveedorUtils.validarExistenciaProveedor(proveedorRequestDTO);

        // Paso 2: Actualizar datos
        proveedorUtils.actualizarDatosProveedor(proveedorRequestDTO, proveedorEntity);

        // Paso 3: Guardar cambios en la BD
        ProveedorEntity actualizado = proveedorRepository.save(proveedorEntity);
        log.info("✅ Proveedor actualizado con Codigo de Sucursal: {}", actualizado.getCodigoSucursal());

        // Paso 4: Mapear a DTO
        ProveedorResponseDTO proveedorResponseDTO = mapper.mapEntityToResponseDto(actualizado);
        log.info("📌 Finaliza actualización de Proveedor: {} con Codigo de Sucursal: {}",
                proveedorResponseDTO.getNombre(), proveedorResponseDTO.getCodigoSucursal());

        return proveedorResponseDTO;
    }

    @Transactional
    public void eliminarProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Inicio de eliminación de Proveedor con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        Optional<ProveedorEntity> proveedorOptional = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (proveedorOptional.isPresent()) {
            ProveedorEntity proveedorEntity = proveedorOptional.get();
            proveedorRepository.delete(proveedorEntity);
            log.info("✅ Proveedor eliminado con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
        } else {
            log.warn("⚠️ Proveedor no encontrado con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorNoEncontradoException(proveedorRequestDTO.getCodigoSucursal());
        }
    }
}
