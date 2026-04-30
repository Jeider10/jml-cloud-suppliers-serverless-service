package com.cloud.jml.controller;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
        log.info("🔥 ProveedorController inicializado correctamente.");
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores() {
        log.info("📥 [SOLICITUD] Listar todos los proveedores");

        List<ProveedorResponseDTO> proveedores = proveedorService.listarProveedores();

        if (proveedores == null || proveedores.isEmpty()) {
            log.warn("📤 [RESPUESTA] No se encontraron proveedores");
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores", proveedores.size());

        return ResponseEntity.ok(proveedores);
    }

    @PostMapping("/register")
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@Valid @RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📥 [SOLICITUD] Crear Proveedor: {}", proveedorRequestDTO.getNombre());

        ProveedorResponseDTO response = proveedorService.crearProveedor(proveedorRequestDTO);

        // FIX: Se reemplazo describeConstable().isEmpty() por validacion null directa — describeConstable() en Long nunca retorna vacio, causando que la validacion nunca detectara errores
        if (response == null || response.getCodigoSucursal() == null) {
            log.warn("📤 [RESPUESTA] Error al crear el proveedor: {}", proveedorRequestDTO.getNombre());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("📤 [RESPUESTA] Proveedor creado: {} con codigo de sucursal: {}", response.getNombre(), response.getCodigoSucursal());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigoSucursal")
    public ResponseEntity<ProveedorResponseDTO> obtenerProveedorPorCodigoSucursal(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📥 [SOLICITUD] Buscar proveedor por codigo de sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        ProveedorResponseDTO proveedor = proveedorService.obtenerProveedorPorCodigoSucursal(proveedorRequestDTO);

        // FIX: Se reemplazo describeConstable().isEmpty() por validacion null directa — misma correccion que en crearProveedor
        if (proveedor == null || proveedor.getCodigoSucursal() == null) {
            log.warn("📤 [RESPUESTA] Proveedor no encontrado con codigo de sucursal: {}", codigoSucursal);
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Proveedor encontrado con codigo de sucursal: {}", proveedor.getCodigoSucursal());

        return ResponseEntity.ok(proveedor);
    }

    @GetMapping("/nombre")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorNombre(@RequestParam("nombre") String nombre) {
        log.info("📥 [SOLICITUD] Buscar proveedor por nombre: {}", nombre);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setNombre(nombre);

        List<ProveedorResponseDTO> proveedor = proveedorService.obtenerProveedorPorNombre(proveedorRequestDTO);

        if (proveedor == null || proveedor.isEmpty()) {
            log.warn("📤 [RESPUESTA] Proveedor no encontrado con nombre: {}", proveedorRequestDTO.getNombre());
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores con nombre: {}", proveedor.size(), proveedorRequestDTO.getNombre());

        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/update")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@Valid @RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📥 [SOLICITUD] Actualizar proveedor con codigo de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        ProveedorResponseDTO proveedorResponseDTO = proveedorService.actualizarProveedor(proveedorRequestDTO);

        // FIX: Se reemplazo describeConstable().isEmpty() por validacion null directa — misma correccion que en crearProveedor
        if (proveedorResponseDTO == null || proveedorResponseDTO.getCodigoSucursal() == null) {
            log.warn("📤 [RESPUESTA] Error al actualizar el proveedor con codigo de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        log.info("📤 [RESPUESTA] Proveedor actualizado correctamente: {} con codigo de sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());

        return ResponseEntity.ok(proveedorResponseDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> eliminarProveedor(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📥 [SOLICITUD] Eliminar proveedor con codigo de sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        proveedorService.eliminarProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor eliminado correctamente con codigo de sucursal: {}", codigoSucursal);

        return ResponseEntity.ok().build();
    }
}
