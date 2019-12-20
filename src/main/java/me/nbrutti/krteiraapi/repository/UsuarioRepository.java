package me.nbrutti.krteiraapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository {
    Optional recuperarPorEmail(final String email);
    Boolean existePorEmail(final String email);
}
