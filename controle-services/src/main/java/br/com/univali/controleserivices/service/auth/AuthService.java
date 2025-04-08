package br.com.univali.controleserivices.service.auth;

import br.com.univali.controleserivices.repositories.UsuarioRepository;
import br.com.univali.controleserivices.service.exceptions.ResourceNotFoundExceptoin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundExceptoin("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return usuarioRepository.findByName(username)
                .orElseThrow(() -> new ResourceNotFoundExceptoin("Usuário não encontrado"))
                .getId();
    }
}
