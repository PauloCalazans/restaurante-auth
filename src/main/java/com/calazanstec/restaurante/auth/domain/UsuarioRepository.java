package com.calazanstec.restaurante.auth.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * @author Paulo Calazans on 11/01/2025
 */

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
