package com.cloud.jml.controller;

import com.cloud.jml.dto.ProveedorDTO;
import com.cloud.jml.service.ProveedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/proveedores")
@CrossOrigin(origins = "http://localhost:8080")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    // Crear Proveedor
    @PostMapping("/register")
    public ResponseEntity<ProveedorDTO> crearProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        log.info("📌 Iniciando petición para crear Proveedor: {}", proveedorDTO.getNombre());

        ProveedorDTO response = proveedorService.crearProveedor(proveedorDTO);

        log.info("📌 Finaliza petición para crear Proveedor: {}", proveedorDTO.getNombre());

        return ResponseEntity.ok(response);
    }

    // Buscar Proveedor por NIC
    @GetMapping("/nic")
    public ResponseEntity<ProveedorDTO> obtenerProveedorPorNic(@RequestParam("nic") Long nic) {
        log.info("📌 Iniciando petición para buscar Proveedor por NIC: {}", nic);

        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setNic(nic);

        Optional<ProveedorDTO> response = proveedorService.obtenerProveedorPorNic(proveedorDTO);

        log.info("📌 Finaliza petición de buscar Proveedor por NIC: {}", nic);

        return response.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Buscar Proveedor por nombre
    @GetMapping("/nombre")
    public ResponseEntity<List<ProveedorDTO>> obtenerProveedorPorNombre(@RequestParam("nombre") String nombre) {
        log.info("📌 Iniciando petición para buscar Proveedor por nombre: {}", nombre);

        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setNombre(nombre);

        List<ProveedorDTO> response = proveedorService.obtenerProveedorPorNombre(proveedorDTO);

        log.info("📌 Finaliza petición de buscar Proveedor por nombre: {}", nombre);

        return response.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(response);
    }

    // Listar todos los Proveedores
    @GetMapping("/listar-proveedores")
    public ResponseEntity<List<ProveedorDTO>> listarProveedores() {
        log.info("📌 Iniciando petición para listar todos los Proveedores");

        List<ProveedorDTO> proveedores = proveedorService.listarProveedores();

        log.info("📌 Finaliza petición para listar todos los Proveedores");

        return ResponseEntity.ok(proveedores);
    }

    // Actualizar Proveedor
    @PutMapping("/actualizar")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(@RequestBody ProveedorDTO proveedorDTO) {
        log.info("📌 Iniciando petición para actualizar Proveedor con NIC: {}", proveedorDTO.getNic());

        try {
            ProveedorDTO response = proveedorService.actualizarProveedor(proveedorDTO);
            log.info("📌 Finaliza petición de actualización de Proveedor con NIC: {}", proveedorDTO.getNic());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.warn("⚠️ No se pudo actualizar el Proveedor. NIC no encontrado: {}", proveedorDTO.getNic());
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar Proveedor por NIC
    @DeleteMapping("/eliminar-nic")
    public ResponseEntity<Void> eliminarProveedor(@RequestParam("nic") Long nic) {
        log.info("📌 Iniciando petición para eliminar Proveedor con NIC: {}", nic);

        ProveedorDTO proveedorDTO = new ProveedorDTO();
        proveedorDTO.setNic(nic);

        try {
            proveedorService.eliminarProveedor(proveedorDTO);
            log.info("📌 Finalizó petición de eliminación de Proveedor con NIC: {}", proveedorDTO.getNic());
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            log.warn("⚠️ Error al eliminar Proveedor: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
