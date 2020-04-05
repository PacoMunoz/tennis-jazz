package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.service.MatchService;
import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.repository.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Match}.
 */
@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    private final Logger log = LoggerFactory.getLogger(MatchServiceImpl.class);

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * Save a match.
     *
     * @param match the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Match save(Match match) {
        log.debug("Request to save Match : {}", match);
        return matchRepository.save(match);
    }

    /**
     * Get all the matches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Match> findAll(Pageable pageable) {
        log.debug("Request to get all Matches");
        return matchRepository.findAll(pageable);
    }


    /**
     * Get one match by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Match> findOne(Long id) {
        log.debug("Request to get Match : {}", id);
        return matchRepository.findById(id);
    }

    /**
     * Delete the match by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Match : {}", id);
        matchRepository.deleteById(id);
    }

    @Override
    public List<Match> buscarTodosPorJugadorYJornadas(Player player, List<Round> rounds) {
        log.debug("Request to find all matches of a player: " + player.getId() + " in the sets of rounds: " + rounds);
        return matchRepository.buscarTodosPorJugadorYJornadas(player, rounds);
    }
}
