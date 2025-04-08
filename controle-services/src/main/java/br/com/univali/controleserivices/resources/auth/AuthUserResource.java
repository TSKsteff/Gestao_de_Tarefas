package br.com.univali.controleserivices.resources.auth;

import br.com.univali.controleserivices.entities.Usuario;
import br.com.univali.controleserivices.service.UsuarioService;
import br.com.univali.controleserivices.service.exceptions.ResourceNotFoundExceptoin;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/auth")
public class AuthUserResource {

    private final UsuarioService usuarioService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> getUser(@PathVariable Long id) {
        try {
            log.debug("Rest request to get user by id {}:", id);
            Usuario usuario = usuarioService.getById(id);
            return ResponseEntity.ok(usuario);
        } catch (ResourceNotFoundExceptoin e) {
            log.error("Erro ao buscar usuário por ID: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar usuário por ID: {}", e.getMessage());
            throw new RuntimeException("Erro inesperado ao buscar usuário por ID", e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String name = userDetails.getUsername();

            log.debug("Rest request to get current user by email {}:", name);
            Usuario usuario = usuarioService.findByUserName(name);
            return ResponseEntity.ok(usuario);
        } catch (UsernameNotFoundException e) {
            log.error("Usuário não encontrado: {}", e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}