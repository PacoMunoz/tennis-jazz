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

import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.domain.*; // for static metamodels
import es.pmg.tennisjazz.repository.TournamentGroupRepository;
import es.pmg.tennisjazz.service.dto.TournamentGroupCriteria;

/**
 * Service for executing complex queries for {@link TournamentGroup} entities in the database.
 * The main input is a {@link TournamentGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TournamentGroup} or a {@link Page} of {@link TournamentGroup} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TournamentGroupQueryService extends QueryService<TournamentGroup> {

    private final Logger log = LoggerFactory.getLogger(TournamentGroupQueryService.class);

    private final TournamentGroupRepository tournamentGroupRepository;

    public TournamentGroupQueryService(TournamentGroupRepository tournamentGroupRepository) {
        this.tournamentGroupRepository = tournamentGroupRepository;
    }

    /**
     * Return a {@link List} of {@link TournamentGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TournamentGroup> findByCriteria(TournamentGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TournamentGroup> specification = createSpecification(criteria);
        return tournamentGroupRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TournamentGroup} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TournamentGroup> findByCriteria(TournamentGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TournamentGroup> specification = createSpecification(criteria);
        return tournamentGroupRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TournamentGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TournamentGroup> specification = createSpecification(criteria);
        return tournamentGroupRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    private Specification<TournamentGroup> createSpecification(TournamentGroupCriteria criteria) {
        Specification<TournamentGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TournamentGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TournamentGroup_.name));
            }
            if (criteria.getTournamentId() != null) {
                specification = specification.and(buildSpecification(criteria.getTournamentId(),
                    root -> root.join(TournamentGroup_.tournament, JoinType.LEFT).get(Tournament_.id)));
            }
            if (criteria.getRoundsId() != null) {
                specification = specification.and(buildSpecification(criteria.getRoundsId(),
                    root -> root.join(TournamentGroup_.rounds, JoinType.LEFT).get(Round_.id)));
            }
            if (criteria.getPlayersId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlayersId(),
                    root -> root.join(TournamentGroup_.players, JoinType.LEFT).get(Player_.id)));
            }
        }
        return specification;
    }
}
