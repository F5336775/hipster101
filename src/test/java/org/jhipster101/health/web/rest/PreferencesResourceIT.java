package org.jhipster101.health.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster101.health.domain.PreferencesAsserts.*;
import static org.jhipster101.health.web.rest.TestUtil.createUpdateProxyForBean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.jhipster101.health.IntegrationTest;
import org.jhipster101.health.domain.Preferences;
import org.jhipster101.health.domain.User;
import org.jhipster101.health.repository.PreferencesRepository;
import org.jhipster101.health.repository.UserRepository;
import org.jhipster101.health.repository.search.PreferencesSearchRepository;
import org.jhipster101.health.service.PreferencesService;
import org.jhipster101.health.service.dto.PreferencesDTO;
import org.jhipster101.health.service.mapper.PreferencesMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PreferencesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PreferencesResourceIT {

    private static final String DEFAULT_WEEKELYGOAL = "AAAAAAAAAA";
    private static final String UPDATED_WEEKELYGOAL = "BBBBBBBBBB";

    private static final String DEFAULT_WEIGHTUNITS = "AAAAAAAAAA";
    private static final String UPDATED_WEIGHTUNITS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/preferences/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PreferencesRepository preferencesRepositoryMock;

    @Autowired
    private PreferencesMapper preferencesMapper;

    @Mock
    private PreferencesService preferencesServiceMock;

    @Autowired
    private PreferencesSearchRepository preferencesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreferencesMockMvc;

    private Preferences preferences;

    private Preferences insertedPreferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createEntity() {
        return new Preferences().weekelygoal(DEFAULT_WEEKELYGOAL).weightunits(DEFAULT_WEIGHTUNITS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createUpdatedEntity() {
        return new Preferences().weekelygoal(UPDATED_WEEKELYGOAL).weightunits(UPDATED_WEIGHTUNITS);
    }

    @BeforeEach
    public void initTest() {
        preferences = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPreferences != null) {
            preferencesRepository.delete(insertedPreferences);
            preferencesSearchRepository.delete(insertedPreferences);
            insertedPreferences = null;
        }
    }

    @Test
    @Transactional
    void createPreferences() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);
        var returnedPreferencesDTO = om.readValue(
            restPreferencesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preferencesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PreferencesDTO.class
        );

        // Validate the Preferences in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPreferences = preferencesMapper.toEntity(returnedPreferencesDTO);
        assertPreferencesUpdatableFieldsEquals(returnedPreferences, getPersistedPreferences(returnedPreferences));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedPreferences = returnedPreferences;
    }

    @Test
    @Transactional
    void createPreferencesWithExistingId() throws Exception {
        // Create the Preferences with an existing ID
        preferences.setId(1L);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferencesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPreferences() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList
        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekelygoal").value(hasItem(DEFAULT_WEEKELYGOAL)))
            .andExpect(jsonPath("$.[*].weightunits").value(hasItem(DEFAULT_WEIGHTUNITS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPreferencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(preferencesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPreferencesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(preferencesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPreferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(preferencesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPreferencesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(preferencesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPreferences() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get the preferences
        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL_ID, preferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(preferences.getId().intValue()))
            .andExpect(jsonPath("$.weekelygoal").value(DEFAULT_WEEKELYGOAL))
            .andExpect(jsonPath("$.weightunits").value(DEFAULT_WEIGHTUNITS));
    }

    @Test
    @Transactional
    void getPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        Long id = preferences.getId();

        defaultPreferencesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPreferencesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPreferencesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPreferencesByWeekelygoalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weekelygoal equals to
        defaultPreferencesFiltering("weekelygoal.equals=" + DEFAULT_WEEKELYGOAL, "weekelygoal.equals=" + UPDATED_WEEKELYGOAL);
    }

    @Test
    @Transactional
    void getAllPreferencesByWeekelygoalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weekelygoal in
        defaultPreferencesFiltering(
            "weekelygoal.in=" + DEFAULT_WEEKELYGOAL + "," + UPDATED_WEEKELYGOAL,
            "weekelygoal.in=" + UPDATED_WEEKELYGOAL
        );
    }

    @Test
    @Transactional
    void getAllPreferencesByWeekelygoalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weekelygoal is not null
        defaultPreferencesFiltering("weekelygoal.specified=true", "weekelygoal.specified=false");
    }

    @Test
    @Transactional
    void getAllPreferencesByWeekelygoalContainsSomething() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weekelygoal contains
        defaultPreferencesFiltering("weekelygoal.contains=" + DEFAULT_WEEKELYGOAL, "weekelygoal.contains=" + UPDATED_WEEKELYGOAL);
    }

    @Test
    @Transactional
    void getAllPreferencesByWeekelygoalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weekelygoal does not contain
        defaultPreferencesFiltering(
            "weekelygoal.doesNotContain=" + UPDATED_WEEKELYGOAL,
            "weekelygoal.doesNotContain=" + DEFAULT_WEEKELYGOAL
        );
    }

    @Test
    @Transactional
    void getAllPreferencesByWeightunitsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weightunits equals to
        defaultPreferencesFiltering("weightunits.equals=" + DEFAULT_WEIGHTUNITS, "weightunits.equals=" + UPDATED_WEIGHTUNITS);
    }

    @Test
    @Transactional
    void getAllPreferencesByWeightunitsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weightunits in
        defaultPreferencesFiltering(
            "weightunits.in=" + DEFAULT_WEIGHTUNITS + "," + UPDATED_WEIGHTUNITS,
            "weightunits.in=" + UPDATED_WEIGHTUNITS
        );
    }

    @Test
    @Transactional
    void getAllPreferencesByWeightunitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weightunits is not null
        defaultPreferencesFiltering("weightunits.specified=true", "weightunits.specified=false");
    }

    @Test
    @Transactional
    void getAllPreferencesByWeightunitsContainsSomething() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weightunits contains
        defaultPreferencesFiltering("weightunits.contains=" + DEFAULT_WEIGHTUNITS, "weightunits.contains=" + UPDATED_WEIGHTUNITS);
    }

    @Test
    @Transactional
    void getAllPreferencesByWeightunitsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where weightunits does not contain
        defaultPreferencesFiltering(
            "weightunits.doesNotContain=" + UPDATED_WEIGHTUNITS,
            "weightunits.doesNotContain=" + DEFAULT_WEIGHTUNITS
        );
    }

    @Test
    @Transactional
    void getAllPreferencesByManytooneIsEqualToSomething() throws Exception {
        User manytoone;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            preferencesRepository.saveAndFlush(preferences);
            manytoone = UserResourceIT.createEntity();
        } else {
            manytoone = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(manytoone);
        em.flush();
        preferences.setManytoone(manytoone);
        preferencesRepository.saveAndFlush(preferences);
        Long manytooneId = manytoone.getId();
        // Get all the preferencesList where manytoone equals to manytooneId
        defaultPreferencesShouldBeFound("manytooneId.equals=" + manytooneId);

        // Get all the preferencesList where manytoone equals to (manytooneId + 1)
        defaultPreferencesShouldNotBeFound("manytooneId.equals=" + (manytooneId + 1));
    }

    private void defaultPreferencesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPreferencesShouldBeFound(shouldBeFound);
        defaultPreferencesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPreferencesShouldBeFound(String filter) throws Exception {
        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekelygoal").value(hasItem(DEFAULT_WEEKELYGOAL)))
            .andExpect(jsonPath("$.[*].weightunits").value(hasItem(DEFAULT_WEIGHTUNITS)));

        // Check, that the count call also returns 1
        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPreferencesShouldNotBeFound(String filter) throws Exception {
        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPreferencesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPreferences() throws Exception {
        // Get the preferences
        restPreferencesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPreferences() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        preferencesSearchRepository.save(preferences);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());

        // Update the preferences
        Preferences updatedPreferences = preferencesRepository.findById(preferences.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPreferences are not directly saved in db
        em.detach(updatedPreferences);
        updatedPreferences.weekelygoal(UPDATED_WEEKELYGOAL).weightunits(UPDATED_WEIGHTUNITS);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(updatedPreferences);

        restPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, preferencesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(preferencesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPreferencesToMatchAllProperties(updatedPreferences);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Preferences> preferencesSearchList = Streamable.of(preferencesSearchRepository.findAll()).toList();
                Preferences testPreferencesSearch = preferencesSearchList.get(searchDatabaseSizeAfter - 1);

                assertPreferencesAllPropertiesEquals(testPreferencesSearch, updatedPreferences);
            });
    }

    @Test
    @Transactional
    void putNonExistingPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        preferences.setId(longCount.incrementAndGet());

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, preferencesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(preferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        preferences.setId(longCount.incrementAndGet());

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreferencesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(preferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        preferences.setId(longCount.incrementAndGet());

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreferencesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(preferencesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePreferencesWithPatch() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the preferences using partial update
        Preferences partialUpdatedPreferences = new Preferences();
        partialUpdatedPreferences.setId(preferences.getId());

        partialUpdatedPreferences.weightunits(UPDATED_WEIGHTUNITS);

        restPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPreferences))
            )
            .andExpect(status().isOk());

        // Validate the Preferences in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPreferencesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPreferences, preferences),
            getPersistedPreferences(preferences)
        );
    }

    @Test
    @Transactional
    void fullUpdatePreferencesWithPatch() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the preferences using partial update
        Preferences partialUpdatedPreferences = new Preferences();
        partialUpdatedPreferences.setId(preferences.getId());

        partialUpdatedPreferences.weekelygoal(UPDATED_WEEKELYGOAL).weightunits(UPDATED_WEIGHTUNITS);

        restPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPreferences.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPreferences))
            )
            .andExpect(status().isOk());

        // Validate the Preferences in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPreferencesUpdatableFieldsEquals(partialUpdatedPreferences, getPersistedPreferences(partialUpdatedPreferences));
    }

    @Test
    @Transactional
    void patchNonExistingPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        preferences.setId(longCount.incrementAndGet());

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, preferencesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(preferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        preferences.setId(longCount.incrementAndGet());

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreferencesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(preferencesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPreferences() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        preferences.setId(longCount.incrementAndGet());

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreferencesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(preferencesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Preferences in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePreferences() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);
        preferencesRepository.save(preferences);
        preferencesSearchRepository.save(preferences);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the preferences
        restPreferencesMockMvc
            .perform(delete(ENTITY_API_URL_ID, preferences.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(preferencesSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPreferences() throws Exception {
        // Initialize the database
        insertedPreferences = preferencesRepository.saveAndFlush(preferences);
        preferencesSearchRepository.save(preferences);

        // Search the preferences
        restPreferencesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + preferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekelygoal").value(hasItem(DEFAULT_WEEKELYGOAL)))
            .andExpect(jsonPath("$.[*].weightunits").value(hasItem(DEFAULT_WEIGHTUNITS)));
    }

    protected long getRepositoryCount() {
        return preferencesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Preferences getPersistedPreferences(Preferences preferences) {
        return preferencesRepository.findById(preferences.getId()).orElseThrow();
    }

    protected void assertPersistedPreferencesToMatchAllProperties(Preferences expectedPreferences) {
        assertPreferencesAllPropertiesEquals(expectedPreferences, getPersistedPreferences(expectedPreferences));
    }

    protected void assertPersistedPreferencesToMatchUpdatableProperties(Preferences expectedPreferences) {
        assertPreferencesAllUpdatablePropertiesEquals(expectedPreferences, getPersistedPreferences(expectedPreferences));
    }
}
