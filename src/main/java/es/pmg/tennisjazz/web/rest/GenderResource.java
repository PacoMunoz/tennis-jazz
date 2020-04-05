package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.domain.Gender;
import es.pmg.tennisjazz.service.GenderService;
import es.pmg.tennisjazz.web.rest.errors.BadRequestAlertException;
import es.pmg.tennisjazz.service.dto.GenderCriteria;
import es.pmg.tennisjazz.service.GenderQueryService;

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
 * REST controller for managing {@link es.pmg.tennisjazz.domain.Gender}.
 */
@RestController
@RequestMapping("/api")
public class GenderResource {

    private final Logger log = LoggerFactory.getLogger(GenderResource.class);

    private static final String ENTITY_NAME = "gender";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenderService genderService;

    private final GenderQueryService genderQueryService;

    public GenderResource(GenderService genderService, GenderQueryService genderQueryService) {
        this.genderService = genderService;
        this.genderQueryService = genderQueryService;
    }

    /**
     * {@code POST  /genders} : Create a new gender.
     *
     * @param gender the gender to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gender, or with status {@code 400 (Bad Request)} if the gender has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/genders")
    public ResponseEntity<Gender> createGender(@Valid @RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to save Gender : {}", gender);
        if (gender.getId() != null) {
            throw new BadRequestAlertException("A new gender cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gender result = genderService.save(gender);
        return ResponseEntity.created(new URI("/api/genders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /genders} : Updates an existing gender.
     *
     * @param gender the gender to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gender,
     * or with status {@code 400 (Bad Request)} if the gender is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gender couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/genders")
    public ResponseEntity<Gender> updateGender(@Valid @RequestBody Gender gender) throws URISyntaxException {
        log.debug("REST request to update Gender : {}", gender);
        if (gender.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Gender result = genderService.save(gender);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gender.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /genders} : get all the genders.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genders in body.
     */
    @GetMapping("/genders")
    public ResponseEntity<List<Gender>> getAllGenders(GenderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Genders by criteria: {}", criteria);
        Page<Gender> page = genderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /genders/count} : count all the genders.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/genders/count")
    public ResponseEntity<Long> countGenders(GenderCriteria criteria) {
        log.debug("REST request to count Genders by criteria: {}", criteria);
        return ResponseEntity.ok().body(genderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /genders/:id} : get the "id" gender.
     *
     * @param id the id of the gender to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gender, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/genders/{id}")
    public ResponseEntity<Gender> getGender(@PathVariable Long id) {
        log.debug("REST request to get Gender : {}", id);
        Optional<Gender> gender = genderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gender);
    }

    /**
     * {@code DELETE  /genders/:id} : delete the "id" gender.
     *
     * @param id the id of the gender to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/genders/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Long id) {
        log.debug("REST request to delete Gender : {}", id);
        genderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
