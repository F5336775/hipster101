package org.jhipster101.health.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster101.health.repository.PreferencesRepository;
import org.jhipster101.health.service.PreferencesQueryService;
import org.jhipster101.health.service.PreferencesService;
import org.jhipster101.health.service.criteria.PreferencesCriteria;
import org.jhipster101.health.service.dto.PreferencesDTO;
import org.jhipster101.health.web.rest.errors.BadRequestAlertException;
import org.jhipster101.health.web.rest.errors.ElasticsearchExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.jhipster101.health.domain.Preferences}.
 */
@RestController
@RequestMapping("/api/preferences")
public class PreferencesResource {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesResource.class);

    private static final String ENTITY_NAME = "preferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreferencesService preferencesService;

    private final PreferencesRepository preferencesRepository;

    private final PreferencesQueryService preferencesQueryService;

    public PreferencesResource(
        PreferencesService preferencesService,
        PreferencesRepository preferencesRepository,
        PreferencesQueryService preferencesQueryService
    ) {
        this.preferencesService = preferencesService;
        this.preferencesRepository = preferencesRepository;
        this.preferencesQueryService = preferencesQueryService;
    }

    /**
     * {@code POST  /preferences} : Create a new preferences.
     *
     * @param preferencesDTO the preferencesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new preferencesDTO, or with status {@code 400 (Bad Request)} if the preferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PreferencesDTO> createPreferences(@RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        LOG.debug("REST request to save Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new preferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        preferencesDTO = preferencesService.save(preferencesDTO);
        return ResponseEntity.created(new URI("/api/preferences/" + preferencesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, preferencesDTO.getId().toString()))
            .body(preferencesDTO);
    }

    /**
     * {@code PUT  /preferences/:id} : Updates an existing preferences.
     *
     * @param id the id of the preferencesDTO to save.
     * @param preferencesDTO the preferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preferencesDTO,
     * or with status {@code 400 (Bad Request)} if the preferencesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the preferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PreferencesDTO> updatePreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PreferencesDTO preferencesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Preferences : {}, {}", id, preferencesDTO);
        if (preferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, preferencesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        preferencesDTO = preferencesService.update(preferencesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, preferencesDTO.getId().toString()))
            .body(preferencesDTO);
    }

    /**
     * {@code PATCH  /preferences/:id} : Partial updates given fields of an existing preferences, field will ignore if it is null
     *
     * @param id the id of the preferencesDTO to save.
     * @param preferencesDTO the preferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preferencesDTO,
     * or with status {@code 400 (Bad Request)} if the preferencesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the preferencesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the preferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PreferencesDTO> partialUpdatePreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PreferencesDTO preferencesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Preferences partially : {}, {}", id, preferencesDTO);
        if (preferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, preferencesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preferencesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PreferencesDTO> result = preferencesService.partialUpdate(preferencesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, preferencesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /preferences} : get all the preferences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of preferences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PreferencesDTO>> getAllPreferences(
        PreferencesCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Preferences by criteria: {}", criteria);

        Page<PreferencesDTO> page = preferencesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /preferences/count} : count all the preferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPreferences(PreferencesCriteria criteria) {
        LOG.debug("REST request to count Preferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(preferencesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /preferences/:id} : get the "id" preferences.
     *
     * @param id the id of the preferencesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreferencesDTO> getPreferences(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Preferences : {}", id);
        Optional<PreferencesDTO> preferencesDTO = preferencesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(preferencesDTO);
    }

    /**
     * {@code DELETE  /preferences/:id} : delete the "id" preferences.
     *
     * @param id the id of the preferencesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreferences(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Preferences : {}", id);
        preferencesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /preferences/_search?query=:query} : search for the preferences corresponding
     * to the query.
     *
     * @param query the query of the preferences search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<PreferencesDTO>> searchPreferences(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Preferences for query {}", query);
        try {
            Page<PreferencesDTO> page = preferencesService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
