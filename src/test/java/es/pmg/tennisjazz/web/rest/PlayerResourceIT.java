package es.pmg.tennisjazz.web.rest;

import es.pmg.tennisjazz.TennisJazzApp;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Ranking;
import es.pmg.tennisjazz.domain.Gender;
import es.pmg.tennisjazz.domain.User;
import es.pmg.tennisjazz.domain.TournamentGroup;
import es.pmg.tennisjazz.repository.PlayerRepository;
import es.pmg.tennisjazz.service.PlayerService;
import es.pmg.tennisjazz.web.rest.errors.ExceptionTranslator;
import es.pmg.tennisjazz.service.dto.PlayerCriteria;
import es.pmg.tennisjazz.service.PlayerQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static es.pmg.tennisjazz.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlayerResource} REST controller.
 */
@SpringBootTest(classes = TennisJazzApp.class)
public class PlayerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER = "AAAAAAAAAA";
    private static final String UPDATED_OTHER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerQueryService playerQueryService;

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

    private MockMvc restPlayerMockMvc;

    private Player player;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerResource playerResource = new PlayerResource(playerService, playerQueryService);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
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
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .other(DEFAULT_OTHER)
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE);
        return player;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createUpdatedEntity(EntityManager em) {
        Player player = new Player()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .other(UPDATED_OTHER)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE);
        return player;
    }

    @BeforeEach
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlayer.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testPlayer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPlayer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPlayer.getOther()).isEqualTo(DEFAULT_OTHER);
        assertThat(testPlayer.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testPlayer.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        player.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setName(null);

        // Create the Player, which fails.

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setEmail(null);

        // Create the Player, which fails.

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setPhone(null);

        // Create the Player, which fails.

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER.toString())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));
    }
    
    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.other").value(DEFAULT_OTHER.toString()))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)));
    }

    @Test
    @Transactional
    public void getAllPlayersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name equals to DEFAULT_NAME
        defaultPlayerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the playerList where name equals to UPDATED_NAME
        defaultPlayerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlayerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the playerList where name equals to UPDATED_NAME
        defaultPlayerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlayersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where name is not null
        defaultPlayerShouldBeFound("name.specified=true");

        // Get all the playerList where name is null
        defaultPlayerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where surname equals to DEFAULT_SURNAME
        defaultPlayerShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the playerList where surname equals to UPDATED_SURNAME
        defaultPlayerShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPlayersBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultPlayerShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the playerList where surname equals to UPDATED_SURNAME
        defaultPlayerShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllPlayersBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where surname is not null
        defaultPlayerShouldBeFound("surname.specified=true");

        // Get all the playerList where surname is null
        defaultPlayerShouldNotBeFound("surname.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email equals to DEFAULT_EMAIL
        defaultPlayerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the playerList where email equals to UPDATED_EMAIL
        defaultPlayerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPlayerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the playerList where email equals to UPDATED_EMAIL
        defaultPlayerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPlayersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where email is not null
        defaultPlayerShouldBeFound("email.specified=true");

        // Get all the playerList where email is null
        defaultPlayerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone equals to DEFAULT_PHONE
        defaultPlayerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the playerList where phone equals to UPDATED_PHONE
        defaultPlayerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPlayerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the playerList where phone equals to UPDATED_PHONE
        defaultPlayerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPlayersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where phone is not null
        defaultPlayerShouldBeFound("phone.specified=true");

        // Get all the playerList where phone is null
        defaultPlayerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByOtherIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where other equals to DEFAULT_OTHER
        defaultPlayerShouldBeFound("other.equals=" + DEFAULT_OTHER);

        // Get all the playerList where other equals to UPDATED_OTHER
        defaultPlayerShouldNotBeFound("other.equals=" + UPDATED_OTHER);
    }

    @Test
    @Transactional
    public void getAllPlayersByOtherIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where other in DEFAULT_OTHER or UPDATED_OTHER
        defaultPlayerShouldBeFound("other.in=" + DEFAULT_OTHER + "," + UPDATED_OTHER);

        // Get all the playerList where other equals to UPDATED_OTHER
        defaultPlayerShouldNotBeFound("other.in=" + UPDATED_OTHER);
    }

    @Test
    @Transactional
    public void getAllPlayersByOtherIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where other is not null
        defaultPlayerShouldBeFound("other.specified=true");

        // Get all the playerList where other is null
        defaultPlayerShouldNotBeFound("other.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByVisitorMatchesIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        Match visitorMatches = MatchResourceIT.createEntity(em);
        em.persist(visitorMatches);
        em.flush();
        player.addVisitorMatches(visitorMatches);
        playerRepository.saveAndFlush(player);
        Long visitorMatchesId = visitorMatches.getId();

        // Get all the playerList where visitorMatches equals to visitorMatchesId
        defaultPlayerShouldBeFound("visitorMatchesId.equals=" + visitorMatchesId);

        // Get all the playerList where visitorMatches equals to visitorMatchesId + 1
        defaultPlayerShouldNotBeFound("visitorMatchesId.equals=" + (visitorMatchesId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayersByLocalMatchesIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        Match localMatches = MatchResourceIT.createEntity(em);
        em.persist(localMatches);
        em.flush();
        player.addLocalMatches(localMatches);
        playerRepository.saveAndFlush(player);
        Long localMatchesId = localMatches.getId();

        // Get all the playerList where localMatches equals to localMatchesId
        defaultPlayerShouldBeFound("localMatchesId.equals=" + localMatchesId);

        // Get all the playerList where localMatches equals to localMatchesId + 1
        defaultPlayerShouldNotBeFound("localMatchesId.equals=" + (localMatchesId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayersByRankingsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        Ranking rankings = RankingResourceIT.createEntity(em);
        em.persist(rankings);
        em.flush();
        player.addRankings(rankings);
        playerRepository.saveAndFlush(player);
        Long rankingsId = rankings.getId();

        // Get all the playerList where rankings equals to rankingsId
        defaultPlayerShouldBeFound("rankingsId.equals=" + rankingsId);

        // Get all the playerList where rankings equals to rankingsId + 1
        defaultPlayerShouldNotBeFound("rankingsId.equals=" + (rankingsId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        Gender gender = GenderResourceIT.createEntity(em);
        em.persist(gender);
        em.flush();
        player.setGender(gender);
        playerRepository.saveAndFlush(player);
        Long genderId = gender.getId();

        // Get all the playerList where gender equals to genderId
        defaultPlayerShouldBeFound("genderId.equals=" + genderId);

        // Get all the playerList where gender equals to genderId + 1
        defaultPlayerShouldNotBeFound("genderId.equals=" + (genderId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        player.setUser(user);
        playerRepository.saveAndFlush(player);
        Long userId = user.getId();

        // Get all the playerList where user equals to userId
        defaultPlayerShouldBeFound("userId.equals=" + userId);

        // Get all the playerList where user equals to userId + 1
        defaultPlayerShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayersByGroupsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        TournamentGroup groups = TournamentGroupResourceIT.createEntity(em);
        em.persist(groups);
        em.flush();
        player.addGroups(groups);
        playerRepository.saveAndFlush(player);
        Long groupsId = groups.getId();

        // Get all the playerList where groups equals to groupsId
        defaultPlayerShouldBeFound("groupsId.equals=" + groupsId);

        // Get all the playerList where groups equals to groupsId + 1
        defaultPlayerShouldNotBeFound("groupsId.equals=" + (groupsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerShouldBeFound(String filter) throws Exception {
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].other").value(hasItem(DEFAULT_OTHER)))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))));

        // Check, that the count call also returns 1
        restPlayerMockMvc.perform(get("/api/players/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerShouldNotBeFound(String filter) throws Exception {
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerMockMvc.perform(get("/api/players/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerService.save(player);

        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findById(player.getId()).get();
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .other(UPDATED_OTHER)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE);

        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayer)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlayer.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testPlayer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPlayer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPlayer.getOther()).isEqualTo(UPDATED_OTHER);
        assertThat(testPlayer.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testPlayer.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerService.save(player);

        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Delete the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Player.class);
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(player1.getId());
        assertThat(player1).isEqualTo(player2);
        player2.setId(2L);
        assertThat(player1).isNotEqualTo(player2);
        player1.setId(null);
        assertThat(player1).isNotEqualTo(player2);
    }
}
