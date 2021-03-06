package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.service.GenderService;
import es.pmg.tennisjazz.domain.Gender;
import es.pmg.tennisjazz.repository.GenderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Gender}.
 */
@Service
@Transactional
public class GenderServiceImpl implements GenderService {

    private final Logger log = LoggerFactory.getLogger(GenderServiceImpl.class);

    private final GenderRepository genderRepository;

    public GenderServiceImpl(GenderRepository genderRepository) {
        this.genderRepository = genderRepository;
    }

    /**
     * Save a gender.
     *
     * @param gender the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Gender save(Gender gender) {
        log.debug("Request to save Gender : {}", gender);
        return genderRepository.save(gender);
    }

    /**
     * Get all the genders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Gender> findAll(Pageable pageable) {
        log.debug("Request to get all Genders");
        return genderRepository.findAll(pageable);
    }


    /**
     * Get one gender by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Gender> findOne(Long id) {
        log.debug("Request to get Gender : {}", id);
        return genderRepository.findById(id);
    }

    /**
     * Delete the gender by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gender : {}", id);
        genderRepository.deleteById(id);
    }
}
