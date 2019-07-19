package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.service.RoundService;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.repository.RoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Round}.
 */
@Service
@Transactional
public class RoundServiceImpl implements RoundService {

    private final Logger log = LoggerFactory.getLogger(RoundServiceImpl.class);

    private final RoundRepository roundRepository;

    public RoundServiceImpl(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Save a round.
     *
     * @param round the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Round save(Round round) {
        log.debug("Request to save Round : {}", round);
        return roundRepository.save(round);
    }

    /**
     * Get all the rounds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Round> findAll(Pageable pageable) {
        log.debug("Request to get all Rounds");
        return roundRepository.findAll(pageable);
    }


    /**
     * Get one round by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Round> findOne(Long id) {
        log.debug("Request to get Round : {}", id);
        return roundRepository.findById(id);
    }

    /**
     * Delete the round by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Round : {}", id);
        roundRepository.deleteById(id);
    }
}
