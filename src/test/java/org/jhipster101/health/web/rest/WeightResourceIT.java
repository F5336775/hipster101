package org.jhipster101.health.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.jhipster101.health.domain.WeightAsserts.*;
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
import org.jhipster101.health.domain.User;
import org.jhipster101.health.domain.Weight;
import org.jhipster101.health.repository.UserRepository;
import org.jhipster101.health.repository.WeightRepository;
import org.jhipster101.health.repository.search.WeightSearchRepository;
import org.jhipster101.health.service.WeightService;
import org.jhipster101.health.service.dto.WeightDTO;
import org.jhipster101.health.service.mapper.WeightMapper;
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
 * Integration tests for the {@link WeightResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WeightResourceIT {

    private static final ZonedDateTime DEFAULT_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;
    private static final Integer SMALLER_WEIGHT = 1 - 1;

    private static final String ENTITY_API_URL = "/api/weights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/weights/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private WeightRepository weightRepositoryMock;

    @Autowired
    private WeightMapper weightMapper;

    @Mock
    private WeightService weightServiceMock;

    @Autowired
    private WeightSearchRepository weightSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeightMockMvc;

    private Weight weight;

    private Weight insertedWeight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createEntity() {
        return new Weight().datetime(DEFAULT_DATETIME).weight(DEFAULT_WEIGHT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Weight createUpdatedEntity() {
        return new Weight().datetime(UPDATED_DATETIME).weight(UPDATED_WEIGHT);
    }

    @BeforeEach
    public void initTest() {
        weight = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWeight != null) {
            weightRepository.delete(insertedWeight);
            weightSearchRepository.delete(insertedWeight);
            insertedWeight = null;
        }
    }

    @Test
    @Transactional
    void createWeight() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);
        var returnedWeightDTO = om.readValue(
            restWeightMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weightDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WeightDTO.class
        );

        // Validate the Weight in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWeight = weightMapper.toEntity(returnedWeightDTO);
        assertWeightUpdatableFieldsEquals(returnedWeight, getPersistedWeight(returnedWeight));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWeight = returnedWeight;
    }

    @Test
    @Transactional
    void createWeightWithExistingId() throws Exception {
        // Create the Weight with an existing ID
        weight.setId(1L);
        WeightDTO weightDTO = weightMapper.toDto(weight);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDatetimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        // set the field null
        weight.setDatetime(null);

        // Create the Weight, which fails.
        WeightDTO weightDTO = weightMapper.toDto(weight);

        restWeightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weightDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWeights() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList
        restWeightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWeightsWithEagerRelationshipsIsEnabled() throws Exception {
        when(weightServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWeightMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(weightServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWeightsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(weightServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWeightMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(weightRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getWeight() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get the weight
        restWeightMockMvc
            .perform(get(ENTITY_API_URL_ID, weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weight.getId().intValue()))
            .andExpect(jsonPath("$.datetime").value(sameInstant(DEFAULT_DATETIME)))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    void getWeightsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        Long id = weight.getId();

        defaultWeightFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWeightFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWeightFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime equals to
        defaultWeightFiltering("datetime.equals=" + DEFAULT_DATETIME, "datetime.equals=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime in
        defaultWeightFiltering("datetime.in=" + DEFAULT_DATETIME + "," + UPDATED_DATETIME, "datetime.in=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime is not null
        defaultWeightFiltering("datetime.specified=true", "datetime.specified=false");
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime is greater than or equal to
        defaultWeightFiltering("datetime.greaterThanOrEqual=" + DEFAULT_DATETIME, "datetime.greaterThanOrEqual=" + UPDATED_DATETIME);
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime is less than or equal to
        defaultWeightFiltering("datetime.lessThanOrEqual=" + DEFAULT_DATETIME, "datetime.lessThanOrEqual=" + SMALLER_DATETIME);
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime is less than
        defaultWeightFiltering("datetime.lessThan=" + UPDATED_DATETIME, "datetime.lessThan=" + DEFAULT_DATETIME);
    }

    @Test
    @Transactional
    void getAllWeightsByDatetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where datetime is greater than
        defaultWeightFiltering("datetime.greaterThan=" + SMALLER_DATETIME, "datetime.greaterThan=" + DEFAULT_DATETIME);
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight equals to
        defaultWeightFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight in
        defaultWeightFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight is not null
        defaultWeightFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight is greater than or equal to
        defaultWeightFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight is less than or equal to
        defaultWeightFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight is less than
        defaultWeightFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllWeightsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        // Get all the weightList where weight is greater than
        defaultWeightFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllWeightsByManytooneIsEqualToSomething() throws Exception {
        User manytoone;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            weightRepository.saveAndFlush(weight);
            manytoone = UserResourceIT.createEntity();
        } else {
            manytoone = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(manytoone);
        em.flush();
        weight.setManytoone(manytoone);
        weightRepository.saveAndFlush(weight);
        Long manytooneId = manytoone.getId();
        // Get all the weightList where manytoone equals to manytooneId
        defaultWeightShouldBeFound("manytooneId.equals=" + manytooneId);

        // Get all the weightList where manytoone equals to (manytooneId + 1)
        defaultWeightShouldNotBeFound("manytooneId.equals=" + (manytooneId + 1));
    }

    private void defaultWeightFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWeightShouldBeFound(shouldBeFound);
        defaultWeightShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWeightShouldBeFound(String filter) throws Exception {
        restWeightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));

        // Check, that the count call also returns 1
        restWeightMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWeightShouldNotBeFound(String filter) throws Exception {
        restWeightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWeightMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWeight() throws Exception {
        // Get the weight
        restWeightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeight() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        weightSearchRepository.save(weight);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());

        // Update the weight
        Weight updatedWeight = weightRepository.findById(weight.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWeight are not directly saved in db
        em.detach(updatedWeight);
        updatedWeight.datetime(UPDATED_DATETIME).weight(UPDATED_WEIGHT);
        WeightDTO weightDTO = weightMapper.toDto(updatedWeight);

        restWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weightDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weightDTO))
            )
            .andExpect(status().isOk());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWeightToMatchAllProperties(updatedWeight);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Weight> weightSearchList = Streamable.of(weightSearchRepository.findAll()).toList();
                Weight testWeightSearch = weightSearchList.get(searchDatabaseSizeAfter - 1);

                assertWeightAllPropertiesEquals(testWeightSearch, updatedWeight);
            });
    }

    @Test
    @Transactional
    void putNonExistingWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        weight.setId(longCount.incrementAndGet());

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weightDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        weight.setId(longCount.incrementAndGet());

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(weightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        weight.setId(longCount.incrementAndGet());

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(weightDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWeightWithPatch() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weight using partial update
        Weight partialUpdatedWeight = new Weight();
        partialUpdatedWeight.setId(weight.getId());

        partialUpdatedWeight.datetime(UPDATED_DATETIME).weight(UPDATED_WEIGHT);

        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeight))
            )
            .andExpect(status().isOk());

        // Validate the Weight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeightUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWeight, weight), getPersistedWeight(weight));
    }

    @Test
    @Transactional
    void fullUpdateWeightWithPatch() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the weight using partial update
        Weight partialUpdatedWeight = new Weight();
        partialUpdatedWeight.setId(weight.getId());

        partialUpdatedWeight.datetime(UPDATED_DATETIME).weight(UPDATED_WEIGHT);

        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWeight))
            )
            .andExpect(status().isOk());

        // Validate the Weight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWeightUpdatableFieldsEquals(partialUpdatedWeight, getPersistedWeight(partialUpdatedWeight));
    }

    @Test
    @Transactional
    void patchNonExistingWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        weight.setId(longCount.incrementAndGet());

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weightDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        weight.setId(longCount.incrementAndGet());

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(weightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        weight.setId(longCount.incrementAndGet());

        // Create the Weight
        WeightDTO weightDTO = weightMapper.toDto(weight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeightMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(weightDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Weight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWeight() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);
        weightRepository.save(weight);
        weightSearchRepository.save(weight);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the weight
        restWeightMockMvc
            .perform(delete(ENTITY_API_URL_ID, weight.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(weightSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWeight() throws Exception {
        // Initialize the database
        insertedWeight = weightRepository.saveAndFlush(weight);
        weightSearchRepository.save(weight);

        // Search the weight
        restWeightMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + weight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weight.getId().intValue())))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(sameInstant(DEFAULT_DATETIME))))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    protected long getRepositoryCount() {
        return weightRepository.count();
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

    protected Weight getPersistedWeight(Weight weight) {
        return weightRepository.findById(weight.getId()).orElseThrow();
    }

    protected void assertPersistedWeightToMatchAllProperties(Weight expectedWeight) {
        assertWeightAllPropertiesEquals(expectedWeight, getPersistedWeight(expectedWeight));
    }

    protected void assertPersistedWeightToMatchUpdatableProperties(Weight expectedWeight) {
        assertWeightAllUpdatablePropertiesEquals(expectedWeight, getPersistedWeight(expectedWeight));
    }
}
