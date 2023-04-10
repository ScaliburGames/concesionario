package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Comprador;
import com.mycompany.myapp.repository.CompradorRepository;
import com.mycompany.myapp.service.dto.CompradorDTO;
import com.mycompany.myapp.service.mapper.CompradorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comprador}.
 */
@Service
@Transactional
public class CompradorService {

    private final Logger log = LoggerFactory.getLogger(CompradorService.class);

    private final CompradorRepository compradorRepository;

    private final CompradorMapper compradorMapper;

    public CompradorService(CompradorRepository compradorRepository, CompradorMapper compradorMapper) {
        this.compradorRepository = compradorRepository;
        this.compradorMapper = compradorMapper;
    }

    /**
     * Save a comprador.
     *
     * @param compradorDTO the entity to save.
     * @return the persisted entity.
     */
    public CompradorDTO save(CompradorDTO compradorDTO) {
        log.debug("Request to save Comprador : {}", compradorDTO);
        Comprador comprador = compradorMapper.toEntity(compradorDTO);
        comprador = compradorRepository.save(comprador);
        return compradorMapper.toDto(comprador);
    }

    /**
     * Partially update a comprador.
     *
     * @param compradorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompradorDTO> partialUpdate(CompradorDTO compradorDTO) {
        log.debug("Request to partially update Comprador : {}", compradorDTO);

        return compradorRepository
            .findById(compradorDTO.getId())
            .map(existingComprador -> {
                compradorMapper.partialUpdate(existingComprador, compradorDTO);

                return existingComprador;
            })
            .map(compradorRepository::save)
            .map(compradorMapper::toDto);
    }

    /**
     * Get all the compradors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompradorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Compradors");
        return compradorRepository.findAll(pageable).map(compradorMapper::toDto);
    }

    /**
     * Get one comprador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompradorDTO> findOne(Long id) {
        log.debug("Request to get Comprador : {}", id);
        return compradorRepository.findById(id).map(compradorMapper::toDto);
    }

    /**
     * Delete the comprador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comprador : {}", id);
        compradorRepository.deleteById(id);
    }
}
