package org.jhipster101.health.service;

import java.util.Optional;
import org.jhipster101.health.domain.Bloodpressure;
import org.jhipster101.health.repository.BloodpressureRepository;
import org.jhipster101.health.repository.search.BloodpressureSearchRepository;
import org.jhipster101.health.service.dto.BloodpressureDTO;
import org.jhipster101.health.service.mapper.BloodpressureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster101.health.domain.Bloodpressure}.
 */
@Service
@Transactional
public class BloodpressureService {

    private static final Logger LOG = LoggerFactory.getLogger(BloodpressureService.class);

    private final BloodpressureRepository bloodpressureRepository;

    private final BloodpressureMapper bloodpressureMapper;

    private final BloodpressureSearchRepository bloodpressureSearchRepository;

    public BloodpressureService(
        BloodpressureRepository bloodpressureRepository,
        BloodpressureMapper bloodpressureMapper,
        BloodpressureSearchRepository bloodpressureSearchRepository
    ) {
        this.bloodpressureRepository = bloodpressureRepository;
        this.bloodpressureMapper = bloodpressureMapper;
        this.bloodpressureSearchRepository = bloodpressureSearchRepository;
    }

    /**
     * Save a bloodpressure.
     *
     * @param bloodpressureDTO the entity to save.
     * @return the persisted entity.
     */
    public BloodpressureDTO save(BloodpressureDTO bloodpressureDTO) {
        LOG.debug("Request to save Bloodpressure : {}", bloodpressureDTO);
        Bloodpressure bloodpressure = bloodpressureMapper.toEntity(bloodpressureDTO);
        bloodpressure = bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.index(bloodpressure);
        return bloodpressureMapper.toDto(bloodpressure);
    }

    /**
     * Update a bloodpressure.
     *
     * @param bloodpressureDTO the entity to save.
     * @return the persisted entity.
     */
    public BloodpressureDTO update(BloodpressureDTO bloodpressureDTO) {
        LOG.debug("Request to update Bloodpressure : {}", bloodpressureDTO);
        Bloodpressure bloodpressure = bloodpressureMapper.toEntity(bloodpressureDTO);
        bloodpressure = bloodpressureRepository.save(bloodpressure);
        bloodpressureSearchRepository.index(bloodpressure);
        return bloodpressureMapper.toDto(bloodpressure);
    }

    /**
     * Partially update a bloodpressure.
     *
     * @param bloodpressureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BloodpressureDTO> partialUpdate(BloodpressureDTO bloodpressureDTO) {
        LOG.debug("Request to partially update Bloodpressure : {}", bloodpressureDTO);

        return bloodpressureRepository
            .findById(bloodpressureDTO.getId())
            .map(existingBloodpressure -> {
                bloodpressureMapper.partialUpdate(existingBloodpressure, bloodpressureDTO);

                return existingBloodpressure;
            })
            .map(bloodpressureRepository::save)
            .map(savedBloodpressure -> {
                bloodpressureSearchRepository.index(savedBloodpressure);
                return savedBloodpressure;
            })
            .map(bloodpressureMapper::toDto);
    }

    /**
     * Get all the bloodpressures with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BloodpressureDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bloodpressureRepository.findAllWithEagerRelationships(pageable).map(bloodpressureMapper::toDto);
    }

    /**
     * Get one bloodpressure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BloodpressureDTO> findOne(Long id) {
        LOG.debug("Request to get Bloodpressure : {}", id);
        return bloodpressureRepository.findOneWithEagerRelationships(id).map(bloodpressureMapper::toDto);
    }

    /**
     * Delete the bloodpressure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Bloodpressure : {}", id);
        bloodpressureRepository.deleteById(id);
        bloodpressureSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the bloodpressure corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BloodpressureDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Bloodpressures for query {}", query);
        return bloodpressureSearchRepository.search(query, pageable).map(bloodpressureMapper::toDto);
    }
}
