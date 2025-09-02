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
        Optional<ProveedorEntity> existente = obtenerProveedorPorNic(proveedorDTO);
        if (existente.isPresent()) {
            log.warn("⚠️ Proveedor duplicado: {}", proveedorDTO.getNic());
            throw new ProveedorDuplicadoException(proveedorDTO.getNic());
        }

        // Mapeo de DTO a Entity
        ProveedorEntity proveedorEntity = mapDtoToEntity(proveedorDTO);
        log.debug("🔹 Proveedor mapeado a Entity: {}", proveedorEntity);

        // Guardamos en la base de datos
        ProveedorEntity guardado = proveedorRepository.save(proveedorEntity);
        log.info("✅ Proveedor guardado con ID: {}", guardado.getId());

        // Convertimos de nuevo a DTO
        ProveedorDTO response = mapEntityToDto(guardado);
        log.debug("🔹 Proveedor convertido nuevamente a DTO: {}", response);

        log.info("📌 Finalizó creación de Proveedor: {}", response.getNombre());

        return response;
    }

    @Transactional
    public Optional<ProveedorEntity> obtenerProveedorPorNic(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de búsqueda de Proveedor por NIC: {}", proveedorDTO.getNic());

        Optional<ProveedorEntity> byNic = proveedorRepository.findByNic(proveedorDTO.getNic());

        if (byNic.isPresent()) {
            log.info("✅ Proveedor encontrado con NIC: {}", byNic.get().getNic());
        } else {
            log.warn("⚠️ No se encontró Proveedor con NIC: {}", proveedorDTO.getNic());
        }

        log.info("📌 Finaliza búsqueda de Proveedor por NIC: {}", proveedorDTO.getNic());

        return byNic;
    }

    @Transactional
    public List<ProveedorEntity> obtenerProveedorPorNombre(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de búsqueda de Proveedor por nombre: {}", proveedorDTO.getNombre());

        List<ProveedorEntity> proveedores = proveedorRepository.findByNombre(proveedorDTO.getNombre());

        if (proveedores.isEmpty()) {
            log.warn("⚠️ No se encontró Proveedor con nombre: {}", proveedorDTO.getNombre());
        } else {
            log.info("✅ Se encontraron {} Proveedor(s) con el nombre: {}", proveedores.size(), proveedorDTO.getNombre());
        }

        log.info("📌 Finaliza búsqueda de cliente por nombre: {}", proveedorDTO.getNombre());

        return proveedores;
    }

    @Transactional
    public List<ProveedorEntity> listarProveedores() {
        log.info("📌 Inicio de búsqueda de todos los Proveedores");

        List<ProveedorEntity> allProveedor = proveedorRepository.findAll();

        log.info("✅ Se encontraron {} Proveedores", allProveedor.size());

        return allProveedor;
    }

    @Transactional
    public ProveedorDTO actualizarProveedor(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de actualización de Proveedor: {} con NIC: {}", proveedorDTO.getNombre(), proveedorDTO.getNic());

        Optional<ProveedorEntity> proveedorOpt = obtenerProveedorPorNic(proveedorDTO);

        if (proveedorOpt.isEmpty()) {
            log.warn("⚠️ No se encontró Proveedor con NIC: {}", proveedorDTO.getNic());
            throw new ProveedorNoEncontradoException(proveedorDTO.getNic());
        }

        // Verificar si ya existe por NIC
        ProveedorEntity proveedorEntity = proveedorOpt.get();

        actualizarDatosProveedor(proveedorDTO, proveedorEntity);

        ProveedorEntity actualizado = proveedorRepository.save(proveedorEntity);
        log.info("✅ Proveedor actualizado con NIC: {}", actualizado.getNic());

        ProveedorDTO response = mapEntityToDto(actualizado);
        log.debug("🔹 Proveedor actualizado convertido a DTO: {}", response);
        log.info("📌 Finalizó actualización de Proveedor: {} con NIC: {}", response.getNombre(), response.getNic());

        return response;
    }

    @Transactional
    public void eliminarProveedor(ProveedorDTO proveedorDTO) {
        log.info("📌 Inicio de eliminación de Proveedor con NIC: {}", proveedorDTO.getNic());

        // Verificar si ya existe por NIC
        ProveedorEntity proveedorEntity = validarExistenciaProveedor(proveedorDTO);

        proveedorRepository.delete(proveedorEntity);

        log.info("✅ Cliente eliminado con identificacion: {}", proveedorDTO.getNic());
    }

    @Transactional
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
        Optional<ProveedorEntity> proveedorOpt = obtenerProveedorPorNic(proveedorDTO);

        if (proveedorOpt.isPresent()) {
            log.info("✅ Proveedor: {} encontrado con NIC: {}", proveedorDTO.getNombre(), proveedorDTO.getNic());
            return proveedorOpt.get();
        } else {
            log.warn("⚠️ No se encontró Proveedor: {} con NIC: {}", proveedorDTO.getNombre(), proveedorDTO.getNic());
            throw new ProveedorNoEncontradoException(proveedorDTO.getNic());
        }
    }
}
