package com.cloud.jml.controller;

import com.cloud.jml.dto.ProveedorPapeleraResponseDTO;
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

    // ─── Listar activos ───────────────────────────────────────────────────────
    @GetMapping("/list/all")
    public ResponseEntity<List<ProveedorResponseDTO>> listarProveedores() {
        log.info("📥 [SOLICITUD] Listar todos los proveedores activos");

        List<ProveedorResponseDTO> proveedores = proveedorService.listarProveedores();

        if (proveedores.isEmpty()) {
            log.warn("📤 [RESPUESTA] No se encontraron proveedores activos");
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores", proveedores.size());

        return ResponseEntity.ok(proveedores);
    }

    // ─── Registrar ────────────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@Valid @RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📥 [SOLICITUD] Crear Proveedor: {}", proveedorRequestDTO.getNombre());

        ProveedorResponseDTO response = proveedorService.crearProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor creado: {} con codigo: {}", response.getNombre(), response.getCodigoSucursal());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── Buscar por código sucursal ───────────────────────────────────────────
    @GetMapping("/codigoSucursal")
    public ResponseEntity<ProveedorResponseDTO> obtenerProveedorPorCodigoSucursal(@RequestParam("codigoSucursal") String codigoSucursal) {
        log.info("📥 [SOLICITUD] Buscar proveedor por codigo de sucursal: {}", codigoSucursal);

        ProveedorResponseDTO proveedor = proveedorService.obtenerProveedorPorCodigoSucursal(codigoSucursal);

        log.info("📤 [RESPUESTA] Proveedor encontrado: {}", codigoSucursal);

        return ResponseEntity.ok(proveedor);
    }

    // ─── Buscar por nombre ────────────────────────────────────────────────────
    @GetMapping("/nombre")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorNombre(@RequestParam("nombre") String nombre) {
        log.info("📥 [SOLICITUD] Buscar proveedor por nombre: {}", nombre);

        List<ProveedorResponseDTO> proveedores = proveedorService.obtenerProveedorPorNombre(nombre);

        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores con nombre: {}", proveedores.size(), nombre);

        return ResponseEntity.ok(proveedores);
    }

    // ─── Buscar por correo ────────────────────────────────────────────────────
    @GetMapping("/correo")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorCorreo(@RequestParam("correo") String correo) {
        log.info("📥 [SOLICITUD] Buscar proveedores por correo: {}", correo);

        List<ProveedorResponseDTO> proveedores = proveedorService.obtenerProveedorPorCorreo(correo);

        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores con correo: {}", proveedores.size(), correo);

        return ResponseEntity.ok(proveedores);
    }

    // ─── Buscar por creadoPor ─────────────────────────────────────────────────
    @GetMapping("/creadoPor")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorCreadoPor(@RequestParam("creadoPor") String creadoPor) {
        log.info("📥 [SOLICITUD] Buscar proveedores por creadoPor: {}", creadoPor);

        List<ProveedorResponseDTO> proveedores = proveedorService.obtenerProveedorPorCreadoPor(creadoPor);

        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores creados por: {}", proveedores.size(), creadoPor);

        return ResponseEntity.ok(proveedores);
    }

    // ─── Buscar por fecha de creación ─────────────────────────────────────────
    @GetMapping("/fechaCreacion")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorFechaCreacion(
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {

        log.info("📥 [SOLICITUD] Buscar proveedores por rango de fecha de creacion: {} - {}", fechaInicio, fechaFin);

        List<ProveedorResponseDTO> proveedores = proveedorService.obtenerProveedorPorFechaCreacion(fechaInicio, fechaFin);

        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores en el rango de fechas", proveedores.size());

        return ResponseEntity.ok(proveedores);
    }

    // ─── Buscar por fecha de actualización ───────────────────────────────────
    @GetMapping("/fechaActualizacion")
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerProveedorPorFechaActualizacion(
            @RequestParam("fechaInicio") String fechaInicio,
            @RequestParam("fechaFin") String fechaFin) {

        log.info("📥 [SOLICITUD] Buscar proveedores por rango de fecha de actualizacion: {} - {}", fechaInicio, fechaFin);

        List<ProveedorResponseDTO> proveedores = proveedorService.obtenerProveedorPorFechaActualizacion(fechaInicio, fechaFin);

        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores por fecha de actualizacion", proveedores.size());

        return ResponseEntity.ok(proveedores);
    }

    // ─── Actualizar ───────────────────────────────────────────────────────────
    @PutMapping("/update")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@Valid @RequestBody ProveedorRequestDTO proveedorRequestDTO) {
        log.info("📥 [SOLICITUD] Actualizar proveedor con codigo: {}", proveedorRequestDTO.getCodigoSucursal());

        ProveedorResponseDTO response = proveedorService.actualizarProveedor(proveedorRequestDTO);

        log.info("📤 [RESPUESTA] Proveedor actualizado: {}", response.getCodigoSucursal());

        return ResponseEntity.ok(response);
    }

    // ─── Soft delete (enviar a papelera) ──────────────────────────────────────
    @DeleteMapping("/delete")
    public ResponseEntity<Void> eliminarProveedor(
            @RequestParam("codigoSucursal") String codigoSucursal,
            @RequestParam("eliminadoPorId") String eliminadoPorId,
            @RequestParam("eliminadoPorNombre") String eliminadoPorNombre) {

        log.info("📥 [SOLICITUD] Enviar a papelera proveedor con codigo: {}", codigoSucursal);

        proveedorService.eliminarProveedor(codigoSucursal, eliminadoPorId, eliminadoPorNombre);

        log.info("📤 [RESPUESTA] Proveedor {} enviado a papelera por: {}", codigoSucursal, eliminadoPorNombre);

        return ResponseEntity.ok().build();
    }

    // ─── Listar papelera ──────────────────────────────────────────────────────
    @GetMapping("/trash")
    public ResponseEntity<List<ProveedorPapeleraResponseDTO>> listarPapelera() {
        log.info("📥 [SOLICITUD] Listar proveedores en papelera");

        List<ProveedorPapeleraResponseDTO> papelera = proveedorService.listarPapelera();

        if (papelera.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        log.info("📤 [RESPUESTA] Se retornan {} proveedores en papelera", papelera.size());

        return ResponseEntity.ok(papelera);
    }

    // ─── Restaurar desde papelera ─────────────────────────────────────────────
    @PutMapping("/restore")
    public ResponseEntity<ProveedorResponseDTO> restaurarProveedor(@RequestParam("codigoSucursal") String codigoSucursal) {
        log.info("📥 [SOLICITUD] Restaurar proveedor con codigo: {}", codigoSucursal);

        ProveedorResponseDTO response = proveedorService.restaurarProveedor(codigoSucursal);

        log.info("📤 [RESPUESTA] Proveedor restaurado: {}", codigoSucursal);

        return ResponseEntity.ok(response);
    }

    // ─── Eliminar definitivamente ─────────────────────────────────────────────
    @DeleteMapping("/permanent-delete")
    public ResponseEntity<Void> eliminarDefinitivo(@RequestParam("codigoSucursal") String codigoSucursal) {
        log.info("📥 [SOLICITUD] Eliminar definitivamente proveedor con codigo: {}", codigoSucursal);

        proveedorService.eliminarDefinitivo(codigoSucursal);

        log.info("📤 [RESPUESTA] Proveedor {} eliminado definitivamente", codigoSucursal);

        return ResponseEntity.ok().build();
    }
}
