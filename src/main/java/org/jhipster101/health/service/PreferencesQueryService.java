package org.jhipster101.health.service;

import jakarta.persistence.criteria.JoinType;
import org.jhipster101.health.domain.*; // for static metamodels
import org.jhipster101.health.domain.Preferences;
import org.jhipster101.health.repository.PreferencesRepository;
import org.jhipster101.health.repository.search.PreferencesSearchRepository;
import org.jhipster101.health.service.criteria.PreferencesCriteria;
import org.jhipster101.health.service.dto.PreferencesDTO;
import org.jhipster101.health.service.mapper.PreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Preferences} entities in the database.
 * The main input is a {@link PreferencesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PreferencesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PreferencesQueryService extends QueryService<Preferences> {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesQueryService.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    private final PreferencesSearchRepository preferencesSearchRepository;

    public PreferencesQueryService(
        PreferencesRepository preferencesRepository,
        PreferencesMapper preferencesMapper,
        PreferencesSearchRepository preferencesSearchRepository
    ) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
        this.preferencesSearchRepository = preferencesSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link PreferencesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PreferencesDTO> findByCriteria(PreferencesCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Preferences> specification = createSpecification(criteria);
        return preferencesRepository.findAll(specification, page).map(preferencesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PreferencesCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Preferences> specification = createSpecification(criteria);
        return preferencesRepository.count(specification);
    }

    /**
     * Function to convert {@link PreferencesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Preferences> createSpecification(PreferencesCriteria criteria) {
        Specification<Preferences> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Preferences_.id));
            }
            if (criteria.getWeekelygoal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWeekelygoal(), Preferences_.weekelygoal));
            }
            if (criteria.getWeightunits() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWeightunits(), Preferences_.weightunits));
            }
            if (criteria.getManytooneId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getManytooneId(), root -> root.join(Preferences_.manytoone, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
