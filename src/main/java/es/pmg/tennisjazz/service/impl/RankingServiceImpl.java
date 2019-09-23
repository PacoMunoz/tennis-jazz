package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.domain.*;
import es.pmg.tennisjazz.repository.MatchRepository;
import es.pmg.tennisjazz.service.RankingService;
import es.pmg.tennisjazz.repository.RankingRepository;
import es.pmg.tennisjazz.service.RoundQueryService;
import es.pmg.tennisjazz.service.RoundService;
import es.pmg.tennisjazz.service.dto.RoundCriteria;
import es.pmg.tennisjazz.service.util.RankingCalculateUtil;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Ranking}.
 */
@Service
@Transactional
public class RankingServiceImpl implements RankingService {

    private final Logger log = LoggerFactory.getLogger(RankingServiceImpl.class);

    private final RankingRepository rankingRepository;

    private final MatchRepository matchRepository;

    private final RoundQueryService roundQueryService;

    public RankingServiceImpl(RankingRepository rankingRepository, MatchRepository matchRepository, RoundQueryService roundQueryService) {
        this.rankingRepository = rankingRepository;
        this.matchRepository = matchRepository;
        this.roundQueryService = roundQueryService;
    }

    /**
     * Save a ranking.
     *
     * @param ranking the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Ranking save(Ranking ranking) {
        log.debug("Request to save Ranking : {}", ranking);
        return rankingRepository.save(ranking);
    }

    /**
     * Get all the rankings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Ranking> findAll(Pageable pageable) {
        log.debug("Request to get all Rankings");
        return rankingRepository.findAll(pageable);
    }


    /**
     * Get one ranking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ranking> findOne(Long id) {
        log.debug("Request to get Ranking : {}", id);
        return rankingRepository.findById(id);
    }

    /**
     * Delete the ranking by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ranking : {}", id);
        rankingRepository.deleteById(id);
    }

    /**
     * Update player ranking in a group
     *
     * @param player the player which ranking must update
     * @param group the group which player ranking must update
     */
    @Override
    public void updateRanking(Player player, TournamentGroup group) {
        log.debug("Request to update Ranking of player: " + player.getId() + " in group : " + group.getId());
        //get all player match in group
        RoundCriteria criteria = new RoundCriteria();
        LongFilter groupId = new LongFilter();
        List<Round> rounds = new ArrayList<>();
        List<Match> matches = new ArrayList<>();

        groupId.setEquals(group.getId());
        criteria.setTournamentGroupId(groupId);
        rounds =  this.roundQueryService.findByCriteria(criteria);
        matches =  this.matchRepository.buscarTodosPorJugadorYJornadas(player, rounds);
        //calculate total player points
        RankingCalculateUtil.calculatePoints(player, matches);
        //calculate total games won
        RankingCalculateUtil.calculateGamesWon(player, matches);
        //calculate total games loss
        RankingCalculateUtil.calculateGamesLoss(player, matches);
        //calculate set won

        //calculate matches played

        //calculate matches won

        //calculate matches loss

        //calculate matches not present

        //calculate matches abandoned

        //calculate tieBreaksPlayed

        //calculate tieBreaksWon

        Ranking ranking = new Ranking();
        this.rankingRepository.save(ranking);


    }

}


