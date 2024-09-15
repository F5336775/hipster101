package org.jhipster101.health.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster101.health.domain.BloodpressureAsserts.*;
import static org.jhipster101.health.web.rest.TestUtil.createUpdateProxyForBean;
import static org.jhipster101.health.web.rest.TestUtil.sameInstant;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.jhipster101.health.IntegrationTest;
import org.jhipster101.health.domain.Bloodpressure;
import org.jhipster101.health.domain.User;
import org.jhipster101.health.repository.BloodpressureRepository;
import org.jhipster101.health.repository.UserRepository;
import org.jhipster101.health.repository.search.BloodpressureSearchRepository;
import org.jhipster101.health.service.BloodpressureService;
import org.jhipster101.health.service.dto.BloodpressureDTO;
import org.jhipster101.health.service.mapper.BloodpressureMapper;
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
 * Integration tests for the {@link BloodpressureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BloodpressureResourceIT {

    private static final ZonedDateTime DEFAULT_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_SYSTOLIC = 1;
    private static final Integer UPDATED_SYSTOLIC = 2;
    private static final Integer SMALLER_SYSTOLIC = 1 - 1;

    private static final Integer DEFAULT_DIASTOLIC = 1;
    private static final Integer UPDATED_DIASTOLIC = 2;
    private static final Integer SMALLER_DIASTOLIC = 1 - 1;

    private static final String ENTITY_API_URL = "/api/bloodpressures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/bloodpressures/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BloodpressureRepository bloodpressureRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private BloodpressureRepository bloodpressureRepositoryMock;

    @Autowired
    private BloodpressureMapper bloodpressureMapper;

    @Mock
    private BloodpressureService bloodpressureServiceMock;

    @Autowired
    private BloodpressureSearchRepository bloodpressureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBloodpressureMockMvc;

    private Bloodpressure bloodpressure;

    private Bloodpressure insertedBloodpressure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bloodpressure createEntity() {
        return new Bloodpressure().datetime(DEFAULT_DATETIME).systolic(DEFAULT_SYSTOLIC).diastolic(DEFAULT_DIASTOLIC);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bloodpressure createUpdatedEntity() {
        return new Bloodpressure().datetime(UPDATED_DATETIME).systolic(UPDATED_SYSTOLIC).diastolic(UPDATED_DIASTOLIC);
    }

    @BeforeEach
    public void initTest() {
        bloodpressure = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBloodpressure != null) {
            bloodpressureRepository.delete(insertedBloodpressure);
            bloodpressureSearchRepository.delete(insertedBloodpressure);
            insertedBloodpressure = null;
        }
    }

    @Test
    @Transactional
    void createBloodpressure() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);
        var returnedBloodpressureDTO = om.readValue(
            restBloodpressureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodpressureDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BloodpressureDTO.class
        );

        // Validate the Bloodpressure in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBloodpressure = bloodpressureMapper.toEntity(returnedBloodpressureDTO);
        assertBloodpressureUpdatableFieldsEquals(returnedBloodpressure, getPersistedBloodpressure(returnedBloodpressure));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedBloodpressure = returnedBloodpressure;
    }

    @Test
    @Transactional
    void createBloodpressureWithExistingId() throws Exception {
        // Create the Bloodpressure with an existing ID
        bloodpressure.setId(1L);
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloodpressureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodpressureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDatetimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        // set the field null
        bloodpressure.setDatetime(null);

        // Create the Bloodpressure, which fails.
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        restBloodpressureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodpressureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllBloodpressures() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList
        restBloodpressureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBloodpressuresWithEagerRelationshipsIsEnabled() throws Exception {
        when(bloodpressureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBloodpressureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bloodpressureServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBloodpressuresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bloodpressureServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBloodpressureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(bloodpressureRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBloodpressure() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get the bloodpressure
        restBloodpressureMockMvc
            .perform(get(ENTITY_API_URL_ID, bloodpressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bloodpressure.getId().intValue()))
            .andExpect(jsonPath("$.datetime").value(sameInstant(DEFAULT_DATETIME)))
            .andExpect(jsonPath("$.systolic").value(DEFAULT_SYSTOLIC))
            .andExpect(jsonPath("$.diastolic").value(DEFAULT_DIASTOLIC));
    }

    @Test
    @Transactional
    void getBloodpressuresByIdFiltering() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        Long id = bloodpressure.getId();

        defaultBloodpressureFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBloodpressureFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBloodpressureFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime equals to
        defaultBloodpressureFiltering("datetime.equals=" + DEFAULT_DATETIME, "datetime.equals=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime in
        defaultBloodpressureFiltering("datetime.in=" + DEFAULT_DATETIME + "," + UPDATED_DATETIME, "datetime.in=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime is not null
        defaultBloodpressureFiltering("datetime.specified=true", "datetime.specified=false");
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime is greater than or equal to
        defaultBloodpressureFiltering("datetime.greaterThanOrEqual=" + DEFAULT_DATETIME, "datetime.greaterThanOrEqual=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime is less than or equal to
        defaultBloodpressureFiltering("datetime.lessThanOrEqual=" + DEFAULT_DATETIME, "datetime.lessThanOrEqual=" + SMALLER_DATETIME);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime is less than
        defaultBloodpressureFiltering("datetime.lessThan=" + UPDATED_DATETIME, "datetime.lessThan=" + DEFAULT_DATETIME);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where datetime is greater than
        defaultBloodpressureFiltering("datetime.greaterThan=" + SMALLER_DATETIME, "datetime.greaterThan=" + DEFAULT_DATETIME);
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic equals to
        defaultBloodpressureFiltering("systolic.equals=" + DEFAULT_SYSTOLIC, "systolic.equals=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic in
        defaultBloodpressureFiltering("systolic.in=" + DEFAULT_SYSTOLIC + "," + UPDATED_SYSTOLIC, "systolic.in=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic is not null
        defaultBloodpressureFiltering("systolic.specified=true", "systolic.specified=false");
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic is greater than or equal to
        defaultBloodpressureFiltering("systolic.greaterThanOrEqual=" + DEFAULT_SYSTOLIC, "systolic.greaterThanOrEqual=" + UPDATED_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic is less than or equal to
        defaultBloodpressureFiltering("systolic.lessThanOrEqual=" + DEFAULT_SYSTOLIC, "systolic.lessThanOrEqual=" + SMALLER_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic is less than
        defaultBloodpressureFiltering("systolic.lessThan=" + UPDATED_SYSTOLIC, "systolic.lessThan=" + DEFAULT_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresBySystolicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where systolic is greater than
        defaultBloodpressureFiltering("systolic.greaterThan=" + SMALLER_SYSTOLIC, "systolic.greaterThan=" + DEFAULT_SYSTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic equals to
        defaultBloodpressureFiltering("diastolic.equals=" + DEFAULT_DIASTOLIC, "diastolic.equals=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic in
        defaultBloodpressureFiltering("diastolic.in=" + DEFAULT_DIASTOLIC + "," + UPDATED_DIASTOLIC, "diastolic.in=" + UPDATED_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic is not null
        defaultBloodpressureFiltering("diastolic.specified=true", "diastolic.specified=false");
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic is greater than or equal to
        defaultBloodpressureFiltering(
            "diastolic.greaterThanOrEqual=" + DEFAULT_DIASTOLIC,
            "diastolic.greaterThanOrEqual=" + UPDATED_DIASTOLIC
        );
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic is less than or equal to
        defaultBloodpressureFiltering("diastolic.lessThanOrEqual=" + DEFAULT_DIASTOLIC, "diastolic.lessThanOrEqual=" + SMALLER_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic is less than
        defaultBloodpressureFiltering("diastolic.lessThan=" + UPDATED_DIASTOLIC, "diastolic.lessThan=" + DEFAULT_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByDiastolicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        // Get all the bloodpressureList where diastolic is greater than
        defaultBloodpressureFiltering("diastolic.greaterThan=" + SMALLER_DIASTOLIC, "diastolic.greaterThan=" + DEFAULT_DIASTOLIC);
    }

    @Test
    @Transactional
    void getAllBloodpressuresByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            bloodpressureRepository.saveAndFlush(bloodpressure);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        bloodpressure.setUser(user);
        bloodpressureRepository.saveAndFlush(bloodpressure);
        Long userId = user.getId();
        // Get all the bloodpressureList where user equals to userId
        defaultBloodpressureShouldBeFound("userId.equals=" + userId);

        // Get all the bloodpressureList where user equals to (userId + 1)
        defaultBloodpressureShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultBloodpressureFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBloodpressureShouldBeFound(shouldBeFound);
        defaultBloodpressureShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBloodpressureShouldBeFound(String filter) throws Exception {
        restBloodpressureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));

        // Check, that the count call also returns 1
        restBloodpressureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBloodpressureShouldNotBeFound(String filter) throws Exception {
        restBloodpressureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBloodpressureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBloodpressure() throws Exception {
        // Get the bloodpressure
        restBloodpressureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBloodpressure() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloodpressureSearchRepository.save(bloodpressure);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());

        // Update the bloodpressure
        Bloodpressure updatedBloodpressure = bloodpressureRepository.findById(bloodpressure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBloodpressure are not directly saved in db
        em.detach(updatedBloodpressure);
        updatedBloodpressure.datetime(UPDATED_DATETIME).systolic(UPDATED_SYSTOLIC).diastolic(UPDATED_DIASTOLIC);
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(updatedBloodpressure);

        restBloodpressureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bloodpressureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloodpressureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBloodpressureToMatchAllProperties(updatedBloodpressure);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Bloodpressure> bloodpressureSearchList = Streamable.of(bloodpressureSearchRepository.findAll()).toList();
                Bloodpressure testBloodpressureSearch = bloodpressureSearchList.get(searchDatabaseSizeAfter - 1);

                assertBloodpressureAllPropertiesEquals(testBloodpressureSearch, updatedBloodpressure);
            });
    }

    @Test
    @Transactional
    void putNonExistingBloodpressure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        bloodpressure.setId(longCount.incrementAndGet());

        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBloodpressureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bloodpressureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloodpressureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchBloodpressure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        bloodpressure.setId(longCount.incrementAndGet());

        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodpressureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloodpressureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBloodpressure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        bloodpressure.setId(longCount.incrementAndGet());

        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodpressureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloodpressureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateBloodpressureWithPatch() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloodpressure using partial update
        Bloodpressure partialUpdatedBloodpressure = new Bloodpressure();
        partialUpdatedBloodpressure.setId(bloodpressure.getId());

        partialUpdatedBloodpressure.diastolic(UPDATED_DIASTOLIC);

        restBloodpressureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBloodpressure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBloodpressure))
            )
            .andExpect(status().isOk());

        // Validate the Bloodpressure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBloodpressureUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBloodpressure, bloodpressure),
            getPersistedBloodpressure(bloodpressure)
        );
    }

    @Test
    @Transactional
    void fullUpdateBloodpressureWithPatch() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloodpressure using partial update
        Bloodpressure partialUpdatedBloodpressure = new Bloodpressure();
        partialUpdatedBloodpressure.setId(bloodpressure.getId());

        partialUpdatedBloodpressure.datetime(UPDATED_DATETIME).systolic(UPDATED_SYSTOLIC).diastolic(UPDATED_DIASTOLIC);

        restBloodpressureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBloodpressure.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBloodpressure))
            )
            .andExpect(status().isOk());

        // Validate the Bloodpressure in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBloodpressureUpdatableFieldsEquals(partialUpdatedBloodpressure, getPersistedBloodpressure(partialUpdatedBloodpressure));
    }

    @Test
    @Transactional
    void patchNonExistingBloodpressure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        bloodpressure.setId(longCount.incrementAndGet());

        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBloodpressureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bloodpressureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bloodpressureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBloodpressure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        bloodpressure.setId(longCount.incrementAndGet());

        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodpressureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bloodpressureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBloodpressure() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        bloodpressure.setId(longCount.incrementAndGet());

        // Create the Bloodpressure
        BloodpressureDTO bloodpressureDTO = bloodpressureMapper.toDto(bloodpressure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloodpressureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bloodpressureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bloodpressure in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteBloodpressure() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the bloodpressure
        restBloodpressureMockMvc
            .perform(delete(ENTITY_API_URL_ID, bloodpressure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(bloodpressureSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchBloodpressure() throws Exception {
        // Initialize the database
        insertedBloodpressure = bloodpressureRepository.saveAndFlush(bloodpressure);
        bloodpressureSearchRepository.save(bloodpressure);

        // Search the bloodpressure
        restBloodpressureMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + bloodpressure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloodpressure.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].systolic").value(hasItem(DEFAULT_SYSTOLIC)))
            .andExpect(jsonPath("$.[*].diastolic").value(hasItem(DEFAULT_DIASTOLIC)));
    }

    protected long getRepositoryCount() {
        return bloodpressureRepository.count();
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

    protected Bloodpressure getPersistedBloodpressure(Bloodpressure bloodpressure) {
        return bloodpressureRepository.findById(bloodpressure.getId()).orElseThrow();
    }

    protected void assertPersistedBloodpressureToMatchAllProperties(Bloodpressure expectedBloodpressure) {
        assertBloodpressureAllPropertiesEquals(expectedBloodpressure, getPersistedBloodpressure(expectedBloodpressure));
    }

    protected void assertPersistedBloodpressureToMatchUpdatableProperties(Bloodpressure expectedBloodpressure) {
        assertBloodpressureAllUpdatablePropertiesEquals(expectedBloodpressure, getPersistedBloodpressure(expectedBloodpressure));
    }
}
