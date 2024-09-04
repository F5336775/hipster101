package org.jhipster101.health.service;

import java.util.Optional;
import org.jhipster101.health.domain.Points;
import org.jhipster101.health.repository.PointsRepository;
import org.jhipster101.health.repository.search.PointsSearchRepository;
import org.jhipster101.health.service.dto.PointsDTO;
import org.jhipster101.health.service.mapper.PointsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.jhipster101.health.domain.Points}.
 */
@Service
@Transactional
public class PointsService {

    private static final Logger LOG = LoggerFactory.getLogger(PointsService.class);

    private final PointsRepository pointsRepository;

    private final PointsMapper pointsMapper;

    private final PointsSearchRepository pointsSearchRepository;

    public PointsService(PointsRepository pointsRepository, PointsMapper pointsMapper, PointsSearchRepository pointsSearchRepository) {
        this.pointsRepository = pointsRepository;
        this.pointsMapper = pointsMapper;
        this.pointsSearchRepository = pointsSearchRepository;
    }

    /**
     * Save a points.
     *
     * @param pointsDTO the entity to save.
     * @return the persisted entity.
     */
    public PointsDTO save(PointsDTO pointsDTO) {
        LOG.debug("Request to save Points : {}", pointsDTO);
        Points points = pointsMapper.toEntity(pointsDTO);
        points = pointsRepository.save(points);
        pointsSearchRepository.index(points);
        return pointsMapper.toDto(points);
    }

    /**
     * Update a points.
     *
     * @param pointsDTO the entity to save.
     * @return the persisted entity.
     */
    public PointsDTO update(PointsDTO pointsDTO) {
        LOG.debug("Request to update Points : {}", pointsDTO);
        Points points = pointsMapper.toEntity(pointsDTO);
        points = pointsRepository.save(points);
        pointsSearchRepository.index(points);
        return pointsMapper.toDto(points);
    }

    /**
     * Partially update a points.
     *
     * @param pointsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PointsDTO> partialUpdate(PointsDTO pointsDTO) {
        LOG.debug("Request to partially update Points : {}", pointsDTO);

        return pointsRepository
            .findById(pointsDTO.getId())
            .map(existingPoints -> {
                pointsMapper.partialUpdate(existingPoints, pointsDTO);

                return existingPoints;
            })
            .map(pointsRepository::save)
            .map(savedPoints -> {
                pointsSearchRepository.index(savedPoints);
                return savedPoints;
            })
            .map(pointsMapper::toDto);
    }

    /**
     * Get all the points.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PointsDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Points");
        return pointsRepository.findAll(pageable).map(pointsMapper::toDto);
    }

    /**
     * Get all the points with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PointsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pointsRepository.findAllWithEagerRelationships(pageable).map(pointsMapper::toDto);
    }

    /**
     * Get one points by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointsDTO> findOne(Long id) {
        LOG.debug("Request to get Points : {}", id);
        return pointsRepository.findOneWithEagerRelationships(id).map(pointsMapper::toDto);
    }

    /**
     * Delete the points by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Points : {}", id);
        pointsRepository.deleteById(id);
        pointsSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the points corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PointsDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Points for query {}", query);
        return pointsSearchRepository.search(query, pageable).map(pointsMapper::toDto);
    }
}
