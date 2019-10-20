package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.domain.*;
import es.pmg.tennisjazz.service.*;
import es.pmg.tennisjazz.service.dto.TournamentGroupCriteria;
import es.pmg.tennisjazz.web.rest.errors.BadRequestAlertException;
import es.pmg.tennisjazz.service.dto.RankingCriteria;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link es.pmg.tennisjazz.domain.Ranking}.
 */
@RestController
@RequestMapping("/api")
public class RankingResource {

    private final Logger log = LoggerFactory.getLogger(RankingResource.class);

    private static final String ENTITY_NAME = "ranking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RankingService rankingService;

    private final RankingQueryService rankingQueryService;

    private final TournamentGroupQueryService tournamentGroupQueryService;

    private final PlayerService playerService;

    public RankingResource(RankingService rankingService, RankingQueryService rankingQueryService,
                           PlayerService playerService,
                           TournamentGroupQueryService tournamentGroupQueryService) {
        this.rankingService = rankingService;
        this.rankingQueryService = rankingQueryService;
        this.playerService = playerService;
        this.tournamentGroupQueryService = tournamentGroupQueryService;
    }

    /**
     * {@code POST  /rankings} : Create a new ranking.
     *
     * @param ranking the ranking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ranking, or with status {@code 400 (Bad Request)} if the ranking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rankings")
    public ResponseEntity<Ranking> createRanking(@RequestBody Ranking ranking) throws URISyntaxException {
        log.debug("REST request to save Ranking : {}", ranking);
        if (ranking.getId() != null) {
            throw new BadRequestAlertException("A new ranking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ranking result = rankingService.save(ranking);
        return ResponseEntity.created(new URI("/api/rankings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rankings} : Updates an existing ranking.
     *
     * @param ranking the ranking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ranking,
     * or with status {@code 400 (Bad Request)} if the ranking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ranking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rankings")
    public ResponseEntity<Ranking> updateRanking(@RequestBody Ranking ranking) throws URISyntaxException {
        log.debug("REST request to update Ranking : {}", ranking);
        if (ranking.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ranking result = rankingService.save(ranking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ranking.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /update} : update player ranking in group
     *
     * @param idp the player id which ranking must update
     * @param idr the round of the group which player ranking must update
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rankings in body.
     */
    @GetMapping("/rankings/update")
    public ResponseEntity<Void> updateRanking(@RequestParam Long idp, @RequestParam Long idr) throws URISyntaxException {
        log.debug("REST request to update Ranking of player with id: " + idp + " in the group of round " + idr);
        if (idr == null || idp == null) {
            throw new BadRequestAlertException("Invalid id", "Round or Player", "idnull");
        }

        TournamentGroupCriteria groupCriteria = new TournamentGroupCriteria();
        LongFilter roundIdLongFilter = new LongFilter();
        roundIdLongFilter.setEquals(idr);
        groupCriteria.setRoundsId(roundIdLongFilter);
        List<TournamentGroup> lstGroups = this.tournamentGroupQueryService.findByCriteria(groupCriteria);
        Optional<TournamentGroup> groupO = Optional.ofNullable(lstGroups.get(0));

        Optional<Player> playerO =  playerService.findOne(idp);
        if (!playerO.isPresent() || !groupO.isPresent()) {
            throw new BadRequestAlertException("Invalid id", "Group or Player", "idNoValid");
        }

        rankingService.updateRanking(playerO.get(), groupO.get());
        //Get player matches in the groups
            // -> Use searchcriteria in Match repository and dinamyc query https://www.baeldung.com/spring-data-jpa-query
            // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.special-parameters

        //Calculate data with all the matches
        //Generate ranking object to save
        //create o update ranking

       /* Page<Ranking> page = rankingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);*/
        //return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, idPlayer.toString())).build();
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Ranking update", "player: " + idp + " group of round: " + idr) ).build();
    }

    /**
     * {@code GET  /rankings} : get all the rankings.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rankings in body.
     */
    @GetMapping("/rankings")
    public ResponseEntity<List<Ranking>> getAllRankings(RankingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rankings by criteria: {}", criteria);
        Page<Ranking> page = rankingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /rankings/count} : count all the rankings.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/rankings/count")
    public ResponseEntity<Long> countRankings(RankingCriteria criteria) {
        log.debug("REST request to count Rankings by criteria: {}", criteria);
        return ResponseEntity.ok().body(rankingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rankings/:id} : get the "id" ranking.
     *
     * @param id the id of the ranking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ranking, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rankings/{id}")
    public ResponseEntity<Ranking> getRanking(@PathVariable Long id) {
        log.debug("REST request to get Ranking : {}", id);
        Optional<Ranking> ranking = rankingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ranking);
    }

    /**
     * {@code DELETE  /rankings/:id} : delete the "id" ranking.
     *
     * @param id the id of the ranking to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rankings/{id}")
    public ResponseEntity<Void> deleteRanking(@PathVariable Long id) {
        log.debug("REST request to delete Ranking : {}", id);
        rankingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
