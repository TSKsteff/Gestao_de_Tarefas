package br.com.univali.controleserivices.resources;

import br.com.univali.controleserivices.entities.Tarefa;
import br.com.univali.controleserivices.service.TarefaService;
import br.com.univali.controleserivices.service.exceptions.InvalidTaskException;
import br.com.univali.controleserivices.service.exceptions.ResourceNotFoundExceptoin;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/task")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class TarefaResource {

    private static final Logger log = LogManager.getLogger(TarefaResource.class);
    private final TarefaService tarefaService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok().body(tarefaService.getMyTasks());
        } catch (Exception e) {
            log.error("Erro ao buscar tarefas: {}", e.getMessage(), e);
            throw new InvalidTaskException("Erro ao buscar tarefas");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(tarefaService.getMyTaskById(id));
        } catch (ResourceNotFoundExceptoin e) {
            log.warn("Tarefa não encontrada: {}", id);
            throw new InvalidTaskException("Tarefa não encontrada");
        }
    }

    @PostMapping("/{id}/create-task")
    public ResponseEntity<?> createTask(@RequestBody Tarefa tarefa, @PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tarefaService.createTarefa(tarefa, id));
        } catch (InvalidTaskException e) {
            log.warn("Erro ao criar tarefa: {}", e.getMessage());
            throw new InvalidTaskException("Erro ao criar tarefa");
        }
    }

    @PutMapping("/{id}/update-task")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Tarefa tarefa) {
        try {
            return ResponseEntity.ok().body(tarefaService.updateTarefa(tarefa, id));
        } catch (ResourceNotFoundExceptoin e) {
            log.warn("Erro ao atualizar tarefa: Tarefa não encontrada - ID {}", id);
            throw new InvalidTaskException("Tarefa não encontrada com este ID: "+id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            tarefaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundExceptoin e) {
            log.warn("Erro ao excluir tarefa: Tarefa não encontrada - ID {}", id);
            throw  new InvalidTaskException("Tarefa não encontrada com esse ID: "+id);
        }
    }
}
