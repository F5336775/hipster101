package org.jhipster101.health.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster101.health.repository.BloodpressureRepository;
import org.jhipster101.health.service.BloodpressureQueryService;
import org.jhipster101.health.service.BloodpressureService;
import org.jhipster101.health.service.criteria.BloodpressureCriteria;
import org.jhipster101.health.service.dto.BloodpressureDTO;
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
 * REST controller for managing {@link org.jhipster101.health.domain.Bloodpressure}.
 */
@RestController
@RequestMapping("/api/bloodpressures")
public class BloodpressureResource {

    private static final Logger LOG = LoggerFactory.getLogger(BloodpressureResource.class);

    private static final String ENTITY_NAME = "bloodpressure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BloodpressureService bloodpressureService;

    private final BloodpressureRepository bloodpressureRepository;

    private final BloodpressureQueryService bloodpressureQueryService;

    public BloodpressureResource(
        BloodpressureService bloodpressureService,
        BloodpressureRepository bloodpressureRepository,
        BloodpressureQueryService bloodpressureQueryService
    ) {
        this.bloodpressureService = bloodpressureService;
        this.bloodpressureRepository = bloodpressureRepository;
        this.bloodpressureQueryService = bloodpressureQueryService;
    }

    /**
     * {@code POST  /bloodpressures} : Create a new bloodpressure.
     *
     * @param bloodpressureDTO the bloodpressureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bloodpressureDTO, or with status {@code 400 (Bad Request)} if the bloodpressure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BloodpressureDTO> createBloodpressure(@Valid @RequestBody BloodpressureDTO bloodpressureDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Bloodpressure : {}", bloodpressureDTO);
        if (bloodpressureDTO.getId() != null) {
            throw new BadRequestAlertException("A new bloodpressure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bloodpressureDTO = bloodpressureService.save(bloodpressureDTO);
        return ResponseEntity.created(new URI("/api/bloodpressures/" + bloodpressureDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bloodpressureDTO.getId().toString()))
            .body(bloodpressureDTO);
    }

    /**
     * {@code PUT  /bloodpressures/:id} : Updates an existing bloodpressure.
     *
     * @param id the id of the bloodpressureDTO to save.
     * @param bloodpressureDTO the bloodpressureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bloodpressureDTO,
     * or with status {@code 400 (Bad Request)} if the bloodpressureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bloodpressureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BloodpressureDTO> updateBloodpressure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BloodpressureDTO bloodpressureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bloodpressure : {}, {}", id, bloodpressureDTO);
        if (bloodpressureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bloodpressureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bloodpressureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bloodpressureDTO = bloodpressureService.update(bloodpressureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bloodpressureDTO.getId().toString()))
            .body(bloodpressureDTO);
    }

    /**
     * {@code PATCH  /bloodpressures/:id} : Partial updates given fields of an existing bloodpressure, field will ignore if it is null
     *
     * @param id the id of the bloodpressureDTO to save.
     * @param bloodpressureDTO the bloodpressureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bloodpressureDTO,
     * or with status {@code 400 (Bad Request)} if the bloodpressureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bloodpressureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bloodpressureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BloodpressureDTO> partialUpdateBloodpressure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BloodpressureDTO bloodpressureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bloodpressure partially : {}, {}", id, bloodpressureDTO);
        if (bloodpressureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bloodpressureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bloodpressureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BloodpressureDTO> result = bloodpressureService.partialUpdate(bloodpressureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bloodpressureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bloodpressures} : get all the bloodpressures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bloodpressures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BloodpressureDTO>> getAllBloodpressures(
        BloodpressureCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Bloodpressures by criteria: {}", criteria);

        Page<BloodpressureDTO> page = bloodpressureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bloodpressures/count} : count all the bloodpressures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBloodpressures(BloodpressureCriteria criteria) {
        LOG.debug("REST request to count Bloodpressures by criteria: {}", criteria);
        return ResponseEntity.ok().body(bloodpressureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bloodpressures/:id} : get the "id" bloodpressure.
     *
     * @param id the id of the bloodpressureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bloodpressureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BloodpressureDTO> getBloodpressure(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bloodpressure : {}", id);
        Optional<BloodpressureDTO> bloodpressureDTO = bloodpressureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bloodpressureDTO);
    }

    /**
     * {@code DELETE  /bloodpressures/:id} : delete the "id" bloodpressure.
     *
     * @param id the id of the bloodpressureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBloodpressure(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bloodpressure : {}", id);
        bloodpressureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /bloodpressures/_search?query=:query} : search for the bloodpressure corresponding
     * to the query.
     *
     * @param query the query of the bloodpressure search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<BloodpressureDTO>> searchBloodpressures(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Bloodpressures for query {}", query);
        try {
            Page<BloodpressureDTO> page = bloodpressureService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
