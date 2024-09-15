package org.jhipster101.health.service;

import java.util.Optional;
import org.jhipster101.health.domain.Preferences;
import org.jhipster101.health.repository.PreferencesRepository;
import org.jhipster101.health.repository.search.PreferencesSearchRepository;
import org.jhipster101.health.service.dto.PreferencesDTO;
import org.jhipster101.health.service.mapper.PreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster101.health.domain.Preferences}.
 */
@Service
@Transactional
public class PreferencesService {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesService.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    private final PreferencesSearchRepository preferencesSearchRepository;

    public PreferencesService(
        PreferencesRepository preferencesRepository,
        PreferencesMapper preferencesMapper,
        PreferencesSearchRepository preferencesSearchRepository
    ) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
        this.preferencesSearchRepository = preferencesSearchRepository;
    }

    /**
     * Save a preferences.
     *
     * @param preferencesDTO the entity to save.
     * @return the persisted entity.
     */
    public PreferencesDTO save(PreferencesDTO preferencesDTO) {
        LOG.debug("Request to save Preferences : {}", preferencesDTO);
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        preferences = preferencesRepository.save(preferences);
        preferencesSearchRepository.index(preferences);
        return preferencesMapper.toDto(preferences);
    }

    /**
     * Update a preferences.
     *
     * @param preferencesDTO the entity to save.
     * @return the persisted entity.
     */
    public PreferencesDTO update(PreferencesDTO preferencesDTO) {
        LOG.debug("Request to update Preferences : {}", preferencesDTO);
        Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
        preferences = preferencesRepository.save(preferences);
        preferencesSearchRepository.index(preferences);
        return preferencesMapper.toDto(preferences);
    }

    /**
     * Partially update a preferences.
     *
     * @param preferencesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PreferencesDTO> partialUpdate(PreferencesDTO preferencesDTO) {
        LOG.debug("Request to partially update Preferences : {}", preferencesDTO);

        return preferencesRepository
            .findById(preferencesDTO.getId())
            .map(existingPreferences -> {
                preferencesMapper.partialUpdate(existingPreferences, preferencesDTO);

                return existingPreferences;
            })
            .map(preferencesRepository::save)
            .map(savedPreferences -> {
                preferencesSearchRepository.index(savedPreferences);
                return savedPreferences;
            })
            .map(preferencesMapper::toDto);
    }

    /**
     * Get all the preferences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PreferencesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return preferencesRepository.findAllWithEagerRelationships(pageable).map(preferencesMapper::toDto);
    }

    /**
     * Get one preferences by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PreferencesDTO> findOne(Long id) {
        LOG.debug("Request to get Preferences : {}", id);
        return preferencesRepository.findOneWithEagerRelationships(id).map(preferencesMapper::toDto);
    }

    /**
     * Delete the preferences by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Preferences : {}", id);
        preferencesRepository.deleteById(id);
        preferencesSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the preferences corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PreferencesDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Preferences for query {}", query);
        return preferencesSearchRepository.search(query, pageable).map(preferencesMapper::toDto);
    }
}
