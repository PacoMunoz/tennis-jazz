package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.service.TournamentGroupService;
import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.repository.TournamentGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TournamentGroup}.
 */
@Service
@Transactional
public class TournamentGroupServiceImpl implements TournamentGroupService {

    private final Logger log = LoggerFactory.getLogger(TournamentGroupServiceImpl.class);

    private final TournamentGroupRepository tournamentGroupRepository;

    public TournamentGroupServiceImpl(TournamentGroupRepository tournamentGroupRepository) {
        this.tournamentGroupRepository = tournamentGroupRepository;
    }

    /**
     * Save a tournamentGroup.
     *
     * @param tournamentGroup the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TournamentGroup save(TournamentGroup tournamentGroup) {
        log.debug("Request to save TournamentGroup : {}", tournamentGroup);
        return tournamentGroupRepository.save(tournamentGroup);
    }

    /**
     * Get all the tournamentGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TournamentGroup> findAll(Pageable pageable) {
        log.debug("Request to get all TournamentGroups");
        return tournamentGroupRepository.findAll(pageable);
    }

    /**
     * Get all the tournamentGroups with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TournamentGroup> findAllWithEagerRelationships(Pageable pageable) {
        return tournamentGroupRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one tournamentGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TournamentGroup> findOne(Long id) {
        log.debug("Request to get TournamentGroup : {}", id);
        return tournamentGroupRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the tournamentGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TournamentGroup : {}", id);
        tournamentGroupRepository.deleteById(id);
    }
}
