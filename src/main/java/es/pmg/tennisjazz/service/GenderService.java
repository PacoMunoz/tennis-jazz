package es.pmg.tennisjazz.service;

import es.pmg.tennisjazz.domain.Gender;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Gender}.
 */
public interface GenderService {

    /**
     * Save a gender.
     *
     * @param gender the entity to save.
     * @return the persisted entity.
     */
    Gender save(Gender gender);

    /**
     * Get all the genders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gender> findAll(Pageable pageable);


    /**
     * Get the "id" gender.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gender> findOne(Long id);

    /**
     * Delete the "id" gender.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
