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

import es.pmg.tennisjazz.domain.Ranking;
import es.pmg.tennisjazz.domain.*; // for static metamodels
import es.pmg.tennisjazz.repository.RankingRepository;
import es.pmg.tennisjazz.service.dto.RankingCriteria;

/**
 * Service for executing complex queries for {@link Ranking} entities in the database.
 * The main input is a {@link RankingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ranking} or a {@link Page} of {@link Ranking} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RankingQueryService extends QueryService<Ranking> {

    private final Logger log = LoggerFactory.getLogger(RankingQueryService.class);

    private final RankingRepository rankingRepository;

    public RankingQueryService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    /**
     * Return a {@link List} of {@link Ranking} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ranking> findByCriteria(RankingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ranking> specification = createSpecification(criteria);
        return rankingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ranking} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ranking> findByCriteria(RankingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ranking> specification = createSpecification(criteria);
        return rankingRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RankingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ranking> specification = createSpecification(criteria);
        return rankingRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<Ranking> createSpecification(RankingCriteria criteria) {
        Specification<Ranking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ranking_.id));
            }
            if (criteria.getPoints() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoints(), Ranking_.points));
            }
            if (criteria.getGamesWin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGamesWin(), Ranking_.gamesWin));
            }
            if (criteria.getGamesLoss() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGamesLoss(), Ranking_.gamesLoss));
            }
            if (criteria.getSetsWin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSetsWin(), Ranking_.setsWin));
            }
            if (criteria.getSetsLoss() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSetsLoss(), Ranking_.setsLoss));
            }
            if (criteria.getMatchesPlayed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMatchesPlayed(), Ranking_.matchesPlayed));
            }
            if (criteria.getMatchesWon() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMatchesWon(), Ranking_.matchesWon));
            }
            if (criteria.getMatchesLoss() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMatchesLoss(), Ranking_.matchesLoss));
            }
            if (criteria.getMatchesNotPresent() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMatchesNotPresent(), Ranking_.matchesNotPresent));
            }
            if (criteria.getMatchesAbandoned() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMatchesAbandoned(), Ranking_.matchesAbandoned));
            }
            if (criteria.getTournamentGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getTournamentGroupId(),
                    root -> root.join(Ranking_.tournamentGroup, JoinType.LEFT).get(TournamentGroup_.id)));
            }
            if (criteria.getPlayerId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlayerId(),
                    root -> root.join(Ranking_.player, JoinType.LEFT).get(Player_.id)));
            }
        }
        return specification;
    }
}
