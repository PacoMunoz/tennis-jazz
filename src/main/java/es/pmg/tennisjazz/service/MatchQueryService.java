package es.pmg.tennisjazz.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import es.pmg.tennisjazz.service.dto.TournamentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.*; // for static metamodels
import es.pmg.tennisjazz.repository.MatchRepository;
import es.pmg.tennisjazz.service.dto.MatchCriteria;

/**
 * Service for executing complex queries for {@link Match} entities in the database.
 * The main input is a {@link MatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Match} or a {@link Page} of {@link Match} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MatchQueryService extends QueryService<Match> {

    private final Logger log = LoggerFactory.getLogger(MatchQueryService.class);

    private final MatchRepository matchRepository;

    public MatchQueryService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    /**
     * Return a {@link List} of {@link Match} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Match> findByCriteria(MatchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Match> specification = createSpecification(criteria);
        return matchRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Match} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Match> findByCriteria(MatchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Match> specification = createSpecification(criteria);
        return matchRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MatchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Match> specification = createSpecification(criteria);
        return matchRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Match> createSpecification(MatchCriteria criteria) {
        Specification<Match> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Match_.id));
            }
            if (criteria.getLocalPlayerSet1Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerSet1Result(), Match_.localPlayerSet1Result));
            }
            if (criteria.getVisitorPlayerSet1Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerSet1Result(), Match_.visitorPlayerSet1Result));
            }
            if (criteria.getLocalPlayerTBSet1Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerTBSet1Result(), Match_.localPlayerTBSet1Result));
            }
            if (criteria.getVisitorPlayerTBSet1Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerTBSet1Result(), Match_.visitorPlayerTBSet1Result));
            }
            if (criteria.getLocalPlayerTBSet2Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerTBSet2Result(), Match_.localPlayerTBSet2Result));
            }
            if (criteria.getVisitorPlayerTBSet2Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerTBSet2Result(), Match_.visitorPlayerTBSet2Result));
            }
            if (criteria.getLocalPlayerTBSet3Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerTBSet3Result(), Match_.localPlayerTBSet3Result));
            }
            if (criteria.getVisitorPlayerTBSet3Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerTBSet3Result(), Match_.visitorPlayerTBSet3Result));
            }
            if (criteria.getLocalPlayerSet2Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerSet2Result(), Match_.localPlayerSet2Result));
            }
            if (criteria.getVisitorPlayerSet2Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerSet2Result(), Match_.visitorPlayerSet2Result));
            }
            if (criteria.getLocalPlayerSet3Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerSet3Result(), Match_.localPlayerSet3Result));
            }
            if (criteria.getVisitorPlayerSet3Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerSet3Result(), Match_.visitorPlayerSet3Result));
            }
            if (criteria.getLocalPlayerSets() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLocalPlayerSets(), Match_.localPlayerSets));
            }
            if (criteria.getVisitorPlayerSets() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVisitorPlayerSets(), Match_.visitorPlayerSets));
            }
            if (criteria.getLocalPlayerAbandoned() != null) {
                specification = specification.and(buildSpecification(criteria.getLocalPlayerAbandoned(), Match_.localPlayerAbandoned));
            }
            if (criteria.getVisitorPlayerAbandoned() != null) {
                specification = specification.and(buildSpecification(criteria.getVisitorPlayerAbandoned(), Match_.visitorPlayerAbandoned));
            }
            if (criteria.getLocalPlayerNotPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getLocalPlayerNotPresent(), Match_.localPlayerNotPresent));
            }
            if (criteria.getVisitorPlayerNotPresent() != null) {
                specification = specification.and(buildSpecification(criteria.getVisitorPlayerNotPresent(), Match_.visitorPlayerNotPresent));
            }
            if (criteria.getRoundId() != null) {
                specification = specification.and(buildSpecification(criteria.getRoundId(),
                    root -> root.join(Match_.round, JoinType.LEFT).get(Round_.id)));
            }
            if (criteria.getVisitorPlayerId() != null) {
                specification = specification.and(buildSpecification(criteria.getVisitorPlayerId(),
                    root -> root.join(Match_.visitorPlayer, JoinType.LEFT).get(Player_.id)));
            }
            if (criteria.getLocalPlayerId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocalPlayerId(),
                    root -> root.join(Match_.localPlayer, JoinType.LEFT).get(Player_.id)));
            }
        }
        return specification;
    }

    public Page<Match> findCurrentByPlayer(Long idPlayer, Pageable pageable) {
        log.debug("find current by playerId : {}", idPlayer);
        return this.matchRepository.buscarPartidosEnCurso(idPlayer, pageable);
    }
}
