package com.cloud.jml.utils.proveedor;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.model.ProveedorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component // 🔹 Anotacion para indicar que es un componente de Spring
public class ProveedorMapper {

    private final ProveedorFormatearFecha proveedorFormatearFecha;

    public ProveedorMapper(ProveedorFormatearFecha proveedorFormatearFecha) {
        this.proveedorFormatearFecha = proveedorFormatearFecha;
        log.info("🔥 ProveedorMapper inicializado correctamente.");
    }

    public ProveedorEntity mapRequestDtoToEntity(ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📦 [MAPEO] Iniciando mapeo DTO → Entity para proveedor");

        ProveedorEntity proveedorEntity = new ProveedorEntity();

        proveedorEntity.setCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());
        proveedorEntity.setNombre(proveedorRequestDTO.getNombre());
        proveedorEntity.setTelefono(proveedorRequestDTO.getTelefono());
        proveedorEntity.setDireccion(proveedorRequestDTO.getDireccion());
        proveedorEntity.setCorreo(proveedorRequestDTO.getCorreo());
        proveedorEntity.setFechaCreacion(LocalDateTime.now());

        log.info("✅ [MAPEO] Mapeo completado DTO → Entity para proveedor");

        return proveedorEntity;
    }

    public ProveedorResponseDTO mapEntityToResponseDto(ProveedorEntity proveedorEntity) {
        log.info("📦 [MAPEO] Iniciando mapeo Entity → DTO para Proveedor");

        ProveedorResponseDTO proveedorResponseDTO = new ProveedorResponseDTO();

        proveedorResponseDTO.setCodigoSucursal(proveedorEntity.getCodigoSucursal());
        proveedorResponseDTO.setNombre(proveedorEntity.getNombre());
        proveedorResponseDTO.setTelefono(proveedorEntity.getTelefono());
        proveedorResponseDTO.setDireccion(proveedorEntity.getDireccion());
        proveedorResponseDTO.setCorreo(proveedorEntity.getCorreo());

        // 🕓 Formateo de fechas
        proveedorFormatearFecha.asignarFechasFormateadas(proveedorEntity, proveedorResponseDTO);

        log.info("✅ [MAPEO] Mapeo completado Entity → DTO para Proveedor");

        return proveedorResponseDTO;
    }

    public void actualizarDatosProveedor(ProveedorRequestDTO proveedorRequestDTO, ProveedorEntity proveedorEntity) {
        log.info("✏️ [SOLICITUD] Actualizando proveedor existente: codigo de sucursal={}", proveedorEntity.getCodigoSucursal());

        // Actualizamos solo los campos permitidos
        proveedorEntity.setCodigoSucursal(proveedorRequestDTO.getCodigoSucursal());
        proveedorEntity.setNombre(proveedorRequestDTO.getNombre());
        proveedorEntity.setCorreo(proveedorRequestDTO.getCorreo());
        proveedorEntity.setTelefono(proveedorRequestDTO.getTelefono());
        proveedorEntity.setDireccion(proveedorRequestDTO.getDireccion());

        // Actualizamos la fecha de actualizacion
        proveedorEntity.setFechaActualizacion(LocalDateTime.now());

        log.info("✅ [FINALIZADO] Proveedor actualizado correctamente: codigo de sucursal={}", proveedorEntity.getCodigoSucursal());
    }
}
