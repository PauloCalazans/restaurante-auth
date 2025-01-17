package com.calazanstec.restaurante.auth.core;

import com.calazanstec.restaurante.auth.domain.Usuario;
import com.calazanstec.restaurante.auth.domain.UsuarioRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Paulo Calazans on 10/01/2025
 */

@Validated
@Component
@ConfigurationProperties("restaurante.jwt.keystore")
@Setter
@Getter
public class JwtKeyStoreProperties {

    private String path;
    private String password;
    private String keypairAlias;

    /**
     * @author Paulo Calazans on 11/01/2025
     */

    @Service
    public static class JpaUserDetailService implements UserDetailsService {

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Transactional(readOnly = true)
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            var usuario = usuarioRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário  não encontrado com o e-mail informado"));

            return new AuthUser(usuario, getAuthorities(usuario));
        }

        private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
            return usuario.getGrupos().stream()
                    .flatMap(grupo -> grupo.getPermissoes().stream())
                    .map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
                    .collect(Collectors.toSet());
        }
    }
}
