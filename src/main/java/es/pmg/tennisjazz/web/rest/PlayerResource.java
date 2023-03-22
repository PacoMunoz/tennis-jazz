package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.service.PlayerService;
import es.pmg.tennisjazz.web.rest.errors.BadRequestAlertException;
import es.pmg.tennisjazz.service.dto.PlayerCriteria;
import es.pmg.tennisjazz.service.PlayerQueryService;

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
 * REST controller for managing {@link es.pmg.tennisjazz.domain.Player}.
 */
@RestController
@RequestMapping("/api")
public class PlayerResource {

    private final Logger log = LoggerFactory.getLogger(PlayerResource.class);

    private static final String ENTITY_NAME = "player";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerService playerService;

    private final PlayerQueryService playerQueryService;

    public PlayerResource(PlayerService playerService, PlayerQueryService playerQueryService) {
        this.playerService = playerService;
        this.playerQueryService = playerQueryService;
    }

    /**
     * {@code POST  /players} : Create a new player.
     *
     * @param player the player to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new player, or with status {@code 400 (Bad Request)} if the player has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to save Player : {}", player);
        if (player.getId() != null) {
            throw new BadRequestAlertException("A new player cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Player result = playerService.save(player);
        return ResponseEntity.created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /players} : Updates an existing player.
     *
     * @param player the player to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated player,
     * or with status {@code 400 (Bad Request)} if the player is not valid,
     * or with status {@code 500 (Internal Server Error)} if the player couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players")
    public ResponseEntity<Player> updatePlayer(@Valid @RequestBody Player player) throws URISyntaxException {
        log.debug("REST request to update Player : {}", player);
        if (player.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Player result = playerService.save(player);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, player.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /players} : get all the players.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
     */
    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers(PlayerCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Players by criteria: {}", criteria);
        Page<Player> page = playerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /players/all} : get all the players without pagination
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
     */
    @GetMapping("/players/all")
    public ResponseEntity<List<Player>> getAllPlayer(PlayerCriteria criteria) {
        log.debug("REST request to get Players without pagination by criteria: {}", criteria);
        List<Player> players = playerQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(players);
    }


    /**
    * {@code GET  /players/count} : count all the players.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/players/count")
    public ResponseEntity<Long> countPlayers(PlayerCriteria criteria) {
        log.debug("REST request to count Players by criteria: {}", criteria);
        return ResponseEntity.ok().body(playerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /players/:id} : get the "id" player.
     *
     * @param id the id of the player to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the player, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long id) {
        log.debug("REST request to get Player : {}", id);
        Optional<Player> player = playerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(player);
    }

    /**
     * {@code DELETE  /players/:id} : delete the "id" player.
     *
     * @param id the id of the player to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        log.debug("REST request to delete Player : {}", id);
        playerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
