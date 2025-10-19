package com.cloud.jml.controller;

import com.cloud.jml.dto.ProveedorRequestDTO;
import com.cloud.jml.dto.ProveedorResponseDTO;
import com.cloud.jml.service.ProveedorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/listar-proveedores")
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores() {
        log.info("📌 Iniciando petición para listar todos los Proveedores");

        List<ProveedorResponseDTO> proveedores = proveedorService.listarProveedores();

        log.info("📌 Finaliza petición para listar todos los Proveedores");

        return ResponseEntity.ok(proveedores);
    }

    @PostMapping("/register")
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📝 [PETICIÓN] Crear Proveedor: {}", proveedorRequestDTO.getNombre());

        ProveedorResponseDTO response = proveedorService.crearProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor creado con nombre: {}", proveedorRequestDTO.getNombre());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigoSucursal")
    public ResponseEntity<ProveedorResponseDTO> obtenerProveedorPorCodigoSucursal(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📌 Iniciando petición para buscar Proveedor por Código de Sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        ProveedorResponseDTO proveedor = proveedorService.obtenerProveedorPorCodigoSucursal(proveedorRequestDTO);

        log.info("✅ [RESPUESTA] Proveedor encontrado por Código de Sucursal: {}", codigoSucursal);

        return ResponseEntity.ok(proveedor);
    }

    @GetMapping("/nombre")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorNombre(@RequestParam("nombre") String nombre) {
        log.info("🔍 [SOLICITUD] Buscar Proveedor por nombre: {}", nombre);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setNombre(nombre);

        List<ProveedorResponseDTO> proveedor = proveedorService.obtenerProveedorPorNombre(proveedorRequestDTO);

        log.info("✅ [RESPUESTA] Proveedor encontrado por nombre: {}", nombre);

        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📝 [PETICIÓN] Actualizar proveedor con Código de Sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        ProveedorResponseDTO proveedorResponseDTO = proveedorService.actualizarProveedor(proveedorRequestDTO);

        log.info("✅ [RESPUESTA] Proveedor actualizado correctamente: {} con Código de Sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());

        return ResponseEntity.ok(proveedorResponseDTO);
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<Void> eliminarProveedor(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("🗑️ [PETICIÓN] Eliminar proveedor con Código de Sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        proveedorService.eliminarProveedor(proveedorRequestDTO);

        log.info("✅ [RESPUESTA] Proveedor eliminado correctamente con Código de Sucursal: {}", codigoSucursal);

        return ResponseEntity.ok().build();
    }
}
