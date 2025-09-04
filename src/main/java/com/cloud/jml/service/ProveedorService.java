package com.cloud.jml.service;

import com.cloud.jml.dto.ProveedorDTO;
import com.cloud.jml.exception.ProveedorDuplicadoException;
import com.cloud.jml.exception.ProveedorNoEncontradoException;
import com.cloud.jml.model.ProveedorEntity;
import com.cloud.jml.repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
        log.info("🔥 ProveedorService inicializado correctamente.");
    }

    @Transactional
    public ProveedorDTO crearProveedor(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de creación de Proveedor: {}", proveedorDTO.getNombre());

        // Verificar si ya existe por identificación
        Optional<ProveedorDTO> existente = obtenerProveedorPorNic(proveedorDTO);
        if (existente.isPresent()) {
            log.warn("⚠️ Proveedor duplicado: {}", proveedorDTO.getNic());
            throw new ProveedorDuplicadoException(proveedorDTO.getNic());
        }

        // Mapeo de DTO a Entity
        ProveedorEntity proveedorEntity = mapDtoToEntity(proveedorDTO);

        // Guardamos en la base de datos
        ProveedorEntity guardado = proveedorRepository.save(proveedorEntity);
        log.info("✅ Proveedor guardado con ID: {}", guardado.getId());

        return mapEntityToDto(guardado);
    }

    @Transactional
    public Optional<ProveedorDTO> obtenerProveedorPorNic(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de búsqueda de Proveedor por NIC: {}", proveedorDTO.getNic());

        Optional<ProveedorDTO> optionalProveedorDTO = proveedorRepository.findByNic(proveedorDTO.getNic())
                .map(this::mapEntityToDto);

        log.info("📌 Finaliza búsqueda de Proveedor por NIC: {}", proveedorDTO.getNic());
        return optionalProveedorDTO;
    }

    @Transactional
    public List<ProveedorDTO> obtenerProveedorPorNombre(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de búsqueda de Proveedor por nombre: {}", proveedorDTO.getNombre());

        List<ProveedorEntity> proveedores = proveedorRepository.findByNombre(proveedorDTO.getNombre());

        List<ProveedorDTO> proveedorName = proveedores.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        log.info("📌 Finaliza búsqueda de Proveedor por nombre: {}", proveedorDTO.getNombre());
        return proveedorName;
    }

    @Transactional
    public List<ProveedorDTO> listarProveedores() {
        log.info("📌 Inicio de búsqueda de todos los Proveedores");

        List<ProveedorDTO> proveedores = proveedorRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        log.info("📌 Finaliza búsqueda de todos los Proveedores");
        return proveedores;
    }

    @Transactional
    public ProveedorDTO actualizarProveedor(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de actualización de Proveedor: {} con NIC: {}", proveedorDTO.getNombre(), proveedorDTO.getNic());

        ProveedorEntity proveedorEntity = validarExistenciaProveedor(proveedorDTO);

        actualizarDatosProveedor(proveedorDTO, proveedorEntity);

        ProveedorEntity actualizado = proveedorRepository.save(proveedorEntity);
        log.info("✅ Proveedor actualizado con NIC: {}", actualizado.getNic());

        return mapEntityToDto(actualizado);
    }

    @Transactional
    public void eliminarProveedor(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de eliminación de Proveedor con NIC: {}", proveedorDTO.getNic());

        // Verificar si ya existe por NIC
        ProveedorEntity proveedorEntity = validarExistenciaProveedor(proveedorDTO);

        proveedorRepository.delete(proveedorEntity);

        log.info("✅ Proveedor eliminado con NIC: {}", proveedorDTO.getNic());
    }

    // ------------------ 🔹 Métodos privados de Mapeos ------------------

    private ProveedorEntity mapDtoToEntity(ProveedorDTO proveedorDTO) {
        log.info("📌 Iniciando mapeo DTO a Entity para crear cliente");

        ProveedorEntity proveedorEntity = new ProveedorEntity();

        proveedorEntity.setNic(proveedorDTO.getNic());
        proveedorEntity.setNombre(proveedorDTO.getNombre());
        proveedorEntity.setTelefono(proveedorDTO.getTelefono());
        proveedorEntity.setDireccion(proveedorDTO.getDireccion());
        proveedorEntity.setCorreo(proveedorDTO.getCorreo());
        proveedorEntity.setFechaCreacion(LocalDateTime.now());

        log.info("📌 Finalizando mapeo DTO a Entity para crear cliente");

        return proveedorEntity;
    }

    private ProveedorDTO mapEntityToDto(ProveedorEntity proveedorEntity) {
        log.info("📌 Iniciando mapeo Entity a DTO para crear cliente");

        ProveedorDTO proveedorDTO = new ProveedorDTO();

        proveedorDTO.setNic(proveedorEntity.getNic());
        proveedorDTO.setNombre(proveedorEntity.getNombre());
        proveedorDTO.setTelefono(proveedorEntity.getTelefono());
        proveedorDTO.setDireccion(proveedorEntity.getDireccion());
        proveedorDTO.setCorreo(proveedorEntity.getCorreo());
        proveedorDTO.setFechaCreacion(proveedorEntity.getFechaCreacion());
        proveedorDTO.setFechaActualizacion(proveedorEntity.getFechaActualizacion());

        log.info("📌 Finalizando mapeo Entity a DTO para crear cliente");

        return proveedorDTO;
    }

    private void actualizarDatosProveedor(ProveedorDTO proveedorDTO, ProveedorEntity proveedorEntity) {
        // Actualizamos solo los campos permitidos
        proveedorEntity.setNombre(proveedorDTO.getNombre());
        proveedorEntity.setCorreo(proveedorDTO.getCorreo());
        proveedorEntity.setTelefono(proveedorDTO.getTelefono());
        proveedorEntity.setDireccion(proveedorDTO.getDireccion());

        // Actualizamos la fecha de actualización
        proveedorEntity.setFechaActualizacion(LocalDateTime.now());
    }

    private ProveedorEntity validarExistenciaProveedor(ProveedorDTO proveedorDTO) {
        ProveedorEntity proveedorEntity = proveedorRepository.findByNic(proveedorDTO.getNic())
                .orElseThrow(() -> new ProveedorNoEncontradoException(proveedorDTO.getNic()));

        log.info("📌 Proveedor encontrado con NIC: {}", proveedorDTO.getNic());
        return proveedorEntity;
    }
}
