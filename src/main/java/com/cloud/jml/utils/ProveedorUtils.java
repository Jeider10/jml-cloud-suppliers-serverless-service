package com.cloud.jml.utils;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.exception.ProveedorNoEncontradoException;
import com.cloud.jml.model.ProveedorEntity;
import com.cloud.jml.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component  // 🔹 Anotación para indicar que es un componente de Spring
public class ProveedorUtils {

    private final ProveedorRepository proveedorRepository;

    public ProveedorUtils(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
        log.info("🔥 ProveedorUtils inicializado correctamente.");
    }

    public ProveedorEntity validarExistenciaProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        Optional<ProveedorEntity> optionalProveedor = proveedorRepository.findByCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());

        if (optionalProveedor.isPresent()) {
            log.info("📌 Proveedor encontrado con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            return optionalProveedor.get();
        } else {
            log.warn("⚠️ Proveedor no encontrado con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            throw new ProveedorNoEncontradoException(proveedorRequestDTO.getCodigoSucursal());
        }
    }

    public void actualizarDatosProveedor(ProveedorRequestDTO proveedorRequestDTO, ProveedorEntity proveedorEntity) {
        // Actualizamos solo los campos permitidos
        proveedorEntity.setNombre(proveedorRequestDTO.getNombre());
        proveedorEntity.setCorreo(proveedorRequestDTO.getCorreo());
        proveedorEntity.setTelefono(proveedorRequestDTO.getTelefono());
        proveedorEntity.setDireccion(proveedorRequestDTO.getDireccion());

        // Actualizamos la fecha de actualización
        proveedorEntity.setFechaActualizacion(LocalDateTime.now());
    }
}
