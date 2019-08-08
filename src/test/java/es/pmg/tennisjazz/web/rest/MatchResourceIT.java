package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.repository.MatchRepository;
import es.pmg.tennisjazz.service.MatchService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;
import es.pmg.tennisjazz.service.dto.MatchCriteria;
import es.pmg.tennisjazz.service.MatchQueryService;

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
import java.util.List;

import static es.pmg.tennisjazz.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MatchResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class MatchResourceIT {

    private static final Integer DEFAULT_LOCAL_PLAYER_SET_1_RESULT = 1;
    private static final Integer UPDATED_LOCAL_PLAYER_SET_1_RESULT = 2;

    private static final Integer DEFAULT_VISITOR_PLAYER_SET_1_RESULT = 1;
    private static final Integer UPDATED_VISITOR_PLAYER_SET_1_RESULT = 2;

    private static final Integer DEFAULT_LOCAL_PLAYER_SET_2_RESULT = 1;
    private static final Integer UPDATED_LOCAL_PLAYER_SET_2_RESULT = 2;

    private static final Integer DEFAULT_VISITOR_PLAYER_SET_2_RESULT = 1;
    private static final Integer UPDATED_VISITOR_PLAYER_SET_2_RESULT = 2;

    private static final Integer DEFAULT_LOCAL_PLAYER_SET_3_RESULT = 1;
    private static final Integer UPDATED_LOCAL_PLAYER_SET_3_RESULT = 2;

    private static final Integer DEFAULT_VISITOR_PLAYER_SET_3_RESULT = 1;
    private static final Integer UPDATED_VISITOR_PLAYER_SET_3_RESULT = 2;

    private static final Integer DEFAULT_LOCAL_PLAYER_SETS = 1;
    private static final Integer UPDATED_LOCAL_PLAYER_SETS = 2;

    private static final Integer DEFAULT_VISITOR_PLAYER_SETS = 1;
    private static final Integer UPDATED_VISITOR_PLAYER_SETS = 2;

    private static final Boolean DEFAULT_LOCAL_PLAYER_ABANDONED = false;
    private static final Boolean UPDATED_LOCAL_PLAYER_ABANDONED = true;

    private static final Boolean DEFAULT_VISITOR_PLAYER_ABANDONED = false;
    private static final Boolean UPDATED_VISITOR_PLAYER_ABANDONED = true;

    private static final Boolean DEFAULT_LOCAL_PLAYER_NOT_PRESENT = false;
    private static final Boolean UPDATED_LOCAL_PLAYER_NOT_PRESENT = true;

    private static final Boolean DEFAULT_VISITOR_PLAYER_NOT_PRESENT = false;
    private static final Boolean UPDATED_VISITOR_PLAYER_NOT_PRESENT = true;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchQueryService matchQueryService;

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

    private MockMvc restMatchMockMvc;

    private Match match;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MatchResource matchResource = new MatchResource(matchService, matchQueryService);
        this.restMatchMockMvc = MockMvcBuilders.standaloneSetup(matchResource)
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
    public static Match createEntity(EntityManager em) {
        Match match = new Match()
            .localPlayerSet1Result(DEFAULT_LOCAL_PLAYER_SET_1_RESULT)
            .visitorPlayerSet1Result(DEFAULT_VISITOR_PLAYER_SET_1_RESULT)
            .localPlayerSet2Result(DEFAULT_LOCAL_PLAYER_SET_2_RESULT)
            .visitorPlayerSet2Result(DEFAULT_VISITOR_PLAYER_SET_2_RESULT)
            .localPlayerSet3Result(DEFAULT_LOCAL_PLAYER_SET_3_RESULT)
            .visitorPlayerSet3Result(DEFAULT_VISITOR_PLAYER_SET_3_RESULT)
            .localPlayerSets(DEFAULT_LOCAL_PLAYER_SETS)
            .visitorPlayerSets(DEFAULT_VISITOR_PLAYER_SETS)
            .localPlayerAbandoned(DEFAULT_LOCAL_PLAYER_ABANDONED)
            .visitorPlayerAbandoned(DEFAULT_VISITOR_PLAYER_ABANDONED)
            .localPlayerNotPresent(DEFAULT_LOCAL_PLAYER_NOT_PRESENT)
            .visitorPlayerNotPresent(DEFAULT_VISITOR_PLAYER_NOT_PRESENT);
        return match;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Match createUpdatedEntity(EntityManager em) {
        Match match = new Match()
            .localPlayerSet1Result(UPDATED_LOCAL_PLAYER_SET_1_RESULT)
            .visitorPlayerSet1Result(UPDATED_VISITOR_PLAYER_SET_1_RESULT)
            .localPlayerSet2Result(UPDATED_LOCAL_PLAYER_SET_2_RESULT)
            .visitorPlayerSet2Result(UPDATED_VISITOR_PLAYER_SET_2_RESULT)
            .localPlayerSet3Result(UPDATED_LOCAL_PLAYER_SET_3_RESULT)
            .visitorPlayerSet3Result(UPDATED_VISITOR_PLAYER_SET_3_RESULT)
            .localPlayerSets(UPDATED_LOCAL_PLAYER_SETS)
            .visitorPlayerSets(UPDATED_VISITOR_PLAYER_SETS)
            .localPlayerAbandoned(UPDATED_LOCAL_PLAYER_ABANDONED)
            .visitorPlayerAbandoned(UPDATED_VISITOR_PLAYER_ABANDONED)
            .localPlayerNotPresent(UPDATED_LOCAL_PLAYER_NOT_PRESENT)
            .visitorPlayerNotPresent(UPDATED_VISITOR_PLAYER_NOT_PRESENT);
        return match;
    }

    @BeforeEach
    public void initTest() {
        match = createEntity(em);
    }

    @Test
    @Transactional
    public void createMatch() throws Exception {
        int databaseSizeBeforeCreate = matchRepository.findAll().size();

        // Create the Match
        restMatchMockMvc.perform(post("/api/matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(match)))
            .andExpect(status().isCreated());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeCreate + 1);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getLocalPlayerSet1Result()).isEqualTo(DEFAULT_LOCAL_PLAYER_SET_1_RESULT);
        assertThat(testMatch.getVisitorPlayerSet1Result()).isEqualTo(DEFAULT_VISITOR_PLAYER_SET_1_RESULT);
        assertThat(testMatch.getLocalPlayerSet2Result()).isEqualTo(DEFAULT_LOCAL_PLAYER_SET_2_RESULT);
        assertThat(testMatch.getVisitorPlayerSet2Result()).isEqualTo(DEFAULT_VISITOR_PLAYER_SET_2_RESULT);
        assertThat(testMatch.getLocalPlayerSet3Result()).isEqualTo(DEFAULT_LOCAL_PLAYER_SET_3_RESULT);
        assertThat(testMatch.getVisitorPlayerSet3Result()).isEqualTo(DEFAULT_VISITOR_PLAYER_SET_3_RESULT);
        assertThat(testMatch.getLocalPlayerSets()).isEqualTo(DEFAULT_LOCAL_PLAYER_SETS);
        assertThat(testMatch.getVisitorPlayerSets()).isEqualTo(DEFAULT_VISITOR_PLAYER_SETS);
        assertThat(testMatch.isLocalPlayerAbandoned()).isEqualTo(DEFAULT_LOCAL_PLAYER_ABANDONED);
        assertThat(testMatch.isVisitorPlayerAbandoned()).isEqualTo(DEFAULT_VISITOR_PLAYER_ABANDONED);
        assertThat(testMatch.isLocalPlayerNotPresent()).isEqualTo(DEFAULT_LOCAL_PLAYER_NOT_PRESENT);
        assertThat(testMatch.isVisitorPlayerNotPresent()).isEqualTo(DEFAULT_VISITOR_PLAYER_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void createMatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = matchRepository.findAll().size();

        // Create the Match with an existing ID
        match.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchMockMvc.perform(post("/api/matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(match)))
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMatches() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList
        restMatchMockMvc.perform(get("/api/matches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(match.getId().intValue())))
            .andExpect(jsonPath("$.[*].localPlayerSet1Result").value(hasItem(DEFAULT_LOCAL_PLAYER_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].visitorPlayerSet1Result").value(hasItem(DEFAULT_VISITOR_PLAYER_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].localPlayerSet2Result").value(hasItem(DEFAULT_LOCAL_PLAYER_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].visitorPlayerSet2Result").value(hasItem(DEFAULT_VISITOR_PLAYER_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].localPlayerSet3Result").value(hasItem(DEFAULT_LOCAL_PLAYER_SET_3_RESULT)))
            .andExpect(jsonPath("$.[*].visitorPlayerSet3Result").value(hasItem(DEFAULT_VISITOR_PLAYER_SET_3_RESULT)))
            .andExpect(jsonPath("$.[*].localPlayerSets").value(hasItem(DEFAULT_LOCAL_PLAYER_SETS)))
            .andExpect(jsonPath("$.[*].visitorPlayerSets").value(hasItem(DEFAULT_VISITOR_PLAYER_SETS)))
            .andExpect(jsonPath("$.[*].localPlayerAbandoned").value(hasItem(DEFAULT_LOCAL_PLAYER_ABANDONED.booleanValue())))
            .andExpect(jsonPath("$.[*].visitorPlayerAbandoned").value(hasItem(DEFAULT_VISITOR_PLAYER_ABANDONED.booleanValue())))
            .andExpect(jsonPath("$.[*].localPlayerNotPresent").value(hasItem(DEFAULT_LOCAL_PLAYER_NOT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].visitorPlayerNotPresent").value(hasItem(DEFAULT_VISITOR_PLAYER_NOT_PRESENT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get the match
        restMatchMockMvc.perform(get("/api/matches/{id}", match.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(match.getId().intValue()))
            .andExpect(jsonPath("$.localPlayerSet1Result").value(DEFAULT_LOCAL_PLAYER_SET_1_RESULT))
            .andExpect(jsonPath("$.visitorPlayerSet1Result").value(DEFAULT_VISITOR_PLAYER_SET_1_RESULT))
            .andExpect(jsonPath("$.localPlayerSet2Result").value(DEFAULT_LOCAL_PLAYER_SET_2_RESULT))
            .andExpect(jsonPath("$.visitorPlayerSet2Result").value(DEFAULT_VISITOR_PLAYER_SET_2_RESULT))
            .andExpect(jsonPath("$.localPlayerSet3Result").value(DEFAULT_LOCAL_PLAYER_SET_3_RESULT))
            .andExpect(jsonPath("$.visitorPlayerSet3Result").value(DEFAULT_VISITOR_PLAYER_SET_3_RESULT))
            .andExpect(jsonPath("$.localPlayerSets").value(DEFAULT_LOCAL_PLAYER_SETS))
            .andExpect(jsonPath("$.visitorPlayerSets").value(DEFAULT_VISITOR_PLAYER_SETS))
            .andExpect(jsonPath("$.localPlayerAbandoned").value(DEFAULT_LOCAL_PLAYER_ABANDONED.booleanValue()))
            .andExpect(jsonPath("$.visitorPlayerAbandoned").value(DEFAULT_VISITOR_PLAYER_ABANDONED.booleanValue()))
            .andExpect(jsonPath("$.localPlayerNotPresent").value(DEFAULT_LOCAL_PLAYER_NOT_PRESENT.booleanValue()))
            .andExpect(jsonPath("$.visitorPlayerNotPresent").value(DEFAULT_VISITOR_PLAYER_NOT_PRESENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet1ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet1Result equals to DEFAULT_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("localPlayerSet1Result.equals=" + DEFAULT_LOCAL_PLAYER_SET_1_RESULT);

        // Get all the matchList where localPlayerSet1Result equals to UPDATED_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet1Result.equals=" + UPDATED_LOCAL_PLAYER_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet1ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet1Result in DEFAULT_LOCAL_PLAYER_SET_1_RESULT or UPDATED_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("localPlayerSet1Result.in=" + DEFAULT_LOCAL_PLAYER_SET_1_RESULT + "," + UPDATED_LOCAL_PLAYER_SET_1_RESULT);

        // Get all the matchList where localPlayerSet1Result equals to UPDATED_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet1Result.in=" + UPDATED_LOCAL_PLAYER_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet1ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet1Result is not null
        defaultMatchShouldBeFound("localPlayerSet1Result.specified=true");

        // Get all the matchList where localPlayerSet1Result is null
        defaultMatchShouldNotBeFound("localPlayerSet1Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet1ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet1Result greater than or equals to DEFAULT_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("localPlayerSet1Result.greaterOrEqualThan=" + DEFAULT_LOCAL_PLAYER_SET_1_RESULT);

        // Get all the matchList where localPlayerSet1Result greater than or equals to UPDATED_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet1Result.greaterOrEqualThan=" + UPDATED_LOCAL_PLAYER_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet1ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet1Result less than or equals to DEFAULT_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet1Result.lessThan=" + DEFAULT_LOCAL_PLAYER_SET_1_RESULT);

        // Get all the matchList where localPlayerSet1Result less than or equals to UPDATED_LOCAL_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("localPlayerSet1Result.lessThan=" + UPDATED_LOCAL_PLAYER_SET_1_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet1ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet1Result equals to DEFAULT_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet1Result.equals=" + DEFAULT_VISITOR_PLAYER_SET_1_RESULT);

        // Get all the matchList where visitorPlayerSet1Result equals to UPDATED_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet1Result.equals=" + UPDATED_VISITOR_PLAYER_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet1ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet1Result in DEFAULT_VISITOR_PLAYER_SET_1_RESULT or UPDATED_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet1Result.in=" + DEFAULT_VISITOR_PLAYER_SET_1_RESULT + "," + UPDATED_VISITOR_PLAYER_SET_1_RESULT);

        // Get all the matchList where visitorPlayerSet1Result equals to UPDATED_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet1Result.in=" + UPDATED_VISITOR_PLAYER_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet1ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet1Result is not null
        defaultMatchShouldBeFound("visitorPlayerSet1Result.specified=true");

        // Get all the matchList where visitorPlayerSet1Result is null
        defaultMatchShouldNotBeFound("visitorPlayerSet1Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet1ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet1Result greater than or equals to DEFAULT_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet1Result.greaterOrEqualThan=" + DEFAULT_VISITOR_PLAYER_SET_1_RESULT);

        // Get all the matchList where visitorPlayerSet1Result greater than or equals to UPDATED_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet1Result.greaterOrEqualThan=" + UPDATED_VISITOR_PLAYER_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet1ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet1Result less than or equals to DEFAULT_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet1Result.lessThan=" + DEFAULT_VISITOR_PLAYER_SET_1_RESULT);

        // Get all the matchList where visitorPlayerSet1Result less than or equals to UPDATED_VISITOR_PLAYER_SET_1_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet1Result.lessThan=" + UPDATED_VISITOR_PLAYER_SET_1_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet2ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet2Result equals to DEFAULT_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("localPlayerSet2Result.equals=" + DEFAULT_LOCAL_PLAYER_SET_2_RESULT);

        // Get all the matchList where localPlayerSet2Result equals to UPDATED_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet2Result.equals=" + UPDATED_LOCAL_PLAYER_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet2ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet2Result in DEFAULT_LOCAL_PLAYER_SET_2_RESULT or UPDATED_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("localPlayerSet2Result.in=" + DEFAULT_LOCAL_PLAYER_SET_2_RESULT + "," + UPDATED_LOCAL_PLAYER_SET_2_RESULT);

        // Get all the matchList where localPlayerSet2Result equals to UPDATED_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet2Result.in=" + UPDATED_LOCAL_PLAYER_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet2ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet2Result is not null
        defaultMatchShouldBeFound("localPlayerSet2Result.specified=true");

        // Get all the matchList where localPlayerSet2Result is null
        defaultMatchShouldNotBeFound("localPlayerSet2Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet2ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet2Result greater than or equals to DEFAULT_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("localPlayerSet2Result.greaterOrEqualThan=" + DEFAULT_LOCAL_PLAYER_SET_2_RESULT);

        // Get all the matchList where localPlayerSet2Result greater than or equals to UPDATED_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet2Result.greaterOrEqualThan=" + UPDATED_LOCAL_PLAYER_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet2ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet2Result less than or equals to DEFAULT_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet2Result.lessThan=" + DEFAULT_LOCAL_PLAYER_SET_2_RESULT);

        // Get all the matchList where localPlayerSet2Result less than or equals to UPDATED_LOCAL_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("localPlayerSet2Result.lessThan=" + UPDATED_LOCAL_PLAYER_SET_2_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet2ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet2Result equals to DEFAULT_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet2Result.equals=" + DEFAULT_VISITOR_PLAYER_SET_2_RESULT);

        // Get all the matchList where visitorPlayerSet2Result equals to UPDATED_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet2Result.equals=" + UPDATED_VISITOR_PLAYER_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet2ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet2Result in DEFAULT_VISITOR_PLAYER_SET_2_RESULT or UPDATED_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet2Result.in=" + DEFAULT_VISITOR_PLAYER_SET_2_RESULT + "," + UPDATED_VISITOR_PLAYER_SET_2_RESULT);

        // Get all the matchList where visitorPlayerSet2Result equals to UPDATED_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet2Result.in=" + UPDATED_VISITOR_PLAYER_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet2ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet2Result is not null
        defaultMatchShouldBeFound("visitorPlayerSet2Result.specified=true");

        // Get all the matchList where visitorPlayerSet2Result is null
        defaultMatchShouldNotBeFound("visitorPlayerSet2Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet2ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet2Result greater than or equals to DEFAULT_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet2Result.greaterOrEqualThan=" + DEFAULT_VISITOR_PLAYER_SET_2_RESULT);

        // Get all the matchList where visitorPlayerSet2Result greater than or equals to UPDATED_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet2Result.greaterOrEqualThan=" + UPDATED_VISITOR_PLAYER_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet2ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet2Result less than or equals to DEFAULT_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet2Result.lessThan=" + DEFAULT_VISITOR_PLAYER_SET_2_RESULT);

        // Get all the matchList where visitorPlayerSet2Result less than or equals to UPDATED_VISITOR_PLAYER_SET_2_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet2Result.lessThan=" + UPDATED_VISITOR_PLAYER_SET_2_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet3ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet3Result equals to DEFAULT_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("localPlayerSet3Result.equals=" + DEFAULT_LOCAL_PLAYER_SET_3_RESULT);

        // Get all the matchList where localPlayerSet3Result equals to UPDATED_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet3Result.equals=" + UPDATED_LOCAL_PLAYER_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet3ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet3Result in DEFAULT_LOCAL_PLAYER_SET_3_RESULT or UPDATED_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("localPlayerSet3Result.in=" + DEFAULT_LOCAL_PLAYER_SET_3_RESULT + "," + UPDATED_LOCAL_PLAYER_SET_3_RESULT);

        // Get all the matchList where localPlayerSet3Result equals to UPDATED_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet3Result.in=" + UPDATED_LOCAL_PLAYER_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet3ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet3Result is not null
        defaultMatchShouldBeFound("localPlayerSet3Result.specified=true");

        // Get all the matchList where localPlayerSet3Result is null
        defaultMatchShouldNotBeFound("localPlayerSet3Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet3ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet3Result greater than or equals to DEFAULT_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("localPlayerSet3Result.greaterOrEqualThan=" + DEFAULT_LOCAL_PLAYER_SET_3_RESULT);

        // Get all the matchList where localPlayerSet3Result greater than or equals to UPDATED_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet3Result.greaterOrEqualThan=" + UPDATED_LOCAL_PLAYER_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSet3ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSet3Result less than or equals to DEFAULT_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("localPlayerSet3Result.lessThan=" + DEFAULT_LOCAL_PLAYER_SET_3_RESULT);

        // Get all the matchList where localPlayerSet3Result less than or equals to UPDATED_LOCAL_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("localPlayerSet3Result.lessThan=" + UPDATED_LOCAL_PLAYER_SET_3_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet3ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet3Result equals to DEFAULT_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet3Result.equals=" + DEFAULT_VISITOR_PLAYER_SET_3_RESULT);

        // Get all the matchList where visitorPlayerSet3Result equals to UPDATED_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet3Result.equals=" + UPDATED_VISITOR_PLAYER_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet3ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet3Result in DEFAULT_VISITOR_PLAYER_SET_3_RESULT or UPDATED_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet3Result.in=" + DEFAULT_VISITOR_PLAYER_SET_3_RESULT + "," + UPDATED_VISITOR_PLAYER_SET_3_RESULT);

        // Get all the matchList where visitorPlayerSet3Result equals to UPDATED_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet3Result.in=" + UPDATED_VISITOR_PLAYER_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet3ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet3Result is not null
        defaultMatchShouldBeFound("visitorPlayerSet3Result.specified=true");

        // Get all the matchList where visitorPlayerSet3Result is null
        defaultMatchShouldNotBeFound("visitorPlayerSet3Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet3ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet3Result greater than or equals to DEFAULT_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet3Result.greaterOrEqualThan=" + DEFAULT_VISITOR_PLAYER_SET_3_RESULT);

        // Get all the matchList where visitorPlayerSet3Result greater than or equals to UPDATED_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet3Result.greaterOrEqualThan=" + UPDATED_VISITOR_PLAYER_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSet3ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSet3Result less than or equals to DEFAULT_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldNotBeFound("visitorPlayerSet3Result.lessThan=" + DEFAULT_VISITOR_PLAYER_SET_3_RESULT);

        // Get all the matchList where visitorPlayerSet3Result less than or equals to UPDATED_VISITOR_PLAYER_SET_3_RESULT
        defaultMatchShouldBeFound("visitorPlayerSet3Result.lessThan=" + UPDATED_VISITOR_PLAYER_SET_3_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSetsIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSets equals to DEFAULT_LOCAL_PLAYER_SETS
        defaultMatchShouldBeFound("localPlayerSets.equals=" + DEFAULT_LOCAL_PLAYER_SETS);

        // Get all the matchList where localPlayerSets equals to UPDATED_LOCAL_PLAYER_SETS
        defaultMatchShouldNotBeFound("localPlayerSets.equals=" + UPDATED_LOCAL_PLAYER_SETS);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSetsIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSets in DEFAULT_LOCAL_PLAYER_SETS or UPDATED_LOCAL_PLAYER_SETS
        defaultMatchShouldBeFound("localPlayerSets.in=" + DEFAULT_LOCAL_PLAYER_SETS + "," + UPDATED_LOCAL_PLAYER_SETS);

        // Get all the matchList where localPlayerSets equals to UPDATED_LOCAL_PLAYER_SETS
        defaultMatchShouldNotBeFound("localPlayerSets.in=" + UPDATED_LOCAL_PLAYER_SETS);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSetsIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSets is not null
        defaultMatchShouldBeFound("localPlayerSets.specified=true");

        // Get all the matchList where localPlayerSets is null
        defaultMatchShouldNotBeFound("localPlayerSets.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSetsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSets greater than or equals to DEFAULT_LOCAL_PLAYER_SETS
        defaultMatchShouldBeFound("localPlayerSets.greaterOrEqualThan=" + DEFAULT_LOCAL_PLAYER_SETS);

        // Get all the matchList where localPlayerSets greater than or equals to UPDATED_LOCAL_PLAYER_SETS
        defaultMatchShouldNotBeFound("localPlayerSets.greaterOrEqualThan=" + UPDATED_LOCAL_PLAYER_SETS);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerSetsIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerSets less than or equals to DEFAULT_LOCAL_PLAYER_SETS
        defaultMatchShouldNotBeFound("localPlayerSets.lessThan=" + DEFAULT_LOCAL_PLAYER_SETS);

        // Get all the matchList where localPlayerSets less than or equals to UPDATED_LOCAL_PLAYER_SETS
        defaultMatchShouldBeFound("localPlayerSets.lessThan=" + UPDATED_LOCAL_PLAYER_SETS);
    }


    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSetsIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSets equals to DEFAULT_VISITOR_PLAYER_SETS
        defaultMatchShouldBeFound("visitorPlayerSets.equals=" + DEFAULT_VISITOR_PLAYER_SETS);

        // Get all the matchList where visitorPlayerSets equals to UPDATED_VISITOR_PLAYER_SETS
        defaultMatchShouldNotBeFound("visitorPlayerSets.equals=" + UPDATED_VISITOR_PLAYER_SETS);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSetsIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSets in DEFAULT_VISITOR_PLAYER_SETS or UPDATED_VISITOR_PLAYER_SETS
        defaultMatchShouldBeFound("visitorPlayerSets.in=" + DEFAULT_VISITOR_PLAYER_SETS + "," + UPDATED_VISITOR_PLAYER_SETS);

        // Get all the matchList where visitorPlayerSets equals to UPDATED_VISITOR_PLAYER_SETS
        defaultMatchShouldNotBeFound("visitorPlayerSets.in=" + UPDATED_VISITOR_PLAYER_SETS);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSetsIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSets is not null
        defaultMatchShouldBeFound("visitorPlayerSets.specified=true");

        // Get all the matchList where visitorPlayerSets is null
        defaultMatchShouldNotBeFound("visitorPlayerSets.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSetsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSets greater than or equals to DEFAULT_VISITOR_PLAYER_SETS
        defaultMatchShouldBeFound("visitorPlayerSets.greaterOrEqualThan=" + DEFAULT_VISITOR_PLAYER_SETS);

        // Get all the matchList where visitorPlayerSets greater than or equals to UPDATED_VISITOR_PLAYER_SETS
        defaultMatchShouldNotBeFound("visitorPlayerSets.greaterOrEqualThan=" + UPDATED_VISITOR_PLAYER_SETS);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerSetsIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerSets less than or equals to DEFAULT_VISITOR_PLAYER_SETS
        defaultMatchShouldNotBeFound("visitorPlayerSets.lessThan=" + DEFAULT_VISITOR_PLAYER_SETS);

        // Get all the matchList where visitorPlayerSets less than or equals to UPDATED_VISITOR_PLAYER_SETS
        defaultMatchShouldBeFound("visitorPlayerSets.lessThan=" + UPDATED_VISITOR_PLAYER_SETS);
    }


    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerAbandonedIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerAbandoned equals to DEFAULT_LOCAL_PLAYER_ABANDONED
        defaultMatchShouldBeFound("localPlayerAbandoned.equals=" + DEFAULT_LOCAL_PLAYER_ABANDONED);

        // Get all the matchList where localPlayerAbandoned equals to UPDATED_LOCAL_PLAYER_ABANDONED
        defaultMatchShouldNotBeFound("localPlayerAbandoned.equals=" + UPDATED_LOCAL_PLAYER_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerAbandonedIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerAbandoned in DEFAULT_LOCAL_PLAYER_ABANDONED or UPDATED_LOCAL_PLAYER_ABANDONED
        defaultMatchShouldBeFound("localPlayerAbandoned.in=" + DEFAULT_LOCAL_PLAYER_ABANDONED + "," + UPDATED_LOCAL_PLAYER_ABANDONED);

        // Get all the matchList where localPlayerAbandoned equals to UPDATED_LOCAL_PLAYER_ABANDONED
        defaultMatchShouldNotBeFound("localPlayerAbandoned.in=" + UPDATED_LOCAL_PLAYER_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerAbandonedIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerAbandoned is not null
        defaultMatchShouldBeFound("localPlayerAbandoned.specified=true");

        // Get all the matchList where localPlayerAbandoned is null
        defaultMatchShouldNotBeFound("localPlayerAbandoned.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerAbandonedIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerAbandoned equals to DEFAULT_VISITOR_PLAYER_ABANDONED
        defaultMatchShouldBeFound("visitorPlayerAbandoned.equals=" + DEFAULT_VISITOR_PLAYER_ABANDONED);

        // Get all the matchList where visitorPlayerAbandoned equals to UPDATED_VISITOR_PLAYER_ABANDONED
        defaultMatchShouldNotBeFound("visitorPlayerAbandoned.equals=" + UPDATED_VISITOR_PLAYER_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerAbandonedIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerAbandoned in DEFAULT_VISITOR_PLAYER_ABANDONED or UPDATED_VISITOR_PLAYER_ABANDONED
        defaultMatchShouldBeFound("visitorPlayerAbandoned.in=" + DEFAULT_VISITOR_PLAYER_ABANDONED + "," + UPDATED_VISITOR_PLAYER_ABANDONED);

        // Get all the matchList where visitorPlayerAbandoned equals to UPDATED_VISITOR_PLAYER_ABANDONED
        defaultMatchShouldNotBeFound("visitorPlayerAbandoned.in=" + UPDATED_VISITOR_PLAYER_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerAbandonedIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerAbandoned is not null
        defaultMatchShouldBeFound("visitorPlayerAbandoned.specified=true");

        // Get all the matchList where visitorPlayerAbandoned is null
        defaultMatchShouldNotBeFound("visitorPlayerAbandoned.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerNotPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerNotPresent equals to DEFAULT_LOCAL_PLAYER_NOT_PRESENT
        defaultMatchShouldBeFound("localPlayerNotPresent.equals=" + DEFAULT_LOCAL_PLAYER_NOT_PRESENT);

        // Get all the matchList where localPlayerNotPresent equals to UPDATED_LOCAL_PLAYER_NOT_PRESENT
        defaultMatchShouldNotBeFound("localPlayerNotPresent.equals=" + UPDATED_LOCAL_PLAYER_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerNotPresentIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerNotPresent in DEFAULT_LOCAL_PLAYER_NOT_PRESENT or UPDATED_LOCAL_PLAYER_NOT_PRESENT
        defaultMatchShouldBeFound("localPlayerNotPresent.in=" + DEFAULT_LOCAL_PLAYER_NOT_PRESENT + "," + UPDATED_LOCAL_PLAYER_NOT_PRESENT);

        // Get all the matchList where localPlayerNotPresent equals to UPDATED_LOCAL_PLAYER_NOT_PRESENT
        defaultMatchShouldNotBeFound("localPlayerNotPresent.in=" + UPDATED_LOCAL_PLAYER_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerNotPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where localPlayerNotPresent is not null
        defaultMatchShouldBeFound("localPlayerNotPresent.specified=true");

        // Get all the matchList where localPlayerNotPresent is null
        defaultMatchShouldNotBeFound("localPlayerNotPresent.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerNotPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerNotPresent equals to DEFAULT_VISITOR_PLAYER_NOT_PRESENT
        defaultMatchShouldBeFound("visitorPlayerNotPresent.equals=" + DEFAULT_VISITOR_PLAYER_NOT_PRESENT);

        // Get all the matchList where visitorPlayerNotPresent equals to UPDATED_VISITOR_PLAYER_NOT_PRESENT
        defaultMatchShouldNotBeFound("visitorPlayerNotPresent.equals=" + UPDATED_VISITOR_PLAYER_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerNotPresentIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerNotPresent in DEFAULT_VISITOR_PLAYER_NOT_PRESENT or UPDATED_VISITOR_PLAYER_NOT_PRESENT
        defaultMatchShouldBeFound("visitorPlayerNotPresent.in=" + DEFAULT_VISITOR_PLAYER_NOT_PRESENT + "," + UPDATED_VISITOR_PLAYER_NOT_PRESENT);

        // Get all the matchList where visitorPlayerNotPresent equals to UPDATED_VISITOR_PLAYER_NOT_PRESENT
        defaultMatchShouldNotBeFound("visitorPlayerNotPresent.in=" + UPDATED_VISITOR_PLAYER_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerNotPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where visitorPlayerNotPresent is not null
        defaultMatchShouldBeFound("visitorPlayerNotPresent.specified=true");

        // Get all the matchList where visitorPlayerNotPresent is null
        defaultMatchShouldNotBeFound("visitorPlayerNotPresent.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByRoundIsEqualToSomething() throws Exception {
        // Initialize the database
        Round round = RoundResourceIT.createEntity(em);
        em.persist(round);
        em.flush();
        match.setRound(round);
        matchRepository.saveAndFlush(match);
        Long roundId = round.getId();

        // Get all the matchList where round equals to roundId
        defaultMatchShouldBeFound("roundId.equals=" + roundId);

        // Get all the matchList where round equals to roundId + 1
        defaultMatchShouldNotBeFound("roundId.equals=" + (roundId + 1));
    }


    @Test
    @Transactional
    public void getAllMatchesByVisitorPlayerIsEqualToSomething() throws Exception {
        // Initialize the database
        Player visitorPlayer = PlayerResourceIT.createEntity(em);
        em.persist(visitorPlayer);
        em.flush();
        match.setVisitorPlayer(visitorPlayer);
        matchRepository.saveAndFlush(match);
        Long visitorPlayerId = visitorPlayer.getId();

        // Get all the matchList where visitorPlayer equals to visitorPlayerId
        defaultMatchShouldBeFound("visitorPlayerId.equals=" + visitorPlayerId);

        // Get all the matchList where visitorPlayer equals to visitorPlayerId + 1
        defaultMatchShouldNotBeFound("visitorPlayerId.equals=" + (visitorPlayerId + 1));
    }


    @Test
    @Transactional
    public void getAllMatchesByLocalPlayerIsEqualToSomething() throws Exception {
        // Initialize the database
        Player localPlayer = PlayerResourceIT.createEntity(em);
        em.persist(localPlayer);
        em.flush();
        match.setLocalPlayer(localPlayer);
        matchRepository.saveAndFlush(match);
        Long localPlayerId = localPlayer.getId();

        // Get all the matchList where localPlayer equals to localPlayerId
        defaultMatchShouldBeFound("localPlayerId.equals=" + localPlayerId);

        // Get all the matchList where localPlayer equals to localPlayerId + 1
        defaultMatchShouldNotBeFound("localPlayerId.equals=" + (localPlayerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMatchShouldBeFound(String filter) throws Exception {
        restMatchMockMvc.perform(get("/api/matches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(match.getId().intValue())))
            .andExpect(jsonPath("$.[*].localPlayerSet1Result").value(hasItem(DEFAULT_LOCAL_PLAYER_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].visitorPlayerSet1Result").value(hasItem(DEFAULT_VISITOR_PLAYER_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].localPlayerSet2Result").value(hasItem(DEFAULT_LOCAL_PLAYER_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].visitorPlayerSet2Result").value(hasItem(DEFAULT_VISITOR_PLAYER_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].localPlayerSet3Result").value(hasItem(DEFAULT_LOCAL_PLAYER_SET_3_RESULT)))
            .andExpect(jsonPath("$.[*].visitorPlayerSet3Result").value(hasItem(DEFAULT_VISITOR_PLAYER_SET_3_RESULT)))
            .andExpect(jsonPath("$.[*].localPlayerSets").value(hasItem(DEFAULT_LOCAL_PLAYER_SETS)))
            .andExpect(jsonPath("$.[*].visitorPlayerSets").value(hasItem(DEFAULT_VISITOR_PLAYER_SETS)))
            .andExpect(jsonPath("$.[*].localPlayerAbandoned").value(hasItem(DEFAULT_LOCAL_PLAYER_ABANDONED.booleanValue())))
            .andExpect(jsonPath("$.[*].visitorPlayerAbandoned").value(hasItem(DEFAULT_VISITOR_PLAYER_ABANDONED.booleanValue())))
            .andExpect(jsonPath("$.[*].localPlayerNotPresent").value(hasItem(DEFAULT_LOCAL_PLAYER_NOT_PRESENT.booleanValue())))
            .andExpect(jsonPath("$.[*].visitorPlayerNotPresent").value(hasItem(DEFAULT_VISITOR_PLAYER_NOT_PRESENT.booleanValue())));

        // Check, that the count call also returns 1
        restMatchMockMvc.perform(get("/api/matches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMatchShouldNotBeFound(String filter) throws Exception {
        restMatchMockMvc.perform(get("/api/matches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMatchMockMvc.perform(get("/api/matches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMatch() throws Exception {
        // Get the match
        restMatchMockMvc.perform(get("/api/matches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMatch() throws Exception {
        // Initialize the database
        matchService.save(match);

        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Update the match
        Match updatedMatch = matchRepository.findById(match.getId()).get();
        // Disconnect from session so that the updates on updatedMatch are not directly saved in db
        em.detach(updatedMatch);
        updatedMatch
            .localPlayerSet1Result(UPDATED_LOCAL_PLAYER_SET_1_RESULT)
            .visitorPlayerSet1Result(UPDATED_VISITOR_PLAYER_SET_1_RESULT)
            .localPlayerSet2Result(UPDATED_LOCAL_PLAYER_SET_2_RESULT)
            .visitorPlayerSet2Result(UPDATED_VISITOR_PLAYER_SET_2_RESULT)
            .localPlayerSet3Result(UPDATED_LOCAL_PLAYER_SET_3_RESULT)
            .visitorPlayerSet3Result(UPDATED_VISITOR_PLAYER_SET_3_RESULT)
            .localPlayerSets(UPDATED_LOCAL_PLAYER_SETS)
            .visitorPlayerSets(UPDATED_VISITOR_PLAYER_SETS)
            .localPlayerAbandoned(UPDATED_LOCAL_PLAYER_ABANDONED)
            .visitorPlayerAbandoned(UPDATED_VISITOR_PLAYER_ABANDONED)
            .localPlayerNotPresent(UPDATED_LOCAL_PLAYER_NOT_PRESENT)
            .visitorPlayerNotPresent(UPDATED_VISITOR_PLAYER_NOT_PRESENT);

        restMatchMockMvc.perform(put("/api/matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMatch)))
            .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getLocalPlayerSet1Result()).isEqualTo(UPDATED_LOCAL_PLAYER_SET_1_RESULT);
        assertThat(testMatch.getVisitorPlayerSet1Result()).isEqualTo(UPDATED_VISITOR_PLAYER_SET_1_RESULT);
        assertThat(testMatch.getLocalPlayerSet2Result()).isEqualTo(UPDATED_LOCAL_PLAYER_SET_2_RESULT);
        assertThat(testMatch.getVisitorPlayerSet2Result()).isEqualTo(UPDATED_VISITOR_PLAYER_SET_2_RESULT);
        assertThat(testMatch.getLocalPlayerSet3Result()).isEqualTo(UPDATED_LOCAL_PLAYER_SET_3_RESULT);
        assertThat(testMatch.getVisitorPlayerSet3Result()).isEqualTo(UPDATED_VISITOR_PLAYER_SET_3_RESULT);
        assertThat(testMatch.getLocalPlayerSets()).isEqualTo(UPDATED_LOCAL_PLAYER_SETS);
        assertThat(testMatch.getVisitorPlayerSets()).isEqualTo(UPDATED_VISITOR_PLAYER_SETS);
        assertThat(testMatch.isLocalPlayerAbandoned()).isEqualTo(UPDATED_LOCAL_PLAYER_ABANDONED);
        assertThat(testMatch.isVisitorPlayerAbandoned()).isEqualTo(UPDATED_VISITOR_PLAYER_ABANDONED);
        assertThat(testMatch.isLocalPlayerNotPresent()).isEqualTo(UPDATED_LOCAL_PLAYER_NOT_PRESENT);
        assertThat(testMatch.isVisitorPlayerNotPresent()).isEqualTo(UPDATED_VISITOR_PLAYER_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void updateNonExistingMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Create the Match

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchMockMvc.perform(put("/api/matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(match)))
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMatch() throws Exception {
        // Initialize the database
        matchService.save(match);

        int databaseSizeBeforeDelete = matchRepository.findAll().size();

        // Delete the match
        restMatchMockMvc.perform(delete("/api/matches/{id}", match.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Match.class);
        Match match1 = new Match();
        match1.setId(1L);
        Match match2 = new Match();
        match2.setId(match1.getId());
        assertThat(match1).isEqualTo(match2);
        match2.setId(2L);
        assertThat(match1).isNotEqualTo(match2);
        match1.setId(null);
        assertThat(match1).isNotEqualTo(match2);
    }
}
