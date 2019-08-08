package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.service.TournamentGroupService;
import es.pmg.tennisjazz.web.rest.errors.BadRequestAlertException;
import es.pmg.tennisjazz.service.dto.TournamentGroupCriteria;
import es.pmg.tennisjazz.service.TournamentGroupQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link es.pmg.tennisjazz.domain.TournamentGroup}.
 */
@RestController
@RequestMapping("/api")
public class TournamentGroupResource {

    private final Logger log = LoggerFactory.getLogger(TournamentGroupResource.class);

    private static final String ENTITY_NAME = "tournamentGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TournamentGroupService tournamentGroupService;

    private final TournamentGroupQueryService tournamentGroupQueryService;

    public TournamentGroupResource(TournamentGroupService tournamentGroupService, TournamentGroupQueryService tournamentGroupQueryService) {
        this.tournamentGroupService = tournamentGroupService;
        this.tournamentGroupQueryService = tournamentGroupQueryService;
    }

    /**
     * {@code POST  /tournament-groups} : Create a new tournamentGroup.
     *
     * @param tournamentGroup the tournamentGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tournamentGroup, or with status {@code 400 (Bad Request)} if the tournamentGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tournament-groups")
    public ResponseEntity<TournamentGroup> createTournamentGroup(@Valid @RequestBody TournamentGroup tournamentGroup) throws URISyntaxException {
        log.debug("REST request to save TournamentGroup : {}", tournamentGroup);
        if (tournamentGroup.getId() != null) {
            throw new BadRequestAlertException("A new tournamentGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TournamentGroup result = tournamentGroupService.save(tournamentGroup);
        return ResponseEntity.created(new URI("/api/tournament-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tournament-groups} : Updates an existing tournamentGroup.
     *
     * @param tournamentGroup the tournamentGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournamentGroup,
     * or with status {@code 400 (Bad Request)} if the tournamentGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tournamentGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tournament-groups")
    public ResponseEntity<TournamentGroup> updateTournamentGroup(@Valid @RequestBody TournamentGroup tournamentGroup) throws URISyntaxException {
        log.debug("REST request to update TournamentGroup : {}", tournamentGroup);
        if (tournamentGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TournamentGroup result = tournamentGroupService.save(tournamentGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tournamentGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tournament-groups} : get all the tournamentGroups.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tournamentGroups in body.
     */
    @GetMapping("/tournament-groups")
    public ResponseEntity<List<TournamentGroup>> getAllTournamentGroups(TournamentGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TournamentGroups by criteria: {}", criteria);
        Page<TournamentGroup> page = tournamentGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /tournament-groups/count} : count all the tournamentGroups.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/tournament-groups/count")
    public ResponseEntity<Long> countTournamentGroups(TournamentGroupCriteria criteria) {
        log.debug("REST request to count TournamentGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(tournamentGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tournament-groups/:id} : get the "id" tournamentGroup.
     *
     * @param id the id of the tournamentGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tournamentGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tournament-groups/{id}")
    public ResponseEntity<TournamentGroup> getTournamentGroup(@PathVariable Long id) {
        log.debug("REST request to get TournamentGroup : {}", id);
        Optional<TournamentGroup> tournamentGroup = tournamentGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tournamentGroup);
    }

    /**
     * {@code DELETE  /tournament-groups/:id} : delete the "id" tournamentGroup.
     *
     * @param id the id of the tournamentGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tournament-groups/{id}")
    public ResponseEntity<Void> deleteTournamentGroup(@PathVariable Long id) {
        log.debug("REST request to delete TournamentGroup : {}", id);
        tournamentGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
