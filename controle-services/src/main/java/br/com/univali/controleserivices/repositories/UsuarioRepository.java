package br.com.univali.controleserivices.repositories;

import br.com.univali.controleserivices.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> getUsuariosByEmail(String email);
    Optional<Usuario> findByName(String username);

}
