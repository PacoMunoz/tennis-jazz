package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.repository.RoundRepository;
import es.pmg.tennisjazz.service.RoundService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;
import es.pmg.tennisjazz.service.dto.RoundCriteria;
import es.pmg.tennisjazz.service.RoundQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static es.pmg.tennisjazz.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RoundResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class RoundResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private RoundService roundService;

    @Autowired
    private RoundQueryService roundQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRoundMockMvc;

    private Round round;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoundResource roundResource = new RoundResource(roundService, roundQueryService);
        this.restRoundMockMvc = MockMvcBuilders.standaloneSetup(roundResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Round createEntity(EntityManager em) {
        Round round = new Round()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return round;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Round createUpdatedEntity(EntityManager em) {
        Round round = new Round()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return round;
    }

    @BeforeEach
    public void initTest() {
        round = createEntity(em);
    }

    @Test
    @Transactional
    public void createRound() throws Exception {
        int databaseSizeBeforeCreate = roundRepository.findAll().size();

        // Create the Round
        restRoundMockMvc.perform(post("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(round)))
            .andExpect(status().isCreated());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeCreate + 1);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRound.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testRound.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createRoundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roundRepository.findAll().size();

        // Create the Round with an existing ID
        round.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoundMockMvc.perform(post("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(round)))
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roundRepository.findAll().size();
        // set the field null
        round.setName(null);

        // Create the Round, which fails.

        restRoundMockMvc.perform(post("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(round)))
            .andExpect(status().isBadRequest());

        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRounds() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList
        restRoundMockMvc.perform(get("/api/rounds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(round.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getRound() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get the round
        restRoundMockMvc.perform(get("/api/rounds/{id}", round.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(round.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllRoundsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where name equals to DEFAULT_NAME
        defaultRoundShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the roundList where name equals to UPDATED_NAME
        defaultRoundShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRoundsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRoundShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the roundList where name equals to UPDATED_NAME
        defaultRoundShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRoundsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where name is not null
        defaultRoundShouldBeFound("name.specified=true");

        // Get all the roundList where name is null
        defaultRoundShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate equals to DEFAULT_START_DATE
        defaultRoundShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the roundList where startDate equals to UPDATED_START_DATE
        defaultRoundShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultRoundShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the roundList where startDate equals to UPDATED_START_DATE
        defaultRoundShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate is not null
        defaultRoundShouldBeFound("startDate.specified=true");

        // Get all the roundList where startDate is null
        defaultRoundShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultRoundShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the roundList where startDate is greater than or equal to UPDATED_START_DATE
        defaultRoundShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate is less than or equal to DEFAULT_START_DATE
        defaultRoundShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the roundList where startDate is less than or equal to SMALLER_START_DATE
        defaultRoundShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate is less than DEFAULT_START_DATE
        defaultRoundShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the roundList where startDate is less than UPDATED_START_DATE
        defaultRoundShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where startDate is greater than DEFAULT_START_DATE
        defaultRoundShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the roundList where startDate is greater than SMALLER_START_DATE
        defaultRoundShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllRoundsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate equals to DEFAULT_END_DATE
        defaultRoundShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the roundList where endDate equals to UPDATED_END_DATE
        defaultRoundShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultRoundShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the roundList where endDate equals to UPDATED_END_DATE
        defaultRoundShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate is not null
        defaultRoundShouldBeFound("endDate.specified=true");

        // Get all the roundList where endDate is null
        defaultRoundShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRoundsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultRoundShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the roundList where endDate is greater than or equal to UPDATED_END_DATE
        defaultRoundShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate is less than or equal to DEFAULT_END_DATE
        defaultRoundShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the roundList where endDate is less than or equal to SMALLER_END_DATE
        defaultRoundShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate is less than DEFAULT_END_DATE
        defaultRoundShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the roundList where endDate is less than UPDATED_END_DATE
        defaultRoundShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllRoundsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);

        // Get all the roundList where endDate is greater than DEFAULT_END_DATE
        defaultRoundShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the roundList where endDate is greater than SMALLER_END_DATE
        defaultRoundShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllRoundsByTournamentGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);
        TournamentGroup tournamentGroup = TournamentGroupResourceIT.createEntity(em);
        em.persist(tournamentGroup);
        em.flush();
        round.setTournamentGroup(tournamentGroup);
        roundRepository.saveAndFlush(round);
        Long tournamentGroupId = tournamentGroup.getId();

        // Get all the roundList where tournamentGroup equals to tournamentGroupId
        defaultRoundShouldBeFound("tournamentGroupId.equals=" + tournamentGroupId);

        // Get all the roundList where tournamentGroup equals to tournamentGroupId + 1
        defaultRoundShouldNotBeFound("tournamentGroupId.equals=" + (tournamentGroupId + 1));
    }


    @Test
    @Transactional
    public void getAllRoundsByMatchesIsEqualToSomething() throws Exception {
        // Initialize the database
        roundRepository.saveAndFlush(round);
        Match matches = MatchResourceIT.createEntity(em);
        em.persist(matches);
        em.flush();
        round.addMatches(matches);
        roundRepository.saveAndFlush(round);
        Long matchesId = matches.getId();

        // Get all the roundList where matches equals to matchesId
        defaultRoundShouldBeFound("matchesId.equals=" + matchesId);

        // Get all the roundList where matches equals to matchesId + 1
        defaultRoundShouldNotBeFound("matchesId.equals=" + (matchesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRoundShouldBeFound(String filter) throws Exception {
        restRoundMockMvc.perform(get("/api/rounds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(round.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restRoundMockMvc.perform(get("/api/rounds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRoundShouldNotBeFound(String filter) throws Exception {
        restRoundMockMvc.perform(get("/api/rounds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRoundMockMvc.perform(get("/api/rounds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRound() throws Exception {
        // Get the round
        restRoundMockMvc.perform(get("/api/rounds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRound() throws Exception {
        // Initialize the database
        roundService.save(round);

        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Update the round
        Round updatedRound = roundRepository.findById(round.getId()).get();
        // Disconnect from session so that the updates on updatedRound are not directly saved in db
        em.detach(updatedRound);
        updatedRound
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restRoundMockMvc.perform(put("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRound)))
            .andExpect(status().isOk());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
        Round testRound = roundList.get(roundList.size() - 1);
        assertThat(testRound.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRound.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testRound.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingRound() throws Exception {
        int databaseSizeBeforeUpdate = roundRepository.findAll().size();

        // Create the Round

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoundMockMvc.perform(put("/api/rounds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(round)))
            .andExpect(status().isBadRequest());

        // Validate the Round in the database
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRound() throws Exception {
        // Initialize the database
        roundService.save(round);

        int databaseSizeBeforeDelete = roundRepository.findAll().size();

        // Delete the round
        restRoundMockMvc.perform(delete("/api/rounds/{id}", round.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Round> roundList = roundRepository.findAll();
        assertThat(roundList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Round.class);
        Round round1 = new Round();
        round1.setId(1L);
        Round round2 = new Round();
        round2.setId(round1.getId());
        assertThat(round1).isEqualTo(round2);
        round2.setId(2L);
        assertThat(round1).isNotEqualTo(round2);
        round1.setId(null);
        assertThat(round1).isNotEqualTo(round2);
    }
}
