package org.jhipster101.health.service;

import jakarta.persistence.criteria.JoinType;
import org.jhipster101.health.domain.*; // for static metamodels
import org.jhipster101.health.domain.Weight;
import org.jhipster101.health.repository.WeightRepository;
import org.jhipster101.health.repository.search.WeightSearchRepository;
import org.jhipster101.health.service.criteria.WeightCriteria;
import org.jhipster101.health.service.dto.WeightDTO;
import org.jhipster101.health.service.mapper.WeightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Weight} entities in the database.
 * The main input is a {@link WeightCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WeightDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeightQueryService extends QueryService<Weight> {

    private static final Logger LOG = LoggerFactory.getLogger(WeightQueryService.class);

    private final WeightRepository weightRepository;

    private final WeightMapper weightMapper;

    private final WeightSearchRepository weightSearchRepository;

    public WeightQueryService(WeightRepository weightRepository, WeightMapper weightMapper, WeightSearchRepository weightSearchRepository) {
        this.weightRepository = weightRepository;
        this.weightMapper = weightMapper;
        this.weightSearchRepository = weightSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link WeightDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeightDTO> findByCriteria(WeightCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Weight> specification = createSpecification(criteria);
        return weightRepository.findAll(specification, page).map(weightMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WeightCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Weight> specification = createSpecification(criteria);
        return weightRepository.count(specification);
    }

    /**
     * Function to convert {@link WeightCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Weight> createSpecification(WeightCriteria criteria) {
        Specification<Weight> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Weight_.id));
            }
            if (criteria.getDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatetime(), Weight_.datetime));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Weight_.weight));
            }
            if (criteria.getManytooneId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getManytooneId(), root -> root.join(Weight_.manytoone, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
