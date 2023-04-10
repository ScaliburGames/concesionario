package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CompradorRepository;
import com.mycompany.myapp.service.CompradorService;
import com.mycompany.myapp.service.dto.CompradorDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Comprador}.
 */
@RestController
@RequestMapping("/api")
public class CompradorResource {

    private final Logger log = LoggerFactory.getLogger(CompradorResource.class);

    private static final String ENTITY_NAME = "comprador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompradorService compradorService;

    private final CompradorRepository compradorRepository;

    public CompradorResource(CompradorService compradorService, CompradorRepository compradorRepository) {
        this.compradorService = compradorService;
        this.compradorRepository = compradorRepository;
    }

    /**
     * {@code POST  /compradors} : Create a new comprador.
     *
     * @param compradorDTO the compradorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compradorDTO, or with status {@code 400 (Bad Request)} if the comprador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compradors")
    public ResponseEntity<CompradorDTO> createComprador(@RequestBody CompradorDTO compradorDTO) throws URISyntaxException {
        log.debug("REST request to save Comprador : {}", compradorDTO);
        if (compradorDTO.getId() != null) {
            throw new BadRequestAlertException("A new comprador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompradorDTO result = compradorService.save(compradorDTO);
        return ResponseEntity
            .created(new URI("/api/compradors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compradors/:id} : Updates an existing comprador.
     *
     * @param id the id of the compradorDTO to save.
     * @param compradorDTO the compradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compradorDTO,
     * or with status {@code 400 (Bad Request)} if the compradorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compradors/{id}")
    public ResponseEntity<CompradorDTO> updateComprador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompradorDTO compradorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Comprador : {}, {}", id, compradorDTO);
        if (compradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompradorDTO result = compradorService.save(compradorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compradorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compradors/:id} : Partial updates given fields of an existing comprador, field will ignore if it is null
     *
     * @param id the id of the compradorDTO to save.
     * @param compradorDTO the compradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compradorDTO,
     * or with status {@code 400 (Bad Request)} if the compradorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compradorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compradors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompradorDTO> partialUpdateComprador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompradorDTO compradorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Comprador partially : {}, {}", id, compradorDTO);
        if (compradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompradorDTO> result = compradorService.partialUpdate(compradorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compradorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compradors} : get all the compradors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compradors in body.
     */
    @GetMapping("/compradors")
    public ResponseEntity<List<CompradorDTO>> getAllCompradors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Compradors");
        Page<CompradorDTO> page = compradorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compradors/:id} : get the "id" comprador.
     *
     * @param id the id of the compradorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compradorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compradors/{id}")
    public ResponseEntity<CompradorDTO> getComprador(@PathVariable Long id) {
        log.debug("REST request to get Comprador : {}", id);
        Optional<CompradorDTO> compradorDTO = compradorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compradorDTO);
    }

    /**
     * {@code DELETE  /compradors/:id} : delete the "id" comprador.
     *
     * @param id the id of the compradorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compradors/{id}")
    public ResponseEntity<Void> deleteComprador(@PathVariable Long id) {
        log.debug("REST request to delete Comprador : {}", id);
        compradorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
