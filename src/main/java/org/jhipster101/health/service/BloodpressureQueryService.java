package org.jhipster101.health.service;

import jakarta.persistence.criteria.JoinType;
import org.jhipster101.health.domain.*; // for static metamodels
import org.jhipster101.health.domain.Bloodpressure;
import org.jhipster101.health.repository.BloodpressureRepository;
import org.jhipster101.health.repository.search.BloodpressureSearchRepository;
import org.jhipster101.health.service.criteria.BloodpressureCriteria;
import org.jhipster101.health.service.dto.BloodpressureDTO;
import org.jhipster101.health.service.mapper.BloodpressureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Bloodpressure} entities in the database.
 * The main input is a {@link BloodpressureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BloodpressureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BloodpressureQueryService extends QueryService<Bloodpressure> {

    private static final Logger LOG = LoggerFactory.getLogger(BloodpressureQueryService.class);

    private final BloodpressureRepository bloodpressureRepository;

    private final BloodpressureMapper bloodpressureMapper;

    private final BloodpressureSearchRepository bloodpressureSearchRepository;

    public BloodpressureQueryService(
        BloodpressureRepository bloodpressureRepository,
        BloodpressureMapper bloodpressureMapper,
        BloodpressureSearchRepository bloodpressureSearchRepository
    ) {
        this.bloodpressureRepository = bloodpressureRepository;
        this.bloodpressureMapper = bloodpressureMapper;
        this.bloodpressureSearchRepository = bloodpressureSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link BloodpressureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BloodpressureDTO> findByCriteria(BloodpressureCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bloodpressure> specification = createSpecification(criteria);
        return bloodpressureRepository.findAll(specification, page).map(bloodpressureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BloodpressureCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Bloodpressure> specification = createSpecification(criteria);
        return bloodpressureRepository.count(specification);
    }

    /**
     * Function to convert {@link BloodpressureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bloodpressure> createSpecification(BloodpressureCriteria criteria) {
        Specification<Bloodpressure> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Bloodpressure_.id));
            }
            if (criteria.getDatetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatetime(), Bloodpressure_.datetime));
            }
            if (criteria.getSystolic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSystolic(), Bloodpressure_.systolic));
            }
            if (criteria.getDiastolic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiastolic(), Bloodpressure_.diastolic));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(Bloodpressure_.user, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
