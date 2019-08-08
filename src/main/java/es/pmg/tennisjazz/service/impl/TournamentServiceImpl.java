package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.service.TournamentService;
import es.pmg.tennisjazz.domain.Tournament;
import es.pmg.tennisjazz.repository.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Tournament}.
 */
@Service
@Transactional
public class TournamentServiceImpl implements TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentServiceImpl.class);

    private final TournamentRepository tournamentRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Save a tournament.
     *
     * @param tournament the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Tournament save(Tournament tournament) {
        log.debug("Request to save Tournament : {}", tournament);
        return tournamentRepository.save(tournament);
    }

    /**
     * Get all the tournaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Tournament> findAll(Pageable pageable) {
        log.debug("Request to get all Tournaments");
        return tournamentRepository.findAll(pageable);
    }


    /**
     * Get one tournament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Tournament> findOne(Long id) {
        log.debug("Request to get Tournament : {}", id);
        return tournamentRepository.findById(id);
    }

    /**
     * Delete the tournament by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tournament : {}", id);
        tournamentRepository.deleteById(id);
    }
}
