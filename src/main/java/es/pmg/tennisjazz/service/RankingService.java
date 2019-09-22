package es.pmg.tennisjazz.service;

import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.domain.Ranking;

import es.pmg.tennisjazz.domain.TournamentGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Ranking}.
 */
public interface RankingService {

    /**
     * Save a ranking.
     *
     * @param ranking the entity to save.
     * @return the persisted entity.
     */
    Ranking save(Ranking ranking);

    /**
     * Get all the rankings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ranking> findAll(Pageable pageable);


    /**
     * Get the "id" ranking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ranking> findOne(Long id);

    /**
     * Delete the "id" ranking.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * update player ranking in group
     *
     * @param player the player which ranking must update
     * @param group the group which player ranking must update
     */
    void updateRanking(Player player, TournamentGroup group);
}
