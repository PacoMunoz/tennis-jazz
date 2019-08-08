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

import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.domain.*; // for static metamodels
import es.pmg.tennisjazz.repository.RoundRepository;
import es.pmg.tennisjazz.service.dto.RoundCriteria;

/**
 * Service for executing complex queries for {@link Round} entities in the database.
 * The main input is a {@link RoundCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Round} or a {@link Page} of {@link Round} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoundQueryService extends QueryService<Round> {

    private final Logger log = LoggerFactory.getLogger(RoundQueryService.class);

    private final RoundRepository roundRepository;

    public RoundQueryService(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    /**
     * Return a {@link List} of {@link Round} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Round> findByCriteria(RoundCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Round> specification = createSpecification(criteria);
        return roundRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Round} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Round> findByCriteria(RoundCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Round> specification = createSpecification(criteria);
        return roundRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoundCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Round> specification = createSpecification(criteria);
        return roundRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Round> createSpecification(RoundCriteria criteria) {
        Specification<Round> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Round_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Round_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Round_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Round_.endDate));
            }
            if (criteria.getTournamentGroupId() != null) {
                specification = specification.and(buildSpecification(criteria.getTournamentGroupId(),
                    root -> root.join(Round_.tournamentGroup, JoinType.LEFT).get(TournamentGroup_.id)));
            }
            if (criteria.getMatchsId() != null) {
                specification = specification.and(buildSpecification(criteria.getMatchsId(),
                    root -> root.join(Round_.matchs, JoinType.LEFT).get(Match_.id)));
            }
        }
        return specification;
    }
}
