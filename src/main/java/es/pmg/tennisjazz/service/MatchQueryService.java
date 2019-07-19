package es.pmg.tennisjazz.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

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
    private Specification<Match> createSpecification(MatchCriteria criteria) {
        Specification<Match> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Match_.id));
            }
            if (criteria.getPlayer1Set1Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayer1Set1Result(), Match_.player1Set1Result));
            }
            if (criteria.getPlayer2Set1Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayer2Set1Result(), Match_.player2Set1Result));
            }
            if (criteria.getPlayer1Set2Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayer1Set2Result(), Match_.player1Set2Result));
            }
            if (criteria.getPlayer2Set2Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayer2Set2Result(), Match_.player2Set2Result));
            }
            if (criteria.getPlayer1Set3Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayer1Set3Result(), Match_.player1Set3Result));
            }
            if (criteria.getPlayer2Set3Result() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlayer2Set3Result(), Match_.player2Set3Result));
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
}
