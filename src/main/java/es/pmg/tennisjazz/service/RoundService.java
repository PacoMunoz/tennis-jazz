package es.pmg.tennisjazz.service;

import es.pmg.tennisjazz.domain.Round;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Round}.
 */
public interface RoundService {

    /**
     * Save a round.
     *
     * @param round the entity to save.
     * @return the persisted entity.
     */
    Round save(Round round);

    /**
     * Get all the rounds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Round> findAll(Pageable pageable);


    /**
     * Get the "id" round.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Round> findOne(Long id);

    /**
     * Delete the "id" round.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
