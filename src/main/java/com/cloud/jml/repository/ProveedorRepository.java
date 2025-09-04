package com.cloud.jml.repository;

import com.cloud.jml.model.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {
    Optional<ProveedorEntity> findByNic(Long nic);

    List<ProveedorEntity> findByNombre(String nombre);
}
