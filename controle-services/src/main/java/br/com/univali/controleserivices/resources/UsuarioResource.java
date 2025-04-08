package br.com.univali.controleserivices.resources;

import br.com.univali.controleserivices.config.security.TokenServices;
import br.com.univali.controleserivices.dto.usuario.CreateRequestDTO;
import br.com.univali.controleserivices.dto.usuario.LoginRequestDTO;
import br.com.univali.controleserivices.dto.usuario.LoginResponseDTO;
import br.com.univali.controleserivices.entities.Usuario;
import br.com.univali.controleserivices.service.UsuarioService;
import br.com.univali.controleserivices.service.exceptions.DatabaseException;
import br.com.univali.controleserivices.service.exceptions.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/api/users")
public class UsuarioResource {


    private final UsuarioService usuarioService;

    private final TokenServices tokenServices;


    @PostMapping(value = "/create")
    public ResponseEntity<Usuario> createUser(@RequestBody CreateRequestDTO dto){
        log.debug("Rest request to create user {}:", dto);
        try {
            Usuario usuario = usuarioService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (EmailAlreadyExistsException e) {
            throw new DatabaseException("Email já cadastrado. Tente outro.");
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO dto){
        log.debug("Rest request login user by email {}:", dto.email());

        try {
            Usuario user = usuarioService.login(dto);
            String token = tokenServices.generateToken(user);
            return ResponseEntity.ok(new LoginResponseDTO(token, user.getId()));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Usuário não encontrado.");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Senha inválida.");
        }
    }
}
