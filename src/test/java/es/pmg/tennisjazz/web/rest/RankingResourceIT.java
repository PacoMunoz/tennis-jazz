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
 * Integration tests for the {@Link RankingResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class RankingResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_GAMES_WIN = 1;
    private static final Integer UPDATED_GAMES_WIN = 2;

    private static final Integer DEFAULT_GAMES_LOSS = 1;
    private static final Integer UPDATED_GAMES_LOSS = 2;

    private static final Integer DEFAULT_SETS_WIN = 1;
    private static final Integer UPDATED_SETS_WIN = 2;

    private static final Integer DEFAULT_SETS_LOST = 1;
    private static final Integer UPDATED_SETS_LOST = 2;

    private static final Integer DEFAULT_MATCHES_PLAYED = 1;
    private static final Integer UPDATED_MATCHES_PLAYED = 2;

    private static final Integer DEFAULT_MATCHES_WINED = 1;
    private static final Integer UPDATED_MATCHES_WINED = 2;

    private static final Integer DEFAULT_MATCHES_LOSS = 1;
    private static final Integer UPDATED_MATCHES_LOSS = 2;

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
            .setsLost(DEFAULT_SETS_LOST)
            .matchesPlayed(DEFAULT_MATCHES_PLAYED)
            .matchesWined(DEFAULT_MATCHES_WINED)
            .matchesLoss(DEFAULT_MATCHES_LOSS);
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
            .setsLost(UPDATED_SETS_LOST)
            .matchesPlayed(UPDATED_MATCHES_PLAYED)
            .matchesWined(UPDATED_MATCHES_WINED)
            .matchesLoss(UPDATED_MATCHES_LOSS);
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
        assertThat(testRanking.getSetsLost()).isEqualTo(DEFAULT_SETS_LOST);
        assertThat(testRanking.getMatchesPlayed()).isEqualTo(DEFAULT_MATCHES_PLAYED);
        assertThat(testRanking.getMatchesWined()).isEqualTo(DEFAULT_MATCHES_WINED);
        assertThat(testRanking.getMatchesLoss()).isEqualTo(DEFAULT_MATCHES_LOSS);
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
            .andExpect(jsonPath("$.[*].setsLost").value(hasItem(DEFAULT_SETS_LOST)))
            .andExpect(jsonPath("$.[*].matchesPlayed").value(hasItem(DEFAULT_MATCHES_PLAYED)))
            .andExpect(jsonPath("$.[*].matchesWined").value(hasItem(DEFAULT_MATCHES_WINED)))
            .andExpect(jsonPath("$.[*].matchesLoss").value(hasItem(DEFAULT_MATCHES_LOSS)));
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
            .andExpect(jsonPath("$.setsLost").value(DEFAULT_SETS_LOST))
            .andExpect(jsonPath("$.matchesPlayed").value(DEFAULT_MATCHES_PLAYED))
            .andExpect(jsonPath("$.matchesWined").value(DEFAULT_MATCHES_WINED))
            .andExpect(jsonPath("$.matchesLoss").value(DEFAULT_MATCHES_LOSS));
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

        // Get all the rankingList where points greater than or equals to DEFAULT_POINTS
        defaultRankingShouldBeFound("points.greaterOrEqualThan=" + DEFAULT_POINTS);

        // Get all the rankingList where points greater than or equals to UPDATED_POINTS
        defaultRankingShouldNotBeFound("points.greaterOrEqualThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllRankingsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where points less than or equals to DEFAULT_POINTS
        defaultRankingShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the rankingList where points less than or equals to UPDATED_POINTS
        defaultRankingShouldBeFound("points.lessThan=" + UPDATED_POINTS);
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

        // Get all the rankingList where gamesWin greater than or equals to DEFAULT_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.greaterOrEqualThan=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin greater than or equals to UPDATED_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.greaterOrEqualThan=" + UPDATED_GAMES_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesWinIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesWin less than or equals to DEFAULT_GAMES_WIN
        defaultRankingShouldNotBeFound("gamesWin.lessThan=" + DEFAULT_GAMES_WIN);

        // Get all the rankingList where gamesWin less than or equals to UPDATED_GAMES_WIN
        defaultRankingShouldBeFound("gamesWin.lessThan=" + UPDATED_GAMES_WIN);
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

        // Get all the rankingList where gamesLoss greater than or equals to DEFAULT_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.greaterOrEqualThan=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss greater than or equals to UPDATED_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.greaterOrEqualThan=" + UPDATED_GAMES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByGamesLossIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where gamesLoss less than or equals to DEFAULT_GAMES_LOSS
        defaultRankingShouldNotBeFound("gamesLoss.lessThan=" + DEFAULT_GAMES_LOSS);

        // Get all the rankingList where gamesLoss less than or equals to UPDATED_GAMES_LOSS
        defaultRankingShouldBeFound("gamesLoss.lessThan=" + UPDATED_GAMES_LOSS);
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

        // Get all the rankingList where setsWin greater than or equals to DEFAULT_SETS_WIN
        defaultRankingShouldBeFound("setsWin.greaterOrEqualThan=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin greater than or equals to UPDATED_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.greaterOrEqualThan=" + UPDATED_SETS_WIN);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsWinIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsWin less than or equals to DEFAULT_SETS_WIN
        defaultRankingShouldNotBeFound("setsWin.lessThan=" + DEFAULT_SETS_WIN);

        // Get all the rankingList where setsWin less than or equals to UPDATED_SETS_WIN
        defaultRankingShouldBeFound("setsWin.lessThan=" + UPDATED_SETS_WIN);
    }


    @Test
    @Transactional
    public void getAllRankingsBySetsLostIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLost equals to DEFAULT_SETS_LOST
        defaultRankingShouldBeFound("setsLost.equals=" + DEFAULT_SETS_LOST);

        // Get all the rankingList where setsLost equals to UPDATED_SETS_LOST
        defaultRankingShouldNotBeFound("setsLost.equals=" + UPDATED_SETS_LOST);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLostIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLost in DEFAULT_SETS_LOST or UPDATED_SETS_LOST
        defaultRankingShouldBeFound("setsLost.in=" + DEFAULT_SETS_LOST + "," + UPDATED_SETS_LOST);

        // Get all the rankingList where setsLost equals to UPDATED_SETS_LOST
        defaultRankingShouldNotBeFound("setsLost.in=" + UPDATED_SETS_LOST);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLostIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLost is not null
        defaultRankingShouldBeFound("setsLost.specified=true");

        // Get all the rankingList where setsLost is null
        defaultRankingShouldNotBeFound("setsLost.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLost greater than or equals to DEFAULT_SETS_LOST
        defaultRankingShouldBeFound("setsLost.greaterOrEqualThan=" + DEFAULT_SETS_LOST);

        // Get all the rankingList where setsLost greater than or equals to UPDATED_SETS_LOST
        defaultRankingShouldNotBeFound("setsLost.greaterOrEqualThan=" + UPDATED_SETS_LOST);
    }

    @Test
    @Transactional
    public void getAllRankingsBySetsLostIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where setsLost less than or equals to DEFAULT_SETS_LOST
        defaultRankingShouldNotBeFound("setsLost.lessThan=" + DEFAULT_SETS_LOST);

        // Get all the rankingList where setsLost less than or equals to UPDATED_SETS_LOST
        defaultRankingShouldBeFound("setsLost.lessThan=" + UPDATED_SETS_LOST);
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

        // Get all the rankingList where matchesPlayed greater than or equals to DEFAULT_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.greaterOrEqualThan=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed greater than or equals to UPDATED_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.greaterOrEqualThan=" + UPDATED_MATCHES_PLAYED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesPlayedIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesPlayed less than or equals to DEFAULT_MATCHES_PLAYED
        defaultRankingShouldNotBeFound("matchesPlayed.lessThan=" + DEFAULT_MATCHES_PLAYED);

        // Get all the rankingList where matchesPlayed less than or equals to UPDATED_MATCHES_PLAYED
        defaultRankingShouldBeFound("matchesPlayed.lessThan=" + UPDATED_MATCHES_PLAYED);
    }


    @Test
    @Transactional
    public void getAllRankingsByMatchesWinedIsEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWined equals to DEFAULT_MATCHES_WINED
        defaultRankingShouldBeFound("matchesWined.equals=" + DEFAULT_MATCHES_WINED);

        // Get all the rankingList where matchesWined equals to UPDATED_MATCHES_WINED
        defaultRankingShouldNotBeFound("matchesWined.equals=" + UPDATED_MATCHES_WINED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWinedIsInShouldWork() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWined in DEFAULT_MATCHES_WINED or UPDATED_MATCHES_WINED
        defaultRankingShouldBeFound("matchesWined.in=" + DEFAULT_MATCHES_WINED + "," + UPDATED_MATCHES_WINED);

        // Get all the rankingList where matchesWined equals to UPDATED_MATCHES_WINED
        defaultRankingShouldNotBeFound("matchesWined.in=" + UPDATED_MATCHES_WINED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWinedIsNullOrNotNull() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWined is not null
        defaultRankingShouldBeFound("matchesWined.specified=true");

        // Get all the rankingList where matchesWined is null
        defaultRankingShouldNotBeFound("matchesWined.specified=false");
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWinedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWined greater than or equals to DEFAULT_MATCHES_WINED
        defaultRankingShouldBeFound("matchesWined.greaterOrEqualThan=" + DEFAULT_MATCHES_WINED);

        // Get all the rankingList where matchesWined greater than or equals to UPDATED_MATCHES_WINED
        defaultRankingShouldNotBeFound("matchesWined.greaterOrEqualThan=" + UPDATED_MATCHES_WINED);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesWinedIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesWined less than or equals to DEFAULT_MATCHES_WINED
        defaultRankingShouldNotBeFound("matchesWined.lessThan=" + DEFAULT_MATCHES_WINED);

        // Get all the rankingList where matchesWined less than or equals to UPDATED_MATCHES_WINED
        defaultRankingShouldBeFound("matchesWined.lessThan=" + UPDATED_MATCHES_WINED);
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

        // Get all the rankingList where matchesLoss greater than or equals to DEFAULT_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.greaterOrEqualThan=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss greater than or equals to UPDATED_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.greaterOrEqualThan=" + UPDATED_MATCHES_LOSS);
    }

    @Test
    @Transactional
    public void getAllRankingsByMatchesLossIsLessThanSomething() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList where matchesLoss less than or equals to DEFAULT_MATCHES_LOSS
        defaultRankingShouldNotBeFound("matchesLoss.lessThan=" + DEFAULT_MATCHES_LOSS);

        // Get all the rankingList where matchesLoss less than or equals to UPDATED_MATCHES_LOSS
        defaultRankingShouldBeFound("matchesLoss.lessThan=" + UPDATED_MATCHES_LOSS);
    }


    @Test
    @Transactional
    public void getAllRankingsByTournamentGroupIsEqualToSomething() throws Exception {
        // Initialize the database
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
            .andExpect(jsonPath("$.[*].setsLost").value(hasItem(DEFAULT_SETS_LOST)))
            .andExpect(jsonPath("$.[*].matchesPlayed").value(hasItem(DEFAULT_MATCHES_PLAYED)))
            .andExpect(jsonPath("$.[*].matchesWined").value(hasItem(DEFAULT_MATCHES_WINED)))
            .andExpect(jsonPath("$.[*].matchesLoss").value(hasItem(DEFAULT_MATCHES_LOSS)));

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
            .setsLost(UPDATED_SETS_LOST)
            .matchesPlayed(UPDATED_MATCHES_PLAYED)
            .matchesWined(UPDATED_MATCHES_WINED)
            .matchesLoss(UPDATED_MATCHES_LOSS);

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
        assertThat(testRanking.getSetsLost()).isEqualTo(UPDATED_SETS_LOST);
        assertThat(testRanking.getMatchesPlayed()).isEqualTo(UPDATED_MATCHES_PLAYED);
        assertThat(testRanking.getMatchesWined()).isEqualTo(UPDATED_MATCHES_WINED);
        assertThat(testRanking.getMatchesLoss()).isEqualTo(UPDATED_MATCHES_LOSS);
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
