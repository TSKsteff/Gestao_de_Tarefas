package br.com.univali.controleserivices.service;

import br.com.univali.controleserivices.config.utils.HashFileUtils;
import br.com.univali.controleserivices.dto.usuario.CreateRequestDTO;
import br.com.univali.controleserivices.dto.usuario.LoginRequestDTO;
import br.com.univali.controleserivices.entities.Usuario;
import br.com.univali.controleserivices.entities.enums.StatusUser;
import br.com.univali.controleserivices.repositories.UsuarioRepository;
import br.com.univali.controleserivices.service.exceptions.EmailAlreadyExistsException;
import br.com.univali.controleserivices.service.exceptions.InvalidPasswordException;
import br.com.univali.controleserivices.service.exceptions.ResourceNotFoundExceptoin;
import br.com.univali.controleserivices.service.exceptions.UserNotActiveException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Log4j2
@RequiredArgsConstructor
@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    public Usuario getById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptoin(id));
    }

    public Usuario createUser(CreateRequestDTO dto) {
        if (usuarioRepository.getUsuariosByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        Usuario usuario = new Usuario(dto);
        usuario.setPassword(HashFileUtils.createHash(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public Usuario login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.getUsuariosByEmail(dto.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!HashFileUtils.createHash(dto.password()).equals(usuario.getPassword())) {
            throw new InvalidPasswordException();
        }

        if (usuario.getStatus() == StatusUser.INACTIVE) {
            throw new UserNotActiveException();
        }

        return usuario;
    }
    public Usuario findByUserName(String name) {
        return usuarioRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundExceptoin("UserName não encontrado: " + name));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.getUsuariosByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundExceptoin("Email não encontrado: " + email));
    }
}