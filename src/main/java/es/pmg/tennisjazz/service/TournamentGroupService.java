package es.pmg.tennisjazz.service;

import es.pmg.tennisjazz.domain.TournamentGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TournamentGroup}.
 */
public interface TournamentGroupService {

    /**
     * Save a tournamentGroup.
     *
     * @param tournamentGroup the entity to save.
     * @return the persisted entity.
     */
    TournamentGroup save(TournamentGroup tournamentGroup);

    /**
     * Get all the tournamentGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TournamentGroup> findAll(Pageable pageable);

    /**
     * Get all the tournamentGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<TournamentGroup> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" tournamentGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TournamentGroup> findOne(Long id);

    /**
     * Delete the "id" tournamentGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
