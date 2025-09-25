package com.cloud.jml.controller;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.exception.ProveedorNoEncontradoException;
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
        log.info("🔥 ProveedorController inicializado correctamente.");
    }

    @PostMapping("/register")
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Iniciando petición para crear Proveedor: {}", proveedorRequestDTO.getNombre());

        ProveedorResponseDTO response = proveedorService.crearProveedor(proveedorRequestDTO);

        log.info("📌 Finaliza petición para crear Proveedor: {}", proveedorRequestDTO.getNombre());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar-proveedores")
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores() {
        log.info("📌 Iniciando petición para listar todos los Proveedores");

        List<ProveedorResponseDTO> proveedores = proveedorService.listarProveedores();

        log.info("📌 Finaliza petición para listar todos los Proveedores");

        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/codigoSucursal")
    public ResponseEntity<ProveedorResponseDTO> obtenerProveedorPorCodigoSucursal(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📌 Iniciando petición para buscar Proveedor por Código de Sucursal: {}", codigoSucursal);

        Optional<ProveedorResponseDTO> response = proveedorService.obtenerProveedorPorCodigoSucursal(codigoSucursal);

        ResponseEntity<ProveedorResponseDTO> proveedorResponse;

        if (response.isPresent()) {
            proveedorResponse = ResponseEntity.ok(response.get());
            log.info("✅ Proveedor encontrado con Código de Sucursal: {}", codigoSucursal);
        } else {
            proveedorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("⚠️ Proveedor no encontrado con Código de Sucursal: {}", codigoSucursal);
        }

        log.info("📌 Finaliza petición de buscar Proveedor por Código de Sucursal: {}", codigoSucursal);
        return proveedorResponse;
    }

    @GetMapping("/nombre")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorNombre(@RequestParam("nombre") String nombre) {
        log.info("📌 Iniciando petición para buscar Proveedor por nombre: {}", nombre);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setNombre(nombre);

        List<ProveedorResponseDTO> response = proveedorService.obtenerProveedorPorNombre(proveedorRequestDTO);

        ResponseEntity<List<ProveedorResponseDTO>> proveedorResponse;
        if (response.isEmpty()) {
            proveedorResponse = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            log.warn("⚠️ No se encontraron proveedores con nombre: {}", nombre);
        } else {
            proveedorResponse = ResponseEntity.ok(response);
            log.info("✅ Proveedores encontrados con nombre: {}. Total: {}", nombre, response.size());
        }

        log.info("📌 Finaliza petición de buscar Proveedor por nombre: {}", nombre);
        return proveedorResponse;
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📌 Iniciando petición para actualizar Proveedor con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        ProveedorResponseDTO response;

        try {
            response = proveedorService.actualizarProveedor(proveedorRequestDTO);
            log.info("📌 Finaliza petición de actualización de Proveedor con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            return ResponseEntity.ok(response);
        } catch (ProveedorNoEncontradoException ex) {
            log.warn("⚠️ No se pudo actualizar el Proveedor. Codigo de Sucursal no encontrado: {}", proveedorRequestDTO.getCodigoSucursal());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<Void> eliminarProveedor(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📌 Iniciando petición para eliminar Proveedor con Codigo de Sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        try {
            proveedorService.eliminarProveedor(proveedorRequestDTO);
            log.info("📌 Finalizó petición de eliminación de Proveedor con Codigo de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("⚠️ Error al eliminar Proveedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
