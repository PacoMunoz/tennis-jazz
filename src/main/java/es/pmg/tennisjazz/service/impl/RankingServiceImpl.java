package es.pmg.tennisjazz.service.impl;

import es.pmg.tennisjazz.domain.*;
import es.pmg.tennisjazz.repository.MatchRepository;
import es.pmg.tennisjazz.service.RankingQueryService;
import es.pmg.tennisjazz.service.RankingService;
import es.pmg.tennisjazz.repository.RankingRepository;
import es.pmg.tennisjazz.service.RoundQueryService;
import es.pmg.tennisjazz.service.dto.RankingCriteria;
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

    private final RankingQueryService rankingQueryService;

    private final MatchRepository matchRepository;

    private final RoundQueryService roundQueryService;

    public RankingServiceImpl(RankingRepository rankingRepository, MatchRepository matchRepository, RoundQueryService roundQueryService, RankingQueryService rankingQueryService) {
        this.rankingRepository = rankingRepository;
        this.matchRepository = matchRepository;
        this.roundQueryService = roundQueryService;
        this.rankingQueryService = rankingQueryService;
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

        //get player ranking in group or create it if not exist
        Optional<Ranking> oRanking = findByPlayerAndGroup(player, group);
        Ranking ranking = null;
        if (oRanking.isPresent()) {
            ranking = oRanking.get();
        } else {
            ranking = new Ranking();
        }
        //get all matches played by a player in group
        List<Match> matches = getMatches(player, group);
        //calculate total player points
        ranking.setPoints(RankingCalculateUtil.calculatePoints(player, matches));
        //calculate total games won
        ranking.setGamesWon(RankingCalculateUtil.calculateGamesWon(player, matches));
        //calculate total games loss
        ranking.setGamesLoss(RankingCalculateUtil.calculateGamesLoss(player, matches));
        //calculate set won
        ranking.setSetsWon(RankingCalculateUtil.calculateSetsWon(player, matches));
        //calculate matches played
        ranking.setMatchesPlayed(RankingCalculateUtil.calculateMatchesPlayed(matches));
        //calculate matches won
        ranking.setMatchesWon(RankingCalculateUtil.calculateMatchesWon(player, matches));
        //calculate matches loss
        ranking.setMatchesLoss(RankingCalculateUtil.calculateMatchesLoss(player, matches));
        //calculate matches not present
        ranking.setMatchesNotPresent(RankingCalculateUtil.calculateMatchesNotPresent(player, matches));
        //calculate matches abandoned
        ranking.setMatchesAbandoned(RankingCalculateUtil.calculateMatchesAbandoned(player, matches));
        //calculate tieBreaksPlayed
        ranking.setTieBreaksPlayed(RankingCalculateUtil.calculateTieBreaksPlayed(matches));
        //calculate tieBreaksWon
        ranking.setTieBreaksWon(RankingCalculateUtil.calculateGamesWon(player, matches));

        //update or create ranking
        this.rankingRepository.save(ranking);
    }

    /**
     * Get Ranking of a player in a group
     * @param player the player
     * @param group the group
     * @return the entity
     */
    private Optional<Ranking> findByPlayerAndGroup(Player player, TournamentGroup group) {
        RankingCriteria rankingCriteria = new RankingCriteria();
        LongFilter groupIdFilter = new LongFilter();
        LongFilter playerIdFilter = new LongFilter();
        groupIdFilter.setEquals(group.getId());

        playerIdFilter.setEquals(player.getId());
        rankingCriteria.setPlayerId(playerIdFilter);
        rankingCriteria.setTournamentGroupId(groupIdFilter);
        List<Ranking> rankings = rankingQueryService.findByCriteria(rankingCriteria);

        return Optional.ofNullable(rankings.get(0));
    }

    /**
     * Get all matches of a player in one group
     * @param player the player
     * @param group the matches
     * @return List of match
     */
    private List<Match> getMatches(Player player, TournamentGroup group) {
        RoundCriteria roundCriteria = new RoundCriteria();
        LongFilter groupId = new LongFilter();
        List<Round> rounds = new ArrayList<>();

        groupId.setEquals(group.getId());
        roundCriteria.setTournamentGroupId(groupId);
        rounds =  this.roundQueryService.findByCriteria(roundCriteria);

        return  this.matchRepository.buscarTodosPorJugadorYJornadas(player, rounds);
    }

}


