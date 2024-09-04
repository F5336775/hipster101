package org.jhipster101.health.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jhipster101.health.repository.PointsRepository;
import org.jhipster101.health.service.PointsService;
import org.jhipster101.health.service.dto.PointsDTO;
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
 * REST controller for managing {@link org.jhipster101.health.domain.Points}.
 */
@RestController
@RequestMapping("/api/points")
public class PointsResource {

    private static final Logger LOG = LoggerFactory.getLogger(PointsResource.class);

    private static final String ENTITY_NAME = "points";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointsService pointsService;

    private final PointsRepository pointsRepository;

    public PointsResource(PointsService pointsService, PointsRepository pointsRepository) {
        this.pointsService = pointsService;
        this.pointsRepository = pointsRepository;
    }

    /**
     * {@code POST  /points} : Create a new points.
     *
     * @param pointsDTO the pointsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointsDTO, or with status {@code 400 (Bad Request)} if the points has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PointsDTO> createPoints(@Valid @RequestBody PointsDTO pointsDTO) throws URISyntaxException {
        LOG.debug("REST request to save Points : {}", pointsDTO);
        if (pointsDTO.getId() != null) {
            throw new BadRequestAlertException("A new points cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pointsDTO = pointsService.save(pointsDTO);
        return ResponseEntity.created(new URI("/api/points/" + pointsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pointsDTO.getId().toString()))
            .body(pointsDTO);
    }

    /**
     * {@code PUT  /points/:id} : Updates an existing points.
     *
     * @param id the id of the pointsDTO to save.
     * @param pointsDTO the pointsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointsDTO,
     * or with status {@code 400 (Bad Request)} if the pointsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PointsDTO> updatePoints(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PointsDTO pointsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Points : {}, {}", id, pointsDTO);
        if (pointsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pointsDTO = pointsService.update(pointsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointsDTO.getId().toString()))
            .body(pointsDTO);
    }

    /**
     * {@code PATCH  /points/:id} : Partial updates given fields of an existing points, field will ignore if it is null
     *
     * @param id the id of the pointsDTO to save.
     * @param pointsDTO the pointsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointsDTO,
     * or with status {@code 400 (Bad Request)} if the pointsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pointsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pointsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PointsDTO> partialUpdatePoints(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PointsDTO pointsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Points partially : {}, {}", id, pointsDTO);
        if (pointsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pointsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pointsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PointsDTO> result = pointsService.partialUpdate(pointsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /points} : get all the points.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of points in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PointsDTO>> getAllPoints(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Points");
        Page<PointsDTO> page;
        if (eagerload) {
            page = pointsService.findAllWithEagerRelationships(pageable);
        } else {
            page = pointsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /points/:id} : get the "id" points.
     *
     * @param id the id of the pointsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PointsDTO> getPoints(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Points : {}", id);
        Optional<PointsDTO> pointsDTO = pointsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointsDTO);
    }

    /**
     * {@code DELETE  /points/:id} : delete the "id" points.
     *
     * @param id the id of the pointsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoints(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Points : {}", id);
        pointsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /points/_search?query=:query} : search for the points corresponding
     * to the query.
     *
     * @param query the query of the points search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<PointsDTO>> searchPoints(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Points for query {}", query);
        try {
            Page<PointsDTO> page = pointsService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
