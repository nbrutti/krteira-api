package me.nbrutti.krteiraapi.repository;

import me.nbrutti.krteiraapi.models.TipoPapel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PapelRepository {
    Optional recuperarPorTipo(final TipoPapel tipo);
}
