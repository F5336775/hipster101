package org.jhipster101.health.service;

import java.util.Optional;
import org.jhipster101.health.domain.Weight;
import org.jhipster101.health.repository.WeightRepository;
import org.jhipster101.health.repository.search.WeightSearchRepository;
import org.jhipster101.health.service.dto.WeightDTO;
import org.jhipster101.health.service.mapper.WeightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster101.health.domain.Weight}.
 */
@Service
@Transactional
public class WeightService {

    private static final Logger LOG = LoggerFactory.getLogger(WeightService.class);

    private final WeightRepository weightRepository;

    private final WeightMapper weightMapper;

    private final WeightSearchRepository weightSearchRepository;

    public WeightService(WeightRepository weightRepository, WeightMapper weightMapper, WeightSearchRepository weightSearchRepository) {
        this.weightRepository = weightRepository;
        this.weightMapper = weightMapper;
        this.weightSearchRepository = weightSearchRepository;
    }

    /**
     * Save a weight.
     *
     * @param weightDTO the entity to save.
     * @return the persisted entity.
     */
    public WeightDTO save(WeightDTO weightDTO) {
        LOG.debug("Request to save Weight : {}", weightDTO);
        Weight weight = weightMapper.toEntity(weightDTO);
        weight = weightRepository.save(weight);
        weightSearchRepository.index(weight);
        return weightMapper.toDto(weight);
    }

    /**
     * Update a weight.
     *
     * @param weightDTO the entity to save.
     * @return the persisted entity.
     */
    public WeightDTO update(WeightDTO weightDTO) {
        LOG.debug("Request to update Weight : {}", weightDTO);
        Weight weight = weightMapper.toEntity(weightDTO);
        weight = weightRepository.save(weight);
        weightSearchRepository.index(weight);
        return weightMapper.toDto(weight);
    }

    /**
     * Partially update a weight.
     *
     * @param weightDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WeightDTO> partialUpdate(WeightDTO weightDTO) {
        LOG.debug("Request to partially update Weight : {}", weightDTO);

        return weightRepository
            .findById(weightDTO.getId())
            .map(existingWeight -> {
                weightMapper.partialUpdate(existingWeight, weightDTO);

                return existingWeight;
            })
            .map(weightRepository::save)
            .map(savedWeight -> {
                weightSearchRepository.index(savedWeight);
                return savedWeight;
            })
            .map(weightMapper::toDto);
    }

    /**
     * Get all the weights with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WeightDTO> findAllWithEagerRelationships(Pageable pageable) {
        return weightRepository.findAllWithEagerRelationships(pageable).map(weightMapper::toDto);
    }

    /**
     * Get one weight by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WeightDTO> findOne(Long id) {
        LOG.debug("Request to get Weight : {}", id);
        return weightRepository.findOneWithEagerRelationships(id).map(weightMapper::toDto);
    }

    /**
     * Delete the weight by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Weight : {}", id);
        weightRepository.deleteById(id);
        weightSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the weight corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WeightDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Weights for query {}", query);
        return weightSearchRepository.search(query, pageable).map(weightMapper::toDto);
    }
}
