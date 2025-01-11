package com.calazanstec.restaurante.auth.core;

import com.calazanstec.restaurante.auth.domain.Usuario;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

/**
 * @author Paulo Calazans on 11/01/2025
 */

@Getter
public class AuthUser extends User {

    private String fullName;

    public AuthUser(Usuario usuario) {
        super(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());

        this.fullName = usuario.getNome();
    }
}
