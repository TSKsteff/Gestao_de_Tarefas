package br.com.univali.controleserivices.service;

import br.com.univali.controleserivices.config.utils.HashFileUtils;
import br.com.univali.controleserivices.dto.usuario.CreateRequestDTO;
import br.com.univali.controleserivices.dto.usuario.LoginRequestDTO;
import br.com.univali.controleserivices.entities.Usuario;
import br.com.univali.controleserivices.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@email.com");
        usuario.setPassword(HashFileUtils.createHash("hashedPassword"));
    }

    @Test
    void getById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Usuario foundUser = usuarioService.getById(1L);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test
    void createUser() {
        CreateRequestDTO dto = new CreateRequestDTO("teste","novo@email.com", "senha123");
        when(usuarioRepository.getUsuariosByEmail(dto.email())).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario createdUser = usuarioService.createUser(dto);
        assertNotNull(createdUser);
        assertEquals("teste@email.com", createdUser.getEmail());
    }

    @Test
    void login() {
        LoginRequestDTO dto = new LoginRequestDTO("teste@email.com", "hashedPassword");

        when(usuarioRepository.getUsuariosByEmail(dto.email())).thenReturn(Optional.of(usuario));

        Usuario loggedInUser = usuarioService.login(dto);
        assertNotNull(loggedInUser);
        assertEquals("teste@email.com", loggedInUser.getEmail());
    }

    @Test
    void findByEmail() {
        when(usuarioRepository.getUsuariosByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        Usuario foundUser = usuarioService.findByEmail("teste@email.com");
        assertNotNull(foundUser);
        assertEquals("teste@email.com", foundUser.getEmail());
    }
}
