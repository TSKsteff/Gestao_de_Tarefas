package br.com.univali.controleserivices.service;

import br.com.univali.controleserivices.entities.Tarefa;
import br.com.univali.controleserivices.entities.Usuario;
import br.com.univali.controleserivices.entities.enums.StatusUser;
import br.com.univali.controleserivices.repositories.TarefaRepository;
import br.com.univali.controleserivices.service.auth.AuthService;
import br.com.univali.controleserivices.service.exceptions.DatabaseException;
import br.com.univali.controleserivices.service.exceptions.InvalidTaskException;
import br.com.univali.controleserivices.service.exceptions.ResourceNotFoundExceptoin;
import br.com.univali.controleserivices.service.exceptions.UserNotActiveException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioService usuarioService;
    private final AuthService authService;


    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    public Tarefa findById(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExceptoin(id));
    }

    public Tarefa createTarefa(Tarefa tarefa, Long userId) {
        Usuario usuario = usuarioService.getById(userId);
        if(tarefa.getDataVencimento().isBefore(LocalDate.now())){
            tarefa.setDataVencimento(LocalDate.now());
        }
        if (usuario.getStatus() == StatusUser.INACTIVE) {
            throw new UserNotActiveException();
        }

        validarTarefa(tarefa);

        tarefa.setUsuario(usuario);
        return tarefaRepository.save(tarefa);
    }

    public Tarefa updateTarefa(Tarefa tarefa, Long id) {
        if(tarefa.getDataVencimento().isBefore(LocalDate.now())){
            tarefa.setDataVencimento(LocalDate.now());
        }
        return tarefaRepository.findById(id)
                .map(record -> {
                    validarTarefa(tarefa);
                    record.setTitulo(tarefa.getTitulo());
                    record.setDescricao(tarefa.getDescricao());
                    record.setDataVencimento(tarefa.getDataVencimento());
                    record.setStatus(tarefa.getStatus());
                    return tarefaRepository.save(record);
                })
                .orElseThrow(() -> new ResourceNotFoundExceptoin(id));
    }

    public void delete(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new ResourceNotFoundExceptoin(id);
        }
        try {
            tarefaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Não é possível excluir a tarefa. Ela pode estar associada a outros registros.");
        }
    }

    public Tarefa getMyTaskById(Long taskId) {
        Long userId = authService.getAuthenticatedUserId();

        Tarefa tarefa = tarefaRepository.findByIdAndUsuarioId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundExceptoin("Tarefa não encontrada ou não pertence a você"));
        return tarefa;
    }
    public List<Tarefa> getMyTasks() {
        Long id = authService.getAuthenticatedUserId();
        List<Tarefa> tarefas = tarefaRepository.findByUsuarioId(id);
        return tarefas;
    }

    private void validarTarefa(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
            throw new InvalidTaskException("O título da tarefa é obrigatório.");
        }
        if (tarefa.getDataVencimento() != null && tarefa.getDataVencimento().isBefore(LocalDate.now())) {
            throw new InvalidTaskException("A data de vencimento não pode ser no passado.");
        }
    }
}
