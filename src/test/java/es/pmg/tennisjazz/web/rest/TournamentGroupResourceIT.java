package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.domain.Tournament;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.repository.TournamentGroupRepository;
import es.pmg.tennisjazz.service.TournamentGroupService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;
import es.pmg.tennisjazz.service.dto.TournamentGroupCriteria;
import es.pmg.tennisjazz.service.TournamentGroupQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static es.pmg.tennisjazz.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TournamentGroupResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class TournamentGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TournamentGroupRepository tournamentGroupRepository;

    @Mock
    private TournamentGroupRepository tournamentGroupRepositoryMock;

    @Mock
    private TournamentGroupService tournamentGroupServiceMock;

    @Autowired
    private TournamentGroupService tournamentGroupService;

    @Autowired
    private TournamentGroupQueryService tournamentGroupQueryService;

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

    private MockMvc restTournamentGroupMockMvc;

    private TournamentGroup tournamentGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TournamentGroupResource tournamentGroupResource = new TournamentGroupResource(tournamentGroupService, tournamentGroupQueryService);
        this.restTournamentGroupMockMvc = MockMvcBuilders.standaloneSetup(tournamentGroupResource)
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
    public static TournamentGroup createEntity(EntityManager em) {
        TournamentGroup tournamentGroup = new TournamentGroup()
            .name(DEFAULT_NAME);
        return tournamentGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TournamentGroup createUpdatedEntity(EntityManager em) {
        TournamentGroup tournamentGroup = new TournamentGroup()
            .name(UPDATED_NAME);
        return tournamentGroup;
    }

    @BeforeEach
    public void initTest() {
        tournamentGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createTournamentGroup() throws Exception {
        int databaseSizeBeforeCreate = tournamentGroupRepository.findAll().size();

        // Create the TournamentGroup
        restTournamentGroupMockMvc.perform(post("/api/tournament-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentGroup)))
            .andExpect(status().isCreated());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeCreate + 1);
        TournamentGroup testTournamentGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
        assertThat(testTournamentGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTournamentGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tournamentGroupRepository.findAll().size();

        // Create the TournamentGroup with an existing ID
        tournamentGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentGroupMockMvc.perform(post("/api/tournament-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentGroup)))
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTournamentGroups() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournamentGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTournamentGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        TournamentGroupResource tournamentGroupResource = new TournamentGroupResource(tournamentGroupServiceMock, tournamentGroupQueryService);
        when(tournamentGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restTournamentGroupMockMvc = MockMvcBuilders.standaloneSetup(tournamentGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTournamentGroupMockMvc.perform(get("/api/tournament-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(tournamentGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTournamentGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        TournamentGroupResource tournamentGroupResource = new TournamentGroupResource(tournamentGroupServiceMock, tournamentGroupQueryService);
            when(tournamentGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restTournamentGroupMockMvc = MockMvcBuilders.standaloneSetup(tournamentGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restTournamentGroupMockMvc.perform(get("/api/tournament-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(tournamentGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTournamentGroup() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get the tournamentGroup
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups/{id}", tournamentGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tournamentGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllTournamentGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name equals to DEFAULT_NAME
        defaultTournamentGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tournamentGroupList where name equals to UPDATED_NAME
        defaultTournamentGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTournamentGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTournamentGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tournamentGroupList where name equals to UPDATED_NAME
        defaultTournamentGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTournamentGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tournamentGroupRepository.saveAndFlush(tournamentGroup);

        // Get all the tournamentGroupList where name is not null
        defaultTournamentGroupShouldBeFound("name.specified=true");

        // Get all the tournamentGroupList where name is null
        defaultTournamentGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllTournamentGroupsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        tournamentGroup.setTournament(tournament);
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Long tournamentId = tournament.getId();

        // Get all the tournamentGroupList where tournament equals to tournamentId
        defaultTournamentGroupShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the tournamentGroupList where tournament equals to tournamentId + 1
        defaultTournamentGroupShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }


    @Test
    @Transactional
    public void getAllTournamentGroupsByRoundsIsEqualToSomething() throws Exception {
        // Initialize the database
        Round rounds = RoundResourceIT.createEntity(em);
        em.persist(rounds);
        em.flush();
        tournamentGroup.addRounds(rounds);
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Long roundsId = rounds.getId();

        // Get all the tournamentGroupList where rounds equals to roundsId
        defaultTournamentGroupShouldBeFound("roundsId.equals=" + roundsId);

        // Get all the tournamentGroupList where rounds equals to roundsId + 1
        defaultTournamentGroupShouldNotBeFound("roundsId.equals=" + (roundsId + 1));
    }


    @Test
    @Transactional
    public void getAllTournamentGroupsByPlayersIsEqualToSomething() throws Exception {
        // Initialize the database
        Player players = PlayerResourceIT.createEntity(em);
        em.persist(players);
        em.flush();
        tournamentGroup.addPlayers(players);
        tournamentGroupRepository.saveAndFlush(tournamentGroup);
        Long playersId = players.getId();

        // Get all the tournamentGroupList where players equals to playersId
        defaultTournamentGroupShouldBeFound("playersId.equals=" + playersId);

        // Get all the tournamentGroupList where players equals to playersId + 1
        defaultTournamentGroupShouldNotBeFound("playersId.equals=" + (playersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTournamentGroupShouldBeFound(String filter) throws Exception {
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournamentGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTournamentGroupShouldNotBeFound(String filter) throws Exception {
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTournamentGroup() throws Exception {
        // Get the tournamentGroup
        restTournamentGroupMockMvc.perform(get("/api/tournament-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTournamentGroup() throws Exception {
        // Initialize the database
        tournamentGroupService.save(tournamentGroup);

        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();

        // Update the tournamentGroup
        TournamentGroup updatedTournamentGroup = tournamentGroupRepository.findById(tournamentGroup.getId()).get();
        // Disconnect from session so that the updates on updatedTournamentGroup are not directly saved in db
        em.detach(updatedTournamentGroup);
        updatedTournamentGroup
            .name(UPDATED_NAME);

        restTournamentGroupMockMvc.perform(put("/api/tournament-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTournamentGroup)))
            .andExpect(status().isOk());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
        TournamentGroup testTournamentGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
        assertThat(testTournamentGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTournamentGroup() throws Exception {
        int databaseSizeBeforeUpdate = tournamentGroupRepository.findAll().size();

        // Create the TournamentGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentGroupMockMvc.perform(put("/api/tournament-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentGroup)))
            .andExpect(status().isBadRequest());

        // Validate the TournamentGroup in the database
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTournamentGroup() throws Exception {
        // Initialize the database
        tournamentGroupService.save(tournamentGroup);

        int databaseSizeBeforeDelete = tournamentGroupRepository.findAll().size();

        // Delete the tournamentGroup
        restTournamentGroupMockMvc.perform(delete("/api/tournament-groups/{id}", tournamentGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TournamentGroup> tournamentGroupList = tournamentGroupRepository.findAll();
        assertThat(tournamentGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TournamentGroup.class);
        TournamentGroup tournamentGroup1 = new TournamentGroup();
        tournamentGroup1.setId(1L);
        TournamentGroup tournamentGroup2 = new TournamentGroup();
        tournamentGroup2.setId(tournamentGroup1.getId());
        assertThat(tournamentGroup1).isEqualTo(tournamentGroup2);
        tournamentGroup2.setId(2L);
        assertThat(tournamentGroup1).isNotEqualTo(tournamentGroup2);
        tournamentGroup1.setId(null);
        assertThat(tournamentGroup1).isNotEqualTo(tournamentGroup2);
    }
}
