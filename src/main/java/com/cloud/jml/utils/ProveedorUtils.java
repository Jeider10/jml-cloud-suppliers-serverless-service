package com.cloud.jml.utils;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.exception.ProveedorNoEncontradoException;
import com.cloud.jml.model.ProveedorEntity;
import com.cloud.jml.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Component  // 🔹 Anotación para indicar que es un componente de Spring
public class ProveedorUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("d/M/yyyy, h:mm:ss a", Locale.of("es", "CO"));

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

    public String formatearFecha(LocalDateTime fecha) {
        String fechaFormateada = fecha.format(FORMATTER).toLowerCase();
        log.info("📌 Fecha formateada originalmente: {}", fechaFormateada);

        // Reemplazar y reasignar el valor "a. m." → "a.m." y "p. m." → "p.m."
        fechaFormateada = fechaFormateada
                .replace("a. m.", "a.m.")
                .replace("p. m.", "p.m.");

        log.info("📌 Fecha formateada final: {}", fechaFormateada);

        return fechaFormateada;
    }

    public void asignarFechasFormateadas(ProveedorEntity proveedorEntity, ProveedorResponseDTO proveedorResponseDTO) {
        if (proveedorEntity.getFechaCreacion() != null) {
            String fechaCreacion = formatearFecha(proveedorEntity.getFechaCreacion());
            log.info("📌 Fecha creación formateada: {}", fechaCreacion);

            proveedorResponseDTO.setFechaCreacion(fechaCreacion);
        } else {
            proveedorResponseDTO.setFechaCreacion(null);
        }

        if (proveedorEntity.getFechaActualizacion() != null) {
            String fechaActualizacion = formatearFecha(proveedorEntity.getFechaActualizacion());
            log.info("📌 Fecha actualización formateada: {}", fechaActualizacion);

            proveedorResponseDTO.setFechaActualizacion(fechaActualizacion);
        } else {
            proveedorResponseDTO.setFechaActualizacion(null);
        }
    }
}
