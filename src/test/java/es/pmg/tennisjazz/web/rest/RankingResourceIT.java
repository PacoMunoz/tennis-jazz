package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.Ranking;
import es.pmg.tennisjazz.repository.RankingRepository;
import es.pmg.tennisjazz.service.RankingService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;

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
        final RankingResource rankingResource = new RankingResource(rankingService);
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
