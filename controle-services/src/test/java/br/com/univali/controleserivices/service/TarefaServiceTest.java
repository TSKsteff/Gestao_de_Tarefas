package br.com.univali.controleserivices.service;

import br.com.univali.controleserivices.entities.Tarefa;
import br.com.univali.controleserivices.entities.Usuario;
import br.com.univali.controleserivices.entities.enums.Status;
import br.com.univali.controleserivices.entities.enums.StatusUser;
import br.com.univali.controleserivices.repositories.TarefaRepository;
import br.com.univali.controleserivices.service.exceptions.DatabaseException;
import br.com.univali.controleserivices.service.exceptions.ResourceNotFoundExceptoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioService usuarioService;


    @InjectMocks
    private TarefaService tarefaService;

    private Tarefa tarefa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setStatus(StatusUser.ACTIVE);

        tarefa = new Tarefa();
        tarefa.setId(1L);
        tarefa.setTitulo("Teste");
        tarefa.setDescricao("Descrição de teste");
        tarefa.setDataVencimento(LocalDate.now().plusDays(1));
        tarefa.setStatus(Status.PENDENTE);
        tarefa.setUsuario(usuario);
    }

    @Test
    void findAll() {
        when(tarefaRepository.findAll()).thenReturn(List.of(tarefa));
        List<Tarefa> tarefas = tarefaService.findAll();
        assertFalse(tarefas.isEmpty());
        verify(tarefaRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        Tarefa result = tarefaService.findById(1L);
        assertNotNull(result);
        assertEquals(tarefa.getId(), result.getId());
        verify(tarefaRepository, times(1)).findById(1L);
    }

    @Test
    void createTarefa() {
        when(usuarioService.getById(1L)).thenReturn(usuario);
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa result = tarefaService.createTarefa(tarefa, 1L);
        assertNotNull(result);
        assertEquals(tarefa.getTitulo(), result.getTitulo());
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void updateTarefa() {
        when(tarefaRepository.findById(1L)).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        Tarefa updated = new Tarefa();
        updated.setTitulo("Atualizado");
        updated.setDescricao("Descrição atualizada");
        updated.setDataVencimento(LocalDate.now().plusDays(5));
        updated.setStatus(Status.CONCLUIDO);

        Tarefa result = tarefaService.updateTarefa(updated, 1L);
        assertEquals("Atualizado", result.getTitulo());
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void delete() {
        when(tarefaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(tarefaRepository).deleteById(1L);

        assertDoesNotThrow(() -> tarefaService.delete(1L));
        verify(tarefaRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteThrowsExceptionWhenNotFound() {
        when(tarefaRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundExceptoin.class, () -> tarefaService.delete(1L));
    }

    @Test
    void deleteThrowsDatabaseException() {
        when(tarefaRepository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(tarefaRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> tarefaService.delete(1L));
    }
}
