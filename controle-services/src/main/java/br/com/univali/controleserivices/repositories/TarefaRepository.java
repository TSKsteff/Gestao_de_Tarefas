package br.com.univali.controleserivices.repositories;

import br.com.univali.controleserivices.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa>  findByUsuarioId(Long userId);
    Optional<Tarefa> findByIdAndUsuarioId(Long taskId, Long userId);
}
