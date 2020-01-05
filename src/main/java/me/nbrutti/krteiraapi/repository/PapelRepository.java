package me.nbrutti.krteiraapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.nbrutti.krteiraapi.models.Papel;
import me.nbrutti.krteiraapi.models.TipoPapel;

import java.util.Optional;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Integer> {
    Optional<Papel> findByTipoPapel(final TipoPapel tipo);
}
