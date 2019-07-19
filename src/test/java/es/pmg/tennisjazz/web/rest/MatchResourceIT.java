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

    private static final Integer DEFAULT_PLAYER_1_SET_1_RESULT = 1;
    private static final Integer UPDATED_PLAYER_1_SET_1_RESULT = 2;

    private static final Integer DEFAULT_PLAYER_2_SET_1_RESULT = 1;
    private static final Integer UPDATED_PLAYER_2_SET_1_RESULT = 2;

    private static final Integer DEFAULT_PLAYER_1_SET_2_RESULT = 1;
    private static final Integer UPDATED_PLAYER_1_SET_2_RESULT = 2;

    private static final Integer DEFAULT_PLAYER_2_SET_2_RESULT = 1;
    private static final Integer UPDATED_PLAYER_2_SET_2_RESULT = 2;

    private static final Integer DEFAULT_PLAYER_1_SET_3_RESULT = 1;
    private static final Integer UPDATED_PLAYER_1_SET_3_RESULT = 2;

    private static final Integer DEFAULT_PLAYER_2_SET_3_RESULT = 1;
    private static final Integer UPDATED_PLAYER_2_SET_3_RESULT = 2;

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
            .player1Set1Result(DEFAULT_PLAYER_1_SET_1_RESULT)
            .player2Set1Result(DEFAULT_PLAYER_2_SET_1_RESULT)
            .player1Set2Result(DEFAULT_PLAYER_1_SET_2_RESULT)
            .player2Set2Result(DEFAULT_PLAYER_2_SET_2_RESULT)
            .player1Set3Result(DEFAULT_PLAYER_1_SET_3_RESULT)
            .player2Set3Result(DEFAULT_PLAYER_2_SET_3_RESULT);
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
            .player1Set1Result(UPDATED_PLAYER_1_SET_1_RESULT)
            .player2Set1Result(UPDATED_PLAYER_2_SET_1_RESULT)
            .player1Set2Result(UPDATED_PLAYER_1_SET_2_RESULT)
            .player2Set2Result(UPDATED_PLAYER_2_SET_2_RESULT)
            .player1Set3Result(UPDATED_PLAYER_1_SET_3_RESULT)
            .player2Set3Result(UPDATED_PLAYER_2_SET_3_RESULT);
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
        assertThat(testMatch.getPlayer1Set1Result()).isEqualTo(DEFAULT_PLAYER_1_SET_1_RESULT);
        assertThat(testMatch.getPlayer2Set1Result()).isEqualTo(DEFAULT_PLAYER_2_SET_1_RESULT);
        assertThat(testMatch.getPlayer1Set2Result()).isEqualTo(DEFAULT_PLAYER_1_SET_2_RESULT);
        assertThat(testMatch.getPlayer2Set2Result()).isEqualTo(DEFAULT_PLAYER_2_SET_2_RESULT);
        assertThat(testMatch.getPlayer1Set3Result()).isEqualTo(DEFAULT_PLAYER_1_SET_3_RESULT);
        assertThat(testMatch.getPlayer2Set3Result()).isEqualTo(DEFAULT_PLAYER_2_SET_3_RESULT);
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
            .andExpect(jsonPath("$.[*].player1Set1Result").value(hasItem(DEFAULT_PLAYER_1_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].player2Set1Result").value(hasItem(DEFAULT_PLAYER_2_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].player1Set2Result").value(hasItem(DEFAULT_PLAYER_1_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].player2Set2Result").value(hasItem(DEFAULT_PLAYER_2_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].player1Set3Result").value(hasItem(DEFAULT_PLAYER_1_SET_3_RESULT)))
            .andExpect(jsonPath("$.[*].player2Set3Result").value(hasItem(DEFAULT_PLAYER_2_SET_3_RESULT)));
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
            .andExpect(jsonPath("$.player1Set1Result").value(DEFAULT_PLAYER_1_SET_1_RESULT))
            .andExpect(jsonPath("$.player2Set1Result").value(DEFAULT_PLAYER_2_SET_1_RESULT))
            .andExpect(jsonPath("$.player1Set2Result").value(DEFAULT_PLAYER_1_SET_2_RESULT))
            .andExpect(jsonPath("$.player2Set2Result").value(DEFAULT_PLAYER_2_SET_2_RESULT))
            .andExpect(jsonPath("$.player1Set3Result").value(DEFAULT_PLAYER_1_SET_3_RESULT))
            .andExpect(jsonPath("$.player2Set3Result").value(DEFAULT_PLAYER_2_SET_3_RESULT));
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set1ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set1Result equals to DEFAULT_PLAYER_1_SET_1_RESULT
        defaultMatchShouldBeFound("player1Set1Result.equals=" + DEFAULT_PLAYER_1_SET_1_RESULT);

        // Get all the matchList where player1Set1Result equals to UPDATED_PLAYER_1_SET_1_RESULT
        defaultMatchShouldNotBeFound("player1Set1Result.equals=" + UPDATED_PLAYER_1_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set1ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set1Result in DEFAULT_PLAYER_1_SET_1_RESULT or UPDATED_PLAYER_1_SET_1_RESULT
        defaultMatchShouldBeFound("player1Set1Result.in=" + DEFAULT_PLAYER_1_SET_1_RESULT + "," + UPDATED_PLAYER_1_SET_1_RESULT);

        // Get all the matchList where player1Set1Result equals to UPDATED_PLAYER_1_SET_1_RESULT
        defaultMatchShouldNotBeFound("player1Set1Result.in=" + UPDATED_PLAYER_1_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set1ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set1Result is not null
        defaultMatchShouldBeFound("player1Set1Result.specified=true");

        // Get all the matchList where player1Set1Result is null
        defaultMatchShouldNotBeFound("player1Set1Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set1ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set1Result greater than or equals to DEFAULT_PLAYER_1_SET_1_RESULT
        defaultMatchShouldBeFound("player1Set1Result.greaterOrEqualThan=" + DEFAULT_PLAYER_1_SET_1_RESULT);

        // Get all the matchList where player1Set1Result greater than or equals to UPDATED_PLAYER_1_SET_1_RESULT
        defaultMatchShouldNotBeFound("player1Set1Result.greaterOrEqualThan=" + UPDATED_PLAYER_1_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set1ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set1Result less than or equals to DEFAULT_PLAYER_1_SET_1_RESULT
        defaultMatchShouldNotBeFound("player1Set1Result.lessThan=" + DEFAULT_PLAYER_1_SET_1_RESULT);

        // Get all the matchList where player1Set1Result less than or equals to UPDATED_PLAYER_1_SET_1_RESULT
        defaultMatchShouldBeFound("player1Set1Result.lessThan=" + UPDATED_PLAYER_1_SET_1_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set1ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set1Result equals to DEFAULT_PLAYER_2_SET_1_RESULT
        defaultMatchShouldBeFound("player2Set1Result.equals=" + DEFAULT_PLAYER_2_SET_1_RESULT);

        // Get all the matchList where player2Set1Result equals to UPDATED_PLAYER_2_SET_1_RESULT
        defaultMatchShouldNotBeFound("player2Set1Result.equals=" + UPDATED_PLAYER_2_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set1ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set1Result in DEFAULT_PLAYER_2_SET_1_RESULT or UPDATED_PLAYER_2_SET_1_RESULT
        defaultMatchShouldBeFound("player2Set1Result.in=" + DEFAULT_PLAYER_2_SET_1_RESULT + "," + UPDATED_PLAYER_2_SET_1_RESULT);

        // Get all the matchList where player2Set1Result equals to UPDATED_PLAYER_2_SET_1_RESULT
        defaultMatchShouldNotBeFound("player2Set1Result.in=" + UPDATED_PLAYER_2_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set1ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set1Result is not null
        defaultMatchShouldBeFound("player2Set1Result.specified=true");

        // Get all the matchList where player2Set1Result is null
        defaultMatchShouldNotBeFound("player2Set1Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set1ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set1Result greater than or equals to DEFAULT_PLAYER_2_SET_1_RESULT
        defaultMatchShouldBeFound("player2Set1Result.greaterOrEqualThan=" + DEFAULT_PLAYER_2_SET_1_RESULT);

        // Get all the matchList where player2Set1Result greater than or equals to UPDATED_PLAYER_2_SET_1_RESULT
        defaultMatchShouldNotBeFound("player2Set1Result.greaterOrEqualThan=" + UPDATED_PLAYER_2_SET_1_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set1ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set1Result less than or equals to DEFAULT_PLAYER_2_SET_1_RESULT
        defaultMatchShouldNotBeFound("player2Set1Result.lessThan=" + DEFAULT_PLAYER_2_SET_1_RESULT);

        // Get all the matchList where player2Set1Result less than or equals to UPDATED_PLAYER_2_SET_1_RESULT
        defaultMatchShouldBeFound("player2Set1Result.lessThan=" + UPDATED_PLAYER_2_SET_1_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set2ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set2Result equals to DEFAULT_PLAYER_1_SET_2_RESULT
        defaultMatchShouldBeFound("player1Set2Result.equals=" + DEFAULT_PLAYER_1_SET_2_RESULT);

        // Get all the matchList where player1Set2Result equals to UPDATED_PLAYER_1_SET_2_RESULT
        defaultMatchShouldNotBeFound("player1Set2Result.equals=" + UPDATED_PLAYER_1_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set2ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set2Result in DEFAULT_PLAYER_1_SET_2_RESULT or UPDATED_PLAYER_1_SET_2_RESULT
        defaultMatchShouldBeFound("player1Set2Result.in=" + DEFAULT_PLAYER_1_SET_2_RESULT + "," + UPDATED_PLAYER_1_SET_2_RESULT);

        // Get all the matchList where player1Set2Result equals to UPDATED_PLAYER_1_SET_2_RESULT
        defaultMatchShouldNotBeFound("player1Set2Result.in=" + UPDATED_PLAYER_1_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set2ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set2Result is not null
        defaultMatchShouldBeFound("player1Set2Result.specified=true");

        // Get all the matchList where player1Set2Result is null
        defaultMatchShouldNotBeFound("player1Set2Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set2ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set2Result greater than or equals to DEFAULT_PLAYER_1_SET_2_RESULT
        defaultMatchShouldBeFound("player1Set2Result.greaterOrEqualThan=" + DEFAULT_PLAYER_1_SET_2_RESULT);

        // Get all the matchList where player1Set2Result greater than or equals to UPDATED_PLAYER_1_SET_2_RESULT
        defaultMatchShouldNotBeFound("player1Set2Result.greaterOrEqualThan=" + UPDATED_PLAYER_1_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set2ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set2Result less than or equals to DEFAULT_PLAYER_1_SET_2_RESULT
        defaultMatchShouldNotBeFound("player1Set2Result.lessThan=" + DEFAULT_PLAYER_1_SET_2_RESULT);

        // Get all the matchList where player1Set2Result less than or equals to UPDATED_PLAYER_1_SET_2_RESULT
        defaultMatchShouldBeFound("player1Set2Result.lessThan=" + UPDATED_PLAYER_1_SET_2_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set2ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set2Result equals to DEFAULT_PLAYER_2_SET_2_RESULT
        defaultMatchShouldBeFound("player2Set2Result.equals=" + DEFAULT_PLAYER_2_SET_2_RESULT);

        // Get all the matchList where player2Set2Result equals to UPDATED_PLAYER_2_SET_2_RESULT
        defaultMatchShouldNotBeFound("player2Set2Result.equals=" + UPDATED_PLAYER_2_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set2ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set2Result in DEFAULT_PLAYER_2_SET_2_RESULT or UPDATED_PLAYER_2_SET_2_RESULT
        defaultMatchShouldBeFound("player2Set2Result.in=" + DEFAULT_PLAYER_2_SET_2_RESULT + "," + UPDATED_PLAYER_2_SET_2_RESULT);

        // Get all the matchList where player2Set2Result equals to UPDATED_PLAYER_2_SET_2_RESULT
        defaultMatchShouldNotBeFound("player2Set2Result.in=" + UPDATED_PLAYER_2_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set2ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set2Result is not null
        defaultMatchShouldBeFound("player2Set2Result.specified=true");

        // Get all the matchList where player2Set2Result is null
        defaultMatchShouldNotBeFound("player2Set2Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set2ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set2Result greater than or equals to DEFAULT_PLAYER_2_SET_2_RESULT
        defaultMatchShouldBeFound("player2Set2Result.greaterOrEqualThan=" + DEFAULT_PLAYER_2_SET_2_RESULT);

        // Get all the matchList where player2Set2Result greater than or equals to UPDATED_PLAYER_2_SET_2_RESULT
        defaultMatchShouldNotBeFound("player2Set2Result.greaterOrEqualThan=" + UPDATED_PLAYER_2_SET_2_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set2ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set2Result less than or equals to DEFAULT_PLAYER_2_SET_2_RESULT
        defaultMatchShouldNotBeFound("player2Set2Result.lessThan=" + DEFAULT_PLAYER_2_SET_2_RESULT);

        // Get all the matchList where player2Set2Result less than or equals to UPDATED_PLAYER_2_SET_2_RESULT
        defaultMatchShouldBeFound("player2Set2Result.lessThan=" + UPDATED_PLAYER_2_SET_2_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set3ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set3Result equals to DEFAULT_PLAYER_1_SET_3_RESULT
        defaultMatchShouldBeFound("player1Set3Result.equals=" + DEFAULT_PLAYER_1_SET_3_RESULT);

        // Get all the matchList where player1Set3Result equals to UPDATED_PLAYER_1_SET_3_RESULT
        defaultMatchShouldNotBeFound("player1Set3Result.equals=" + UPDATED_PLAYER_1_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set3ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set3Result in DEFAULT_PLAYER_1_SET_3_RESULT or UPDATED_PLAYER_1_SET_3_RESULT
        defaultMatchShouldBeFound("player1Set3Result.in=" + DEFAULT_PLAYER_1_SET_3_RESULT + "," + UPDATED_PLAYER_1_SET_3_RESULT);

        // Get all the matchList where player1Set3Result equals to UPDATED_PLAYER_1_SET_3_RESULT
        defaultMatchShouldNotBeFound("player1Set3Result.in=" + UPDATED_PLAYER_1_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set3ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set3Result is not null
        defaultMatchShouldBeFound("player1Set3Result.specified=true");

        // Get all the matchList where player1Set3Result is null
        defaultMatchShouldNotBeFound("player1Set3Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set3ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set3Result greater than or equals to DEFAULT_PLAYER_1_SET_3_RESULT
        defaultMatchShouldBeFound("player1Set3Result.greaterOrEqualThan=" + DEFAULT_PLAYER_1_SET_3_RESULT);

        // Get all the matchList where player1Set3Result greater than or equals to UPDATED_PLAYER_1_SET_3_RESULT
        defaultMatchShouldNotBeFound("player1Set3Result.greaterOrEqualThan=" + UPDATED_PLAYER_1_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer1Set3ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player1Set3Result less than or equals to DEFAULT_PLAYER_1_SET_3_RESULT
        defaultMatchShouldNotBeFound("player1Set3Result.lessThan=" + DEFAULT_PLAYER_1_SET_3_RESULT);

        // Get all the matchList where player1Set3Result less than or equals to UPDATED_PLAYER_1_SET_3_RESULT
        defaultMatchShouldBeFound("player1Set3Result.lessThan=" + UPDATED_PLAYER_1_SET_3_RESULT);
    }


    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set3ResultIsEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set3Result equals to DEFAULT_PLAYER_2_SET_3_RESULT
        defaultMatchShouldBeFound("player2Set3Result.equals=" + DEFAULT_PLAYER_2_SET_3_RESULT);

        // Get all the matchList where player2Set3Result equals to UPDATED_PLAYER_2_SET_3_RESULT
        defaultMatchShouldNotBeFound("player2Set3Result.equals=" + UPDATED_PLAYER_2_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set3ResultIsInShouldWork() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set3Result in DEFAULT_PLAYER_2_SET_3_RESULT or UPDATED_PLAYER_2_SET_3_RESULT
        defaultMatchShouldBeFound("player2Set3Result.in=" + DEFAULT_PLAYER_2_SET_3_RESULT + "," + UPDATED_PLAYER_2_SET_3_RESULT);

        // Get all the matchList where player2Set3Result equals to UPDATED_PLAYER_2_SET_3_RESULT
        defaultMatchShouldNotBeFound("player2Set3Result.in=" + UPDATED_PLAYER_2_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set3ResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set3Result is not null
        defaultMatchShouldBeFound("player2Set3Result.specified=true");

        // Get all the matchList where player2Set3Result is null
        defaultMatchShouldNotBeFound("player2Set3Result.specified=false");
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set3ResultIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set3Result greater than or equals to DEFAULT_PLAYER_2_SET_3_RESULT
        defaultMatchShouldBeFound("player2Set3Result.greaterOrEqualThan=" + DEFAULT_PLAYER_2_SET_3_RESULT);

        // Get all the matchList where player2Set3Result greater than or equals to UPDATED_PLAYER_2_SET_3_RESULT
        defaultMatchShouldNotBeFound("player2Set3Result.greaterOrEqualThan=" + UPDATED_PLAYER_2_SET_3_RESULT);
    }

    @Test
    @Transactional
    public void getAllMatchesByPlayer2Set3ResultIsLessThanSomething() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList where player2Set3Result less than or equals to DEFAULT_PLAYER_2_SET_3_RESULT
        defaultMatchShouldNotBeFound("player2Set3Result.lessThan=" + DEFAULT_PLAYER_2_SET_3_RESULT);

        // Get all the matchList where player2Set3Result less than or equals to UPDATED_PLAYER_2_SET_3_RESULT
        defaultMatchShouldBeFound("player2Set3Result.lessThan=" + UPDATED_PLAYER_2_SET_3_RESULT);
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
            .andExpect(jsonPath("$.[*].player1Set1Result").value(hasItem(DEFAULT_PLAYER_1_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].player2Set1Result").value(hasItem(DEFAULT_PLAYER_2_SET_1_RESULT)))
            .andExpect(jsonPath("$.[*].player1Set2Result").value(hasItem(DEFAULT_PLAYER_1_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].player2Set2Result").value(hasItem(DEFAULT_PLAYER_2_SET_2_RESULT)))
            .andExpect(jsonPath("$.[*].player1Set3Result").value(hasItem(DEFAULT_PLAYER_1_SET_3_RESULT)))
            .andExpect(jsonPath("$.[*].player2Set3Result").value(hasItem(DEFAULT_PLAYER_2_SET_3_RESULT)));

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
            .player1Set1Result(UPDATED_PLAYER_1_SET_1_RESULT)
            .player2Set1Result(UPDATED_PLAYER_2_SET_1_RESULT)
            .player1Set2Result(UPDATED_PLAYER_1_SET_2_RESULT)
            .player2Set2Result(UPDATED_PLAYER_2_SET_2_RESULT)
            .player1Set3Result(UPDATED_PLAYER_1_SET_3_RESULT)
            .player2Set3Result(UPDATED_PLAYER_2_SET_3_RESULT);

        restMatchMockMvc.perform(put("/api/matches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMatch)))
            .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getPlayer1Set1Result()).isEqualTo(UPDATED_PLAYER_1_SET_1_RESULT);
        assertThat(testMatch.getPlayer2Set1Result()).isEqualTo(UPDATED_PLAYER_2_SET_1_RESULT);
        assertThat(testMatch.getPlayer1Set2Result()).isEqualTo(UPDATED_PLAYER_1_SET_2_RESULT);
        assertThat(testMatch.getPlayer2Set2Result()).isEqualTo(UPDATED_PLAYER_2_SET_2_RESULT);
        assertThat(testMatch.getPlayer1Set3Result()).isEqualTo(UPDATED_PLAYER_1_SET_3_RESULT);
        assertThat(testMatch.getPlayer2Set3Result()).isEqualTo(UPDATED_PLAYER_2_SET_3_RESULT);
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
