package com.cloud.jml.utils;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.model.ProveedorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component // 🔹 Anotación para indicar que es un componente de Spring
public class ProveedorMapper {

    public ProveedorMapper() {
        // Constructor
    }

    // ------------------ 🔹 Métodos de Mapeos ------------------

    public ProveedorEntity mapRequestDtoToEntity(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Iniciando mapeo DTO a Entity para crear Proveedor");

        ProveedorEntity proveedorEntity = new ProveedorEntity();

        proveedorEntity.setCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());
        proveedorEntity.setNombre(proveedorRequestDTO.getNombre());
        proveedorEntity.setTelefono(proveedorRequestDTO.getTelefono());
        proveedorEntity.setDireccion(proveedorRequestDTO.getDireccion());
        proveedorEntity.setCorreo(proveedorRequestDTO.getCorreo());
        proveedorEntity.setFechaCreacion(LocalDateTime.now());

        log.info("📌 Finalizando mapeo DTO a Entity para crear Proveedor");

        return proveedorEntity;
    }

    public ProveedorResponseDTO mapEntityToResponseDto(ProveedorEntity proveedorEntity) {
        log.info("📌 Iniciando mapeo Entity a DTO para crear Proveedor");

        ProveedorResponseDTO proveedorResponseDTO = new ProveedorResponseDTO();

        proveedorResponseDTO.setCodigoSucursal(proveedorEntity.getCodigoSucursal());
        proveedorResponseDTO.setNombre(proveedorEntity.getNombre());
        proveedorResponseDTO.setTelefono(proveedorEntity.getTelefono());
        proveedorResponseDTO.setDireccion(proveedorEntity.getDireccion());
        proveedorResponseDTO.setCorreo(proveedorEntity.getCorreo());
        proveedorResponseDTO.setFechaCreacion(proveedorEntity.getFechaCreacion());
        proveedorResponseDTO.setFechaActualizacion(proveedorEntity.getFechaActualizacion());

        log.info("📌 Finalizando mapeo Entity a DTO para crear Proveedor");

        return proveedorResponseDTO;
    }
}
