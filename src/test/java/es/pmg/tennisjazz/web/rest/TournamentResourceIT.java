package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.Tournament;
import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.repository.TournamentRepository;
import es.pmg.tennisjazz.service.TournamentService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;
import es.pmg.tennisjazz.service.dto.TournamentCriteria;
import es.pmg.tennisjazz.service.TournamentQueryService;

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
 * Integration tests for the {@link TournamentResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class TournamentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IN_PROGRESS = false;
    private static final Boolean UPDATED_IN_PROGRESS = true;

    private static final Integer DEFAULT_WIN_POINTS = 1;
    private static final Integer UPDATED_WIN_POINTS = 2;
    private static final Integer SMALLER_WIN_POINTS = 1 - 1;

    private static final Integer DEFAULT_LOSS_POINTS = 1;
    private static final Integer UPDATED_LOSS_POINTS = 2;
    private static final Integer SMALLER_LOSS_POINTS = 1 - 1;

    private static final Integer DEFAULT_NOT_PRESENT_POINTS = 1;
    private static final Integer UPDATED_NOT_PRESENT_POINTS = 2;
    private static final Integer SMALLER_NOT_PRESENT_POINTS = 1 - 1;

    private static final Integer DEFAULT_INJURED_POINTS = 1;
    private static final Integer UPDATED_INJURED_POINTS = 2;
    private static final Integer SMALLER_INJURED_POINTS = 1 - 1;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private TournamentQueryService tournamentQueryService;

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

    private MockMvc restTournamentMockMvc;

    private Tournament tournament;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TournamentResource tournamentResource = new TournamentResource(tournamentService, tournamentQueryService);
        this.restTournamentMockMvc = MockMvcBuilders.standaloneSetup(tournamentResource)
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
    public static Tournament createEntity(EntityManager em) {
        Tournament tournament = new Tournament()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .inProgress(DEFAULT_IN_PROGRESS)
            .winPoints(DEFAULT_WIN_POINTS)
            .lossPoints(DEFAULT_LOSS_POINTS)
            .notPresentPoints(DEFAULT_NOT_PRESENT_POINTS)
            .injuredPoints(DEFAULT_INJURED_POINTS);
        return tournament;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createUpdatedEntity(EntityManager em) {
        Tournament tournament = new Tournament()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .inProgress(UPDATED_IN_PROGRESS)
            .winPoints(UPDATED_WIN_POINTS)
            .lossPoints(UPDATED_LOSS_POINTS)
            .notPresentPoints(UPDATED_NOT_PRESENT_POINTS)
            .injuredPoints(UPDATED_INJURED_POINTS);
        return tournament;
    }

    @BeforeEach
    public void initTest() {
        tournament = createEntity(em);
    }

    @Test
    @Transactional
    public void createTournament() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // Create the Tournament
        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isCreated());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate + 1);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTournament.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTournament.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTournament.isInProgress()).isEqualTo(DEFAULT_IN_PROGRESS);
        assertThat(testTournament.getWinPoints()).isEqualTo(DEFAULT_WIN_POINTS);
        assertThat(testTournament.getLossPoints()).isEqualTo(DEFAULT_LOSS_POINTS);
        assertThat(testTournament.getNotPresentPoints()).isEqualTo(DEFAULT_NOT_PRESENT_POINTS);
        assertThat(testTournament.getInjuredPoints()).isEqualTo(DEFAULT_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void createTournamentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // Create the Tournament with an existing ID
        tournament.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tournamentRepository.findAll().size();
        // set the field null
        tournament.setName(null);

        // Create the Tournament, which fails.

        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isBadRequest());

        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tournamentRepository.findAll().size();
        // set the field null
        tournament.setStartDate(null);

        // Create the Tournament, which fails.

        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isBadRequest());

        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWinPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = tournamentRepository.findAll().size();
        // set the field null
        tournament.setWinPoints(null);

        // Create the Tournament, which fails.

        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isBadRequest());

        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLossPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = tournamentRepository.findAll().size();
        // set the field null
        tournament.setLossPoints(null);

        // Create the Tournament, which fails.

        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isBadRequest());

        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTournaments() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList
        restTournamentMockMvc.perform(get("/api/tournaments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].inProgress").value(hasItem(DEFAULT_IN_PROGRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].winPoints").value(hasItem(DEFAULT_WIN_POINTS)))
            .andExpect(jsonPath("$.[*].lossPoints").value(hasItem(DEFAULT_LOSS_POINTS)))
            .andExpect(jsonPath("$.[*].notPresentPoints").value(hasItem(DEFAULT_NOT_PRESENT_POINTS)))
            .andExpect(jsonPath("$.[*].injuredPoints").value(hasItem(DEFAULT_INJURED_POINTS)));
    }
    
    @Test
    @Transactional
    public void getTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get the tournament
        restTournamentMockMvc.perform(get("/api/tournaments/{id}", tournament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tournament.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.inProgress").value(DEFAULT_IN_PROGRESS.booleanValue()))
            .andExpect(jsonPath("$.winPoints").value(DEFAULT_WIN_POINTS))
            .andExpect(jsonPath("$.lossPoints").value(DEFAULT_LOSS_POINTS))
            .andExpect(jsonPath("$.notPresentPoints").value(DEFAULT_NOT_PRESENT_POINTS))
            .andExpect(jsonPath("$.injuredPoints").value(DEFAULT_INJURED_POINTS));
    }

    @Test
    @Transactional
    public void getAllTournamentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name equals to DEFAULT_NAME
        defaultTournamentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tournamentList where name equals to UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTournamentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tournamentList where name equals to UPDATED_NAME
        defaultTournamentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where name is not null
        defaultTournamentShouldBeFound("name.specified=true");

        // Get all the tournamentList where name is null
        defaultTournamentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate equals to DEFAULT_START_DATE
        defaultTournamentShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the tournamentList where startDate equals to UPDATED_START_DATE
        defaultTournamentShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultTournamentShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the tournamentList where startDate equals to UPDATED_START_DATE
        defaultTournamentShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate is not null
        defaultTournamentShouldBeFound("startDate.specified=true");

        // Get all the tournamentList where startDate is null
        defaultTournamentShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultTournamentShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the tournamentList where startDate is greater than or equal to UPDATED_START_DATE
        defaultTournamentShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate is less than or equal to DEFAULT_START_DATE
        defaultTournamentShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the tournamentList where startDate is less than or equal to SMALLER_START_DATE
        defaultTournamentShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate is less than DEFAULT_START_DATE
        defaultTournamentShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the tournamentList where startDate is less than UPDATED_START_DATE
        defaultTournamentShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where startDate is greater than DEFAULT_START_DATE
        defaultTournamentShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the tournamentList where startDate is greater than SMALLER_START_DATE
        defaultTournamentShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate equals to DEFAULT_END_DATE
        defaultTournamentShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the tournamentList where endDate equals to UPDATED_END_DATE
        defaultTournamentShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultTournamentShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the tournamentList where endDate equals to UPDATED_END_DATE
        defaultTournamentShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate is not null
        defaultTournamentShouldBeFound("endDate.specified=true");

        // Get all the tournamentList where endDate is null
        defaultTournamentShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultTournamentShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the tournamentList where endDate is greater than or equal to UPDATED_END_DATE
        defaultTournamentShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate is less than or equal to DEFAULT_END_DATE
        defaultTournamentShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the tournamentList where endDate is less than or equal to SMALLER_END_DATE
        defaultTournamentShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate is less than DEFAULT_END_DATE
        defaultTournamentShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the tournamentList where endDate is less than UPDATED_END_DATE
        defaultTournamentShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTournamentsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where endDate is greater than DEFAULT_END_DATE
        defaultTournamentShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the tournamentList where endDate is greater than SMALLER_END_DATE
        defaultTournamentShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllTournamentsByInProgressIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where inProgress equals to DEFAULT_IN_PROGRESS
        defaultTournamentShouldBeFound("inProgress.equals=" + DEFAULT_IN_PROGRESS);

        // Get all the tournamentList where inProgress equals to UPDATED_IN_PROGRESS
        defaultTournamentShouldNotBeFound("inProgress.equals=" + UPDATED_IN_PROGRESS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInProgressIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where inProgress in DEFAULT_IN_PROGRESS or UPDATED_IN_PROGRESS
        defaultTournamentShouldBeFound("inProgress.in=" + DEFAULT_IN_PROGRESS + "," + UPDATED_IN_PROGRESS);

        // Get all the tournamentList where inProgress equals to UPDATED_IN_PROGRESS
        defaultTournamentShouldNotBeFound("inProgress.in=" + UPDATED_IN_PROGRESS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInProgressIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where inProgress is not null
        defaultTournamentShouldBeFound("inProgress.specified=true");

        // Get all the tournamentList where inProgress is null
        defaultTournamentShouldNotBeFound("inProgress.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints equals to DEFAULT_WIN_POINTS
        defaultTournamentShouldBeFound("winPoints.equals=" + DEFAULT_WIN_POINTS);

        // Get all the tournamentList where winPoints equals to UPDATED_WIN_POINTS
        defaultTournamentShouldNotBeFound("winPoints.equals=" + UPDATED_WIN_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints in DEFAULT_WIN_POINTS or UPDATED_WIN_POINTS
        defaultTournamentShouldBeFound("winPoints.in=" + DEFAULT_WIN_POINTS + "," + UPDATED_WIN_POINTS);

        // Get all the tournamentList where winPoints equals to UPDATED_WIN_POINTS
        defaultTournamentShouldNotBeFound("winPoints.in=" + UPDATED_WIN_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints is not null
        defaultTournamentShouldBeFound("winPoints.specified=true");

        // Get all the tournamentList where winPoints is null
        defaultTournamentShouldNotBeFound("winPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints is greater than or equal to DEFAULT_WIN_POINTS
        defaultTournamentShouldBeFound("winPoints.greaterThanOrEqual=" + DEFAULT_WIN_POINTS);

        // Get all the tournamentList where winPoints is greater than or equal to UPDATED_WIN_POINTS
        defaultTournamentShouldNotBeFound("winPoints.greaterThanOrEqual=" + UPDATED_WIN_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints is less than or equal to DEFAULT_WIN_POINTS
        defaultTournamentShouldBeFound("winPoints.lessThanOrEqual=" + DEFAULT_WIN_POINTS);

        // Get all the tournamentList where winPoints is less than or equal to SMALLER_WIN_POINTS
        defaultTournamentShouldNotBeFound("winPoints.lessThanOrEqual=" + SMALLER_WIN_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints is less than DEFAULT_WIN_POINTS
        defaultTournamentShouldNotBeFound("winPoints.lessThan=" + DEFAULT_WIN_POINTS);

        // Get all the tournamentList where winPoints is less than UPDATED_WIN_POINTS
        defaultTournamentShouldBeFound("winPoints.lessThan=" + UPDATED_WIN_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByWinPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where winPoints is greater than DEFAULT_WIN_POINTS
        defaultTournamentShouldNotBeFound("winPoints.greaterThan=" + DEFAULT_WIN_POINTS);

        // Get all the tournamentList where winPoints is greater than SMALLER_WIN_POINTS
        defaultTournamentShouldBeFound("winPoints.greaterThan=" + SMALLER_WIN_POINTS);
    }


    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints equals to DEFAULT_LOSS_POINTS
        defaultTournamentShouldBeFound("lossPoints.equals=" + DEFAULT_LOSS_POINTS);

        // Get all the tournamentList where lossPoints equals to UPDATED_LOSS_POINTS
        defaultTournamentShouldNotBeFound("lossPoints.equals=" + UPDATED_LOSS_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints in DEFAULT_LOSS_POINTS or UPDATED_LOSS_POINTS
        defaultTournamentShouldBeFound("lossPoints.in=" + DEFAULT_LOSS_POINTS + "," + UPDATED_LOSS_POINTS);

        // Get all the tournamentList where lossPoints equals to UPDATED_LOSS_POINTS
        defaultTournamentShouldNotBeFound("lossPoints.in=" + UPDATED_LOSS_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints is not null
        defaultTournamentShouldBeFound("lossPoints.specified=true");

        // Get all the tournamentList where lossPoints is null
        defaultTournamentShouldNotBeFound("lossPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints is greater than or equal to DEFAULT_LOSS_POINTS
        defaultTournamentShouldBeFound("lossPoints.greaterThanOrEqual=" + DEFAULT_LOSS_POINTS);

        // Get all the tournamentList where lossPoints is greater than or equal to UPDATED_LOSS_POINTS
        defaultTournamentShouldNotBeFound("lossPoints.greaterThanOrEqual=" + UPDATED_LOSS_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints is less than or equal to DEFAULT_LOSS_POINTS
        defaultTournamentShouldBeFound("lossPoints.lessThanOrEqual=" + DEFAULT_LOSS_POINTS);

        // Get all the tournamentList where lossPoints is less than or equal to SMALLER_LOSS_POINTS
        defaultTournamentShouldNotBeFound("lossPoints.lessThanOrEqual=" + SMALLER_LOSS_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints is less than DEFAULT_LOSS_POINTS
        defaultTournamentShouldNotBeFound("lossPoints.lessThan=" + DEFAULT_LOSS_POINTS);

        // Get all the tournamentList where lossPoints is less than UPDATED_LOSS_POINTS
        defaultTournamentShouldBeFound("lossPoints.lessThan=" + UPDATED_LOSS_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByLossPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where lossPoints is greater than DEFAULT_LOSS_POINTS
        defaultTournamentShouldNotBeFound("lossPoints.greaterThan=" + DEFAULT_LOSS_POINTS);

        // Get all the tournamentList where lossPoints is greater than SMALLER_LOSS_POINTS
        defaultTournamentShouldBeFound("lossPoints.greaterThan=" + SMALLER_LOSS_POINTS);
    }


    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints equals to DEFAULT_NOT_PRESENT_POINTS
        defaultTournamentShouldBeFound("notPresentPoints.equals=" + DEFAULT_NOT_PRESENT_POINTS);

        // Get all the tournamentList where notPresentPoints equals to UPDATED_NOT_PRESENT_POINTS
        defaultTournamentShouldNotBeFound("notPresentPoints.equals=" + UPDATED_NOT_PRESENT_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints in DEFAULT_NOT_PRESENT_POINTS or UPDATED_NOT_PRESENT_POINTS
        defaultTournamentShouldBeFound("notPresentPoints.in=" + DEFAULT_NOT_PRESENT_POINTS + "," + UPDATED_NOT_PRESENT_POINTS);

        // Get all the tournamentList where notPresentPoints equals to UPDATED_NOT_PRESENT_POINTS
        defaultTournamentShouldNotBeFound("notPresentPoints.in=" + UPDATED_NOT_PRESENT_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints is not null
        defaultTournamentShouldBeFound("notPresentPoints.specified=true");

        // Get all the tournamentList where notPresentPoints is null
        defaultTournamentShouldNotBeFound("notPresentPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints is greater than or equal to DEFAULT_NOT_PRESENT_POINTS
        defaultTournamentShouldBeFound("notPresentPoints.greaterThanOrEqual=" + DEFAULT_NOT_PRESENT_POINTS);

        // Get all the tournamentList where notPresentPoints is greater than or equal to UPDATED_NOT_PRESENT_POINTS
        defaultTournamentShouldNotBeFound("notPresentPoints.greaterThanOrEqual=" + UPDATED_NOT_PRESENT_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints is less than or equal to DEFAULT_NOT_PRESENT_POINTS
        defaultTournamentShouldBeFound("notPresentPoints.lessThanOrEqual=" + DEFAULT_NOT_PRESENT_POINTS);

        // Get all the tournamentList where notPresentPoints is less than or equal to SMALLER_NOT_PRESENT_POINTS
        defaultTournamentShouldNotBeFound("notPresentPoints.lessThanOrEqual=" + SMALLER_NOT_PRESENT_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints is less than DEFAULT_NOT_PRESENT_POINTS
        defaultTournamentShouldNotBeFound("notPresentPoints.lessThan=" + DEFAULT_NOT_PRESENT_POINTS);

        // Get all the tournamentList where notPresentPoints is less than UPDATED_NOT_PRESENT_POINTS
        defaultTournamentShouldBeFound("notPresentPoints.lessThan=" + UPDATED_NOT_PRESENT_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByNotPresentPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where notPresentPoints is greater than DEFAULT_NOT_PRESENT_POINTS
        defaultTournamentShouldNotBeFound("notPresentPoints.greaterThan=" + DEFAULT_NOT_PRESENT_POINTS);

        // Get all the tournamentList where notPresentPoints is greater than SMALLER_NOT_PRESENT_POINTS
        defaultTournamentShouldBeFound("notPresentPoints.greaterThan=" + SMALLER_NOT_PRESENT_POINTS);
    }


    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints equals to DEFAULT_INJURED_POINTS
        defaultTournamentShouldBeFound("injuredPoints.equals=" + DEFAULT_INJURED_POINTS);

        // Get all the tournamentList where injuredPoints equals to UPDATED_INJURED_POINTS
        defaultTournamentShouldNotBeFound("injuredPoints.equals=" + UPDATED_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints in DEFAULT_INJURED_POINTS or UPDATED_INJURED_POINTS
        defaultTournamentShouldBeFound("injuredPoints.in=" + DEFAULT_INJURED_POINTS + "," + UPDATED_INJURED_POINTS);

        // Get all the tournamentList where injuredPoints equals to UPDATED_INJURED_POINTS
        defaultTournamentShouldNotBeFound("injuredPoints.in=" + UPDATED_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints is not null
        defaultTournamentShouldBeFound("injuredPoints.specified=true");

        // Get all the tournamentList where injuredPoints is null
        defaultTournamentShouldNotBeFound("injuredPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints is greater than or equal to DEFAULT_INJURED_POINTS
        defaultTournamentShouldBeFound("injuredPoints.greaterThanOrEqual=" + DEFAULT_INJURED_POINTS);

        // Get all the tournamentList where injuredPoints is greater than or equal to UPDATED_INJURED_POINTS
        defaultTournamentShouldNotBeFound("injuredPoints.greaterThanOrEqual=" + UPDATED_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints is less than or equal to DEFAULT_INJURED_POINTS
        defaultTournamentShouldBeFound("injuredPoints.lessThanOrEqual=" + DEFAULT_INJURED_POINTS);

        // Get all the tournamentList where injuredPoints is less than or equal to SMALLER_INJURED_POINTS
        defaultTournamentShouldNotBeFound("injuredPoints.lessThanOrEqual=" + SMALLER_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints is less than DEFAULT_INJURED_POINTS
        defaultTournamentShouldNotBeFound("injuredPoints.lessThan=" + DEFAULT_INJURED_POINTS);

        // Get all the tournamentList where injuredPoints is less than UPDATED_INJURED_POINTS
        defaultTournamentShouldBeFound("injuredPoints.lessThan=" + UPDATED_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTournamentsByInjuredPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList where injuredPoints is greater than DEFAULT_INJURED_POINTS
        defaultTournamentShouldNotBeFound("injuredPoints.greaterThan=" + DEFAULT_INJURED_POINTS);

        // Get all the tournamentList where injuredPoints is greater than SMALLER_INJURED_POINTS
        defaultTournamentShouldBeFound("injuredPoints.greaterThan=" + SMALLER_INJURED_POINTS);
    }


    @Test
    @Transactional
    public void getAllTournamentsByGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);
        TournamentGroup groups = TournamentGroupResourceIT.createEntity(em);
        em.persist(groups);
        em.flush();
        tournament.addGroups(groups);
        tournamentRepository.saveAndFlush(tournament);
        Long groupsId = groups.getId();

        // Get all the tournamentList where groups equals to groupsId
        defaultTournamentShouldBeFound("groupsId.equals=" + groupsId);

        // Get all the tournamentList where groups equals to groupsId + 1
        defaultTournamentShouldNotBeFound("groupsId.equals=" + (groupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTournamentShouldBeFound(String filter) throws Exception {
        restTournamentMockMvc.perform(get("/api/tournaments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].inProgress").value(hasItem(DEFAULT_IN_PROGRESS.booleanValue())))
            .andExpect(jsonPath("$.[*].winPoints").value(hasItem(DEFAULT_WIN_POINTS)))
            .andExpect(jsonPath("$.[*].lossPoints").value(hasItem(DEFAULT_LOSS_POINTS)))
            .andExpect(jsonPath("$.[*].notPresentPoints").value(hasItem(DEFAULT_NOT_PRESENT_POINTS)))
            .andExpect(jsonPath("$.[*].injuredPoints").value(hasItem(DEFAULT_INJURED_POINTS)));

        // Check, that the count call also returns 1
        restTournamentMockMvc.perform(get("/api/tournaments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTournamentShouldNotBeFound(String filter) throws Exception {
        restTournamentMockMvc.perform(get("/api/tournaments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTournamentMockMvc.perform(get("/api/tournaments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTournament() throws Exception {
        // Get the tournament
        restTournamentMockMvc.perform(get("/api/tournaments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTournament() throws Exception {
        // Initialize the database
        tournamentService.save(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament
        Tournament updatedTournament = tournamentRepository.findById(tournament.getId()).get();
        // Disconnect from session so that the updates on updatedTournament are not directly saved in db
        em.detach(updatedTournament);
        updatedTournament
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .inProgress(UPDATED_IN_PROGRESS)
            .winPoints(UPDATED_WIN_POINTS)
            .lossPoints(UPDATED_LOSS_POINTS)
            .notPresentPoints(UPDATED_NOT_PRESENT_POINTS)
            .injuredPoints(UPDATED_INJURED_POINTS);

        restTournamentMockMvc.perform(put("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTournament)))
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTournament.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTournament.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTournament.isInProgress()).isEqualTo(UPDATED_IN_PROGRESS);
        assertThat(testTournament.getWinPoints()).isEqualTo(UPDATED_WIN_POINTS);
        assertThat(testTournament.getLossPoints()).isEqualTo(UPDATED_LOSS_POINTS);
        assertThat(testTournament.getNotPresentPoints()).isEqualTo(UPDATED_NOT_PRESENT_POINTS);
        assertThat(testTournament.getInjuredPoints()).isEqualTo(UPDATED_INJURED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Create the Tournament

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentMockMvc.perform(put("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournament)))
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTournament() throws Exception {
        // Initialize the database
        tournamentService.save(tournament);

        int databaseSizeBeforeDelete = tournamentRepository.findAll().size();

        // Delete the tournament
        restTournamentMockMvc.perform(delete("/api/tournaments/{id}", tournament.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tournament.class);
        Tournament tournament1 = new Tournament();
        tournament1.setId(1L);
        Tournament tournament2 = new Tournament();
        tournament2.setId(tournament1.getId());
        assertThat(tournament1).isEqualTo(tournament2);
        tournament2.setId(2L);
        assertThat(tournament1).isNotEqualTo(tournament2);
        tournament1.setId(null);
        assertThat(tournament1).isNotEqualTo(tournament2);
    }
}
