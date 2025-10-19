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

    @GetMapping("/list/all")
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores() {
        log.info("📥 [SOLICITUD] Listar todos los proveedores");

        List<ProveedorResponseDTO> proveedores = proveedorService.listarProveedores();

        log.info("📤 [RESPUESTA] Se retornan {} proveedores", proveedores.size());

        return ResponseEntity.ok(proveedores);
    }

    @PostMapping("/register")
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📥 [SOLICITUD] Crear Proveedor: {}", proveedorRequestDTO.getNombre());

        ProveedorResponseDTO response = proveedorService.crearProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor creado: {} con código de sucursal: {}", response.getNombre(), response.getCodigoSucursal());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/codigoSucursal")
    public ResponseEntity<ProveedorResponseDTO> obtenerProveedorPorCodigoSucursal(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📥 [SOLICITUD] Buscar proveedor por código de sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        ProveedorResponseDTO proveedor = proveedorService.obtenerProveedorPorCodigoSucursal(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor encontrado con código de sucursal: {}", proveedor.getCodigoSucursal());

        return ResponseEntity.ok(proveedor);
    }

    @GetMapping("/nombre")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorNombre(@RequestParam("nombre") String nombre) {
        log.info("📥 [SOLICITUD] Buscar proveedor por nombre: {}", nombre);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setNombre(nombre);

        List<ProveedorResponseDTO> proveedor = proveedorService.obtenerProveedorPorNombre(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Se retornan {} proveedores con nombre: {}", proveedor.size(), proveedorRequestDTO.getNombre());

        return ResponseEntity.ok(proveedor);
    }

    @PutMapping("/update")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📥 [SOLICITUD] Actualizar proveedor con código de sucursal: {}", proveedorRequestDTO.getCodigoSucursal());

        ProveedorResponseDTO proveedorResponseDTO = proveedorService.actualizarProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor actualizado correctamente: {} con código de sucursal: {}", proveedorRequestDTO.getNombre(), proveedorRequestDTO.getCodigoSucursal());

        return ResponseEntity.ok(proveedorResponseDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> eliminarProveedor(@RequestParam("codigoSucursal") Long codigoSucursal) {
        log.info("📥 [SOLICITUD] Eliminar proveedor con código de sucursal: {}", codigoSucursal);

        ProveedorRequestDTO proveedorRequestDTO = new ProveedorRequestDTO();
        proveedorRequestDTO.setCodigoSucursal(codigoSucursal);

        proveedorService.eliminarProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor eliminado correctamente con código de sucursal: {}", codigoSucursal);

        return ResponseEntity.ok().build();
    }
}
