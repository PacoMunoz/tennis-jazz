package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.Ranking;
import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.repository.RankingRepository;
import es.pmg.tennisjazz.service.RankingService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;
import es.pmg.tennisjazz.service.dto.RankingCriteria;
import es.pmg.tennisjazz.service.RankingQueryService;

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
 * Integration tests for the {@link RankingResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class RankingResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final Integer DEFAULT_GAMES_WIN = 1;
    private static final Integer UPDATED_GAMES_WIN = 2;
    private static final Integer SMALLER_GAMES_WIN = 1 - 1;

    private static final Integer DEFAULT_GAMES_LOSS = 1;
    private static final Integer UPDATED_GAMES_LOSS = 2;
    private static final Integer SMALLER_GAMES_LOSS = 1 - 1;

    private static final Integer DEFAULT_SETS_WIN = 1;
    private static final Integer UPDATED_SETS_WIN = 2;
    private static final Integer SMALLER_SETS_WIN = 1 - 1;

    private static final Integer DEFAULT_SETS_LOSS = 1;
    private static final Integer UPDATED_SETS_LOSS = 2;
    private static final Integer SMALLER_SETS_LOSS = 1 - 1;

    private static final Integer DEFAULT_MATCHES_PLAYED = 1;
    private static final Integer UPDATED_MATCHES_PLAYED = 2;
    private static final Integer SMALLER_MATCHES_PLAYED = 1 - 1;

    private static final Integer DEFAULT_MATCHES_WON = 1;
    private static final Integer UPDATED_MATCHES_WON = 2;
    private static final Integer SMALLER_MATCHES_WON = 1 - 1;

    private static final Integer DEFAULT_MATCHES_LOSS = 1;
    private static final Integer UPDATED_MATCHES_LOSS = 2;
    private static final Integer SMALLER_MATCHES_LOSS = 1 - 1;

    private static final Integer DEFAULT_MATCHES_NOT_PRESENT = 1;
    private static final Integer UPDATED_MATCHES_NOT_PRESENT = 2;
    private static final Integer SMALLER_MATCHES_NOT_PRESENT = 1 - 1;

    private static final Integer DEFAULT_MATCHES_ABANDONED = 1;
    private static final Integer UPDATED_MATCHES_ABANDONED = 2;
    private static final Integer SMALLER_MATCHES_ABANDONED = 1 - 1;

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RankingQueryService rankingQueryService;

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

    private MockMvc restRankingMockMvc;

    private Ranking ranking;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RankingResource rankingResource = new RankingResource(rankingService, rankingQueryService);
        this.restRankingMockMvc = MockMvcBuilders.standaloneSetup(rankingResource)
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
    public static Ranking createEntity(EntityManager em) {
        Ranking ranking = new Ranking()
            .points(DEFAULT_POINTS)
            .gamesWin(DEFAULT_GAMES_WIN)
            .gamesLoss(DEFAULT_GAMES_LOSS)
            .setsWin(DEFAULT_SETS_WIN)
            .setsLoss(DEFAULT_SETS_LOSS)
            .matchesPlayed(DEFAULT_MATCHES_PLAYED)
            .matchesWon(DEFAULT_MATCHES_WON)
            .matchesLoss(DEFAULT_MATCHES_LOSS)
            .matchesNotPresent(DEFAULT_MATCHES_NOT_PRESENT)
            .matchesAbandoned(DEFAULT_MATCHES_ABANDONED);
        return ranking;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranking createUpdatedEntity(EntityManager em) {
        Ranking ranking = new Ranking()
            .points(UPDATED_POINTS)
            .gamesWin(UPDATED_GAMES_WIN)
            .gamesLoss(UPDATED_GAMES_LOSS)
            .setsWin(UPDATED_SETS_WIN)
            .setsLoss(UPDATED_SETS_LOSS)
            .matchesPlayed(UPDATED_MATCHES_PLAYED)
            .matchesWon(UPDATED_MATCHES_WON)
            .matchesLoss(UPDATED_MATCHES_LOSS)
            .matchesNotPresent(UPDATED_MATCHES_NOT_PRESENT)
            .matchesAbandoned(UPDATED_MATCHES_ABANDONED);
        return ranking;
    }

    @BeforeEach
    public void initTest() {
        ranking = createEntity(em);
    }

    @Test
    @Transactional
    public void createRanking() throws Exception {
        int databaseSizeBeforeCreate = rankingRepository.findAll().size();

        // Create the Ranking
        restRankingMockMvc.perform(post("/api/rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isCreated());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeCreate + 1);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testRanking.getGamesWin()).isEqualTo(DEFAULT_GAMES_WIN);
        assertThat(testRanking.getGamesLoss()).isEqualTo(DEFAULT_GAMES_LOSS);
        assertThat(testRanking.getSetsWin()).isEqualTo(DEFAULT_SETS_WIN);
        assertThat(testRanking.getSetsLoss()).isEqualTo(DEFAULT_SETS_LOSS);
        assertThat(testRanking.getMatchesPlayed()).isEqualTo(DEFAULT_MATCHES_PLAYED);
        assertThat(testRanking.getMatchesWon()).isEqualTo(DEFAULT_MATCHES_WON);
        assertThat(testRanking.getMatchesLoss()).isEqualTo(DEFAULT_MATCHES_LOSS);
        assertThat(testRanking.getMatchesNotPresent()).isEqualTo(DEFAULT_MATCHES_NOT_PRESENT);
        assertThat(testRanking.getMatchesAbandoned()).isEqualTo(DEFAULT_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void createRankingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rankingRepository.findAll().size();

        // Create the Ranking with an existing ID
        ranking.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRankingMockMvc.perform(post("/api/rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRankings() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList
        restRankingMockMvc.perform(get("/api/rankings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ranking.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].gamesWin").value(hasItem(DEFAULT_GAMES_WIN)))
            .andExpect(jsonPath("$.[*].gamesLoss").value(hasItem(DEFAULT_GAMES_LOSS)))
            .andExpect(jsonPath("$.[*].setsWin").value(hasItem(DEFAULT_SETS_WIN)))
            .andExpect(jsonPath("$.[*].setsLoss").value(hasItem(DEFAULT_SETS_LOSS)))
            .andExpect(jsonPath("$.[*].matchesPlayed").value(hasItem(DEFAULT_MATCHES_PLAYED)))
            .andExpect(jsonPath("$.[*].matchesWon").value(hasItem(DEFAULT_MATCHES_WON)))
            .andExpect(jsonPath("$.[*].matchesLoss").value(hasItem(DEFAULT_MATCHES_LOSS)))
            .andExpect(jsonPath("$.[*].matchesNotPresent").value(hasItem(DEFAULT_MATCHES_NOT_PRESENT)))
            .andExpect(jsonPath("$.[*].matchesAbandoned").value(hasItem(DEFAULT_MATCHES_ABANDONED)));
    }
    
    @Test
    @Transactional
    public void getRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get the ranking
        restRankingMockMvc.perform(get("/api/rankings/{id}", ranking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ranking.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.gamesWin").value(DEFAULT_GAMES_WIN))
            .andExpect(jsonPath("$.gamesLoss").value(DEFAULT_GAMES_LOSS))
            .andExpect(jsonPath("$.setsWin").value(DEFAULT_SETS_WIN))
            .andExpect(jsonPath("$.setsLoss").value(DEFAULT_SETS_LOSS))
            .andExpect(jsonPath("$.matchesPlayed").value(DEFAULT_MATCHES_PLAYED))
            .andExpect(jsonPath("$.matchesWon").value(DEFAULT_MATCHES_WON))
            .andExpect(jsonPath("$.matchesLoss").value(DEFAULT_MATCHES_LOSS))
            .andExpect(jsonPath("$.matchesNotPresent").value(DEFAULT_MATCHES_NOT_PRESENT))
            .andExpect(jsonPath("$.matchesAbandoned").value(DEFAULT_MATCHES_ABANDONED));
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points equals to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the rankingList where points equals to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultRankingShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the rankingList where points equals to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is not null
        defaultRankingShouldBeFound("points.specified=true");

        // Get all the rankingList where points is null
        defaultRankingShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is greater than or equal to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the rankingList where points is greater than or equal to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is less than or equal to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the rankingList where points is less than or equal to SMALLER_POINTS
        defaultRankingShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is less than DEFAULT_POINTS
        defaultRankingShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the rankingList where points is less than UPDATED_POINTS
        defaultRankingShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points is greater than DEFAULT_POINTS
        defaultRankingShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the rankingList where points is greater than SMALLER_POINTS
        defaultRankingShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin equals to DEFAULT_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.equals=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin equals to UPDATED_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.equals=" + UPDATED_GAMES_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin in DEFAULT_GAMES_WIN or UPDATED_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.in=" + DEFAULT_GAMES_WIN + "," + UPDATED_GAMES_WIN);

        // Get all the rankingList where gamesWin equals to UPDATED_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.in=" + UPDATED_GAMES_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin is not null
        defaultRankingShouldBeFound("gamesWin.specified=true");

        // Get all the rankingList where gamesWin is null
        defaultRankingShouldNotBeFound("gamesWin.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin is greater than or equal to DEFAULT_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.greaterThanOrEqual=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin is greater than or equal to UPDATED_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.greaterThanOrEqual=" + UPDATED_GAMES_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin is less than or equal to DEFAULT_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.lessThanOrEqual=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin is less than or equal to SMALLER_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.lessThanOrEqual=" + SMALLER_GAMES_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin is less than DEFAULT_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.lessThan=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin is less than UPDATED_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.lessThan=" + UPDATED_GAMES_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin is greater than DEFAULT_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.greaterThan=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin is greater than SMALLER_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.greaterThan=" + SMALLER_GAMES_WIN);
    }


    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss equals to DEFAULT_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.equals=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss equals to UPDATED_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.equals=" + UPDATED_GAMES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss in DEFAULT_GAMES_LOSS or UPDATED_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.in=" + DEFAULT_GAMES_LOSS + "," + UPDATED_GAMES_LOSS);

        // Get all the rankingList where gamesLoss equals to UPDATED_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.in=" + UPDATED_GAMES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss is not null
        defaultRankingShouldBeFound("gamesLoss.specified=true");

        // Get all the rankingList where gamesLoss is null
        defaultRankingShouldNotBeFound("gamesLoss.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss is greater than or equal to DEFAULT_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.greaterThanOrEqual=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss is greater than or equal to UPDATED_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.greaterThanOrEqual=" + UPDATED_GAMES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss is less than or equal to DEFAULT_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.lessThanOrEqual=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss is less than or equal to SMALLER_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.lessThanOrEqual=" + SMALLER_GAMES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss is less than DEFAULT_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.lessThan=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss is less than UPDATED_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.lessThan=" + UPDATED_GAMES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss is greater than DEFAULT_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.greaterThan=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss is greater than SMALLER_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.greaterThan=" + SMALLER_GAMES_LOSS);
    }


    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin equals to DEFAULT_SETS_WIN
        defaultRankingShouldBeFound("setsWin.equals=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin equals to UPDATED_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.equals=" + UPDATED_SETS_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin in DEFAULT_SETS_WIN or UPDATED_SETS_WIN
        defaultRankingShouldBeFound("setsWin.in=" + DEFAULT_SETS_WIN + "," + UPDATED_SETS_WIN);

        // Get all the rankingList where setsWin equals to UPDATED_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.in=" + UPDATED_SETS_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin is not null
        defaultRankingShouldBeFound("setsWin.specified=true");

        // Get all the rankingList where setsWin is null
        defaultRankingShouldNotBeFound("setsWin.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin is greater than or equal to DEFAULT_SETS_WIN
        defaultRankingShouldBeFound("setsWin.greaterThanOrEqual=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin is greater than or equal to UPDATED_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.greaterThanOrEqual=" + UPDATED_SETS_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin is less than or equal to DEFAULT_SETS_WIN
        defaultRankingShouldBeFound("setsWin.lessThanOrEqual=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin is less than or equal to SMALLER_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.lessThanOrEqual=" + SMALLER_SETS_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin is less than DEFAULT_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.lessThan=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin is less than UPDATED_SETS_WIN
        defaultRankingShouldBeFound("setsWin.lessThan=" + UPDATED_SETS_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin is greater than DEFAULT_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.greaterThan=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin is greater than SMALLER_SETS_WIN
        defaultRankingShouldBeFound("setsWin.greaterThan=" + SMALLER_SETS_WIN);
    }


    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss equals to DEFAULT_SETS_LOSS
        defaultRankingShouldBeFound("setsLoss.equals=" + DEFAULT_SETS_LOSS);

        // Get all the rankingList where setsLoss equals to UPDATED_SETS_LOSS
        defaultRankingShouldNotBeFound("setsLoss.equals=" + UPDATED_SETS_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss in DEFAULT_SETS_LOSS or UPDATED_SETS_LOSS
        defaultRankingShouldBeFound("setsLoss.in=" + DEFAULT_SETS_LOSS + "," + UPDATED_SETS_LOSS);

        // Get all the rankingList where setsLoss equals to UPDATED_SETS_LOSS
        defaultRankingShouldNotBeFound("setsLoss.in=" + UPDATED_SETS_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss is not null
        defaultRankingShouldBeFound("setsLoss.specified=true");

        // Get all the rankingList where setsLoss is null
        defaultRankingShouldNotBeFound("setsLoss.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss is greater than or equal to DEFAULT_SETS_LOSS
        defaultRankingShouldBeFound("setsLoss.greaterThanOrEqual=" + DEFAULT_SETS_LOSS);

        // Get all the rankingList where setsLoss is greater than or equal to UPDATED_SETS_LOSS
        defaultRankingShouldNotBeFound("setsLoss.greaterThanOrEqual=" + UPDATED_SETS_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss is less than or equal to DEFAULT_SETS_LOSS
        defaultRankingShouldBeFound("setsLoss.lessThanOrEqual=" + DEFAULT_SETS_LOSS);

        // Get all the rankingList where setsLoss is less than or equal to SMALLER_SETS_LOSS
        defaultRankingShouldNotBeFound("setsLoss.lessThanOrEqual=" + SMALLER_SETS_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss is less than DEFAULT_SETS_LOSS
        defaultRankingShouldNotBeFound("setsLoss.lessThan=" + DEFAULT_SETS_LOSS);

        // Get all the rankingList where setsLoss is less than UPDATED_SETS_LOSS
        defaultRankingShouldBeFound("setsLoss.lessThan=" + UPDATED_SETS_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLossIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLoss is greater than DEFAULT_SETS_LOSS
        defaultRankingShouldNotBeFound("setsLoss.greaterThan=" + DEFAULT_SETS_LOSS);

        // Get all the rankingList where setsLoss is greater than SMALLER_SETS_LOSS
        defaultRankingShouldBeFound("setsLoss.greaterThan=" + SMALLER_SETS_LOSS);
    }


    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed equals to DEFAULT_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.equals=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed equals to UPDATED_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.equals=" + UPDATED_MATCHES_PLAYED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed in DEFAULT_MATCHES_PLAYED or UPDATED_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.in=" + DEFAULT_MATCHES_PLAYED + "," + UPDATED_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed equals to UPDATED_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.in=" + UPDATED_MATCHES_PLAYED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed is not null
        defaultRankingShouldBeFound("matchesPlayed.specified=true");

        // Get all the rankingList where matchesPlayed is null
        defaultRankingShouldNotBeFound("matchesPlayed.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed is greater than or equal to DEFAULT_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.greaterThanOrEqual=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed is greater than or equal to UPDATED_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.greaterThanOrEqual=" + UPDATED_MATCHES_PLAYED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed is less than or equal to DEFAULT_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.lessThanOrEqual=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed is less than or equal to SMALLER_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.lessThanOrEqual=" + SMALLER_MATCHES_PLAYED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed is less than DEFAULT_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.lessThan=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed is less than UPDATED_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.lessThan=" + UPDATED_MATCHES_PLAYED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed is greater than DEFAULT_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.greaterThan=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed is greater than SMALLER_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.greaterThan=" + SMALLER_MATCHES_PLAYED);
    }


    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon equals to DEFAULT_MATCHES_WON
        defaultRankingShouldBeFound("matchesWon.equals=" + DEFAULT_MATCHES_WON);

        // Get all the rankingList where matchesWon equals to UPDATED_MATCHES_WON
        defaultRankingShouldNotBeFound("matchesWon.equals=" + UPDATED_MATCHES_WON);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon in DEFAULT_MATCHES_WON or UPDATED_MATCHES_WON
        defaultRankingShouldBeFound("matchesWon.in=" + DEFAULT_MATCHES_WON + "," + UPDATED_MATCHES_WON);

        // Get all the rankingList where matchesWon equals to UPDATED_MATCHES_WON
        defaultRankingShouldNotBeFound("matchesWon.in=" + UPDATED_MATCHES_WON);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon is not null
        defaultRankingShouldBeFound("matchesWon.specified=true");

        // Get all the rankingList where matchesWon is null
        defaultRankingShouldNotBeFound("matchesWon.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon is greater than or equal to DEFAULT_MATCHES_WON
        defaultRankingShouldBeFound("matchesWon.greaterThanOrEqual=" + DEFAULT_MATCHES_WON);

        // Get all the rankingList where matchesWon is greater than or equal to UPDATED_MATCHES_WON
        defaultRankingShouldNotBeFound("matchesWon.greaterThanOrEqual=" + UPDATED_MATCHES_WON);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon is less than or equal to DEFAULT_MATCHES_WON
        defaultRankingShouldBeFound("matchesWon.lessThanOrEqual=" + DEFAULT_MATCHES_WON);

        // Get all the rankingList where matchesWon is less than or equal to SMALLER_MATCHES_WON
        defaultRankingShouldNotBeFound("matchesWon.lessThanOrEqual=" + SMALLER_MATCHES_WON);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon is less than DEFAULT_MATCHES_WON
        defaultRankingShouldNotBeFound("matchesWon.lessThan=" + DEFAULT_MATCHES_WON);

        // Get all the rankingList where matchesWon is less than UPDATED_MATCHES_WON
        defaultRankingShouldBeFound("matchesWon.lessThan=" + UPDATED_MATCHES_WON);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWonIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWon is greater than DEFAULT_MATCHES_WON
        defaultRankingShouldNotBeFound("matchesWon.greaterThan=" + DEFAULT_MATCHES_WON);

        // Get all the rankingList where matchesWon is greater than SMALLER_MATCHES_WON
        defaultRankingShouldBeFound("matchesWon.greaterThan=" + SMALLER_MATCHES_WON);
    }


    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss equals to DEFAULT_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.equals=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss equals to UPDATED_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.equals=" + UPDATED_MATCHES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss in DEFAULT_MATCHES_LOSS or UPDATED_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.in=" + DEFAULT_MATCHES_LOSS + "," + UPDATED_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss equals to UPDATED_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.in=" + UPDATED_MATCHES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss is not null
        defaultRankingShouldBeFound("matchesLoss.specified=true");

        // Get all the rankingList where matchesLoss is null
        defaultRankingShouldNotBeFound("matchesLoss.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss is greater than or equal to DEFAULT_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.greaterThanOrEqual=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss is greater than or equal to UPDATED_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.greaterThanOrEqual=" + UPDATED_MATCHES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss is less than or equal to DEFAULT_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.lessThanOrEqual=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss is less than or equal to SMALLER_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.lessThanOrEqual=" + SMALLER_MATCHES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss is less than DEFAULT_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.lessThan=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss is less than UPDATED_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.lessThan=" + UPDATED_MATCHES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss is greater than DEFAULT_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.greaterThan=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss is greater than SMALLER_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.greaterThan=" + SMALLER_MATCHES_LOSS);
    }


    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent equals to DEFAULT_MATCHES_NOT_PRESENT
        defaultRankingShouldBeFound("matchesNotPresent.equals=" + DEFAULT_MATCHES_NOT_PRESENT);

        // Get all the rankingList where matchesNotPresent equals to UPDATED_MATCHES_NOT_PRESENT
        defaultRankingShouldNotBeFound("matchesNotPresent.equals=" + UPDATED_MATCHES_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent in DEFAULT_MATCHES_NOT_PRESENT or UPDATED_MATCHES_NOT_PRESENT
        defaultRankingShouldBeFound("matchesNotPresent.in=" + DEFAULT_MATCHES_NOT_PRESENT + "," + UPDATED_MATCHES_NOT_PRESENT);

        // Get all the rankingList where matchesNotPresent equals to UPDATED_MATCHES_NOT_PRESENT
        defaultRankingShouldNotBeFound("matchesNotPresent.in=" + UPDATED_MATCHES_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent is not null
        defaultRankingShouldBeFound("matchesNotPresent.specified=true");

        // Get all the rankingList where matchesNotPresent is null
        defaultRankingShouldNotBeFound("matchesNotPresent.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent is greater than or equal to DEFAULT_MATCHES_NOT_PRESENT
        defaultRankingShouldBeFound("matchesNotPresent.greaterThanOrEqual=" + DEFAULT_MATCHES_NOT_PRESENT);

        // Get all the rankingList where matchesNotPresent is greater than or equal to UPDATED_MATCHES_NOT_PRESENT
        defaultRankingShouldNotBeFound("matchesNotPresent.greaterThanOrEqual=" + UPDATED_MATCHES_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent is less than or equal to DEFAULT_MATCHES_NOT_PRESENT
        defaultRankingShouldBeFound("matchesNotPresent.lessThanOrEqual=" + DEFAULT_MATCHES_NOT_PRESENT);

        // Get all the rankingList where matchesNotPresent is less than or equal to SMALLER_MATCHES_NOT_PRESENT
        defaultRankingShouldNotBeFound("matchesNotPresent.lessThanOrEqual=" + SMALLER_MATCHES_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent is less than DEFAULT_MATCHES_NOT_PRESENT
        defaultRankingShouldNotBeFound("matchesNotPresent.lessThan=" + DEFAULT_MATCHES_NOT_PRESENT);

        // Get all the rankingList where matchesNotPresent is less than UPDATED_MATCHES_NOT_PRESENT
        defaultRankingShouldBeFound("matchesNotPresent.lessThan=" + UPDATED_MATCHES_NOT_PRESENT);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesNotPresentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesNotPresent is greater than DEFAULT_MATCHES_NOT_PRESENT
        defaultRankingShouldNotBeFound("matchesNotPresent.greaterThan=" + DEFAULT_MATCHES_NOT_PRESENT);

        // Get all the rankingList where matchesNotPresent is greater than SMALLER_MATCHES_NOT_PRESENT
        defaultRankingShouldBeFound("matchesNotPresent.greaterThan=" + SMALLER_MATCHES_NOT_PRESENT);
    }


    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned equals to DEFAULT_MATCHES_ABANDONED
        defaultRankingShouldBeFound("matchesAbandoned.equals=" + DEFAULT_MATCHES_ABANDONED);

        // Get all the rankingList where matchesAbandoned equals to UPDATED_MATCHES_ABANDONED
        defaultRankingShouldNotBeFound("matchesAbandoned.equals=" + UPDATED_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned in DEFAULT_MATCHES_ABANDONED or UPDATED_MATCHES_ABANDONED
        defaultRankingShouldBeFound("matchesAbandoned.in=" + DEFAULT_MATCHES_ABANDONED + "," + UPDATED_MATCHES_ABANDONED);

        // Get all the rankingList where matchesAbandoned equals to UPDATED_MATCHES_ABANDONED
        defaultRankingShouldNotBeFound("matchesAbandoned.in=" + UPDATED_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned is not null
        defaultRankingShouldBeFound("matchesAbandoned.specified=true");

        // Get all the rankingList where matchesAbandoned is null
        defaultRankingShouldNotBeFound("matchesAbandoned.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned is greater than or equal to DEFAULT_MATCHES_ABANDONED
        defaultRankingShouldBeFound("matchesAbandoned.greaterThanOrEqual=" + DEFAULT_MATCHES_ABANDONED);

        // Get all the rankingList where matchesAbandoned is greater than or equal to UPDATED_MATCHES_ABANDONED
        defaultRankingShouldNotBeFound("matchesAbandoned.greaterThanOrEqual=" + UPDATED_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned is less than or equal to DEFAULT_MATCHES_ABANDONED
        defaultRankingShouldBeFound("matchesAbandoned.lessThanOrEqual=" + DEFAULT_MATCHES_ABANDONED);

        // Get all the rankingList where matchesAbandoned is less than or equal to SMALLER_MATCHES_ABANDONED
        defaultRankingShouldNotBeFound("matchesAbandoned.lessThanOrEqual=" + SMALLER_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned is less than DEFAULT_MATCHES_ABANDONED
        defaultRankingShouldNotBeFound("matchesAbandoned.lessThan=" + DEFAULT_MATCHES_ABANDONED);

        // Get all the rankingList where matchesAbandoned is less than UPDATED_MATCHES_ABANDONED
        defaultRankingShouldBeFound("matchesAbandoned.lessThan=" + UPDATED_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesAbandonedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesAbandoned is greater than DEFAULT_MATCHES_ABANDONED
        defaultRankingShouldNotBeFound("matchesAbandoned.greaterThan=" + DEFAULT_MATCHES_ABANDONED);

        // Get all the rankingList where matchesAbandoned is greater than SMALLER_MATCHES_ABANDONED
        defaultRankingShouldBeFound("matchesAbandoned.greaterThan=" + SMALLER_MATCHES_ABANDONED);
    }


    @Test
    @Transactional
    public void getAllRankingsByTournamentGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);
        TournamentGroup tournamentGroup = TournamentGroupResourceIT.createEntity(em);
        em.persist(tournamentGroup);
        em.flush();
        ranking.setTournamentGroup(tournamentGroup);
        rankingRepository.saveAndFlush(ranking);
        Long tournamentGroupId = tournamentGroup.getId();

        // Get all the rankingList where tournamentGroup equals to tournamentGroupId
        defaultRankingShouldBeFound("tournamentGroupId.equals=" + tournamentGroupId);

        // Get all the rankingList where tournamentGroup equals to tournamentGroupId + 1
        defaultRankingShouldNotBeFound("tournamentGroupId.equals=" + (tournamentGroupId + 1));
    }


    @Test
    @Transactional
    public void getAllRankingsByPlayerIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);
        Player player = PlayerResourceIT.createEntity(em);
        em.persist(player);
        em.flush();
        ranking.setPlayer(player);
        rankingRepository.saveAndFlush(ranking);
        Long playerId = player.getId();

        // Get all the rankingList where player equals to playerId
        defaultRankingShouldBeFound("playerId.equals=" + playerId);

        // Get all the rankingList where player equals to playerId + 1
        defaultRankingShouldNotBeFound("playerId.equals=" + (playerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRankingShouldBeFound(String filter) throws Exception {
        restRankingMockMvc.perform(get("/api/rankings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ranking.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].gamesWin").value(hasItem(DEFAULT_GAMES_WIN)))
            .andExpect(jsonPath("$.[*].gamesLoss").value(hasItem(DEFAULT_GAMES_LOSS)))
            .andExpect(jsonPath("$.[*].setsWin").value(hasItem(DEFAULT_SETS_WIN)))
            .andExpect(jsonPath("$.[*].setsLoss").value(hasItem(DEFAULT_SETS_LOSS)))
            .andExpect(jsonPath("$.[*].matchesPlayed").value(hasItem(DEFAULT_MATCHES_PLAYED)))
            .andExpect(jsonPath("$.[*].matchesWon").value(hasItem(DEFAULT_MATCHES_WON)))
            .andExpect(jsonPath("$.[*].matchesLoss").value(hasItem(DEFAULT_MATCHES_LOSS)))
            .andExpect(jsonPath("$.[*].matchesNotPresent").value(hasItem(DEFAULT_MATCHES_NOT_PRESENT)))
            .andExpect(jsonPath("$.[*].matchesAbandoned").value(hasItem(DEFAULT_MATCHES_ABANDONED)));

        // Check, that the count call also returns 1
        restRankingMockMvc.perform(get("/api/rankings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRankingShouldNotBeFound(String filter) throws Exception {
        restRankingMockMvc.perform(get("/api/rankings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRankingMockMvc.perform(get("/api/rankings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRanking() throws Exception {
        // Get the ranking
        restRankingMockMvc.perform(get("/api/rankings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRanking() throws Exception {
        // Initialize the database
        rankingService.save(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking
        Ranking updatedRanking = rankingRepository.findById(ranking.getId()).get();
        // Disconnect from session so that the updates on updatedRanking are not directly saved in db
        em.detach(updatedRanking);
        updatedRanking
            .points(UPDATED_POINTS)
            .gamesWin(UPDATED_GAMES_WIN)
            .gamesLoss(UPDATED_GAMES_LOSS)
            .setsWin(UPDATED_SETS_WIN)
            .setsLoss(UPDATED_SETS_LOSS)
            .matchesPlayed(UPDATED_MATCHES_PLAYED)
            .matchesWon(UPDATED_MATCHES_WON)
            .matchesLoss(UPDATED_MATCHES_LOSS)
            .matchesNotPresent(UPDATED_MATCHES_NOT_PRESENT)
            .matchesAbandoned(UPDATED_MATCHES_ABANDONED);

        restRankingMockMvc.perform(put("/api/rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRanking)))
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testRanking.getGamesWin()).isEqualTo(UPDATED_GAMES_WIN);
        assertThat(testRanking.getGamesLoss()).isEqualTo(UPDATED_GAMES_LOSS);
        assertThat(testRanking.getSetsWin()).isEqualTo(UPDATED_SETS_WIN);
        assertThat(testRanking.getSetsLoss()).isEqualTo(UPDATED_SETS_LOSS);
        assertThat(testRanking.getMatchesPlayed()).isEqualTo(UPDATED_MATCHES_PLAYED);
        assertThat(testRanking.getMatchesWon()).isEqualTo(UPDATED_MATCHES_WON);
        assertThat(testRanking.getMatchesLoss()).isEqualTo(UPDATED_MATCHES_LOSS);
        assertThat(testRanking.getMatchesNotPresent()).isEqualTo(UPDATED_MATCHES_NOT_PRESENT);
        assertThat(testRanking.getMatchesAbandoned()).isEqualTo(UPDATED_MATCHES_ABANDONED);
    }

    @Test
    @Transactional
    public void updateNonExistingRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Create the Ranking

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRankingMockMvc.perform(put("/api/rankings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRanking() throws Exception {
        // Initialize the database
        rankingService.save(ranking);

        int databaseSizeBeforeDelete = rankingRepository.findAll().size();

        // Delete the ranking
        restRankingMockMvc.perform(delete("/api/rankings/{id}", ranking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ranking.class);
        Ranking ranking1 = new Ranking();
        ranking1.setId(1L);
        Ranking ranking2 = new Ranking();
        ranking2.setId(ranking1.getId());
        assertThat(ranking1).isEqualTo(ranking2);
        ranking2.setId(2L);
        assertThat(ranking1).isNotEqualTo(ranking2);
        ranking1.setId(null);
        assertThat(ranking1).isNotEqualTo(ranking2);
    }
}
