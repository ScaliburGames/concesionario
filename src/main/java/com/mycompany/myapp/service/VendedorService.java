package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Vendedor;
import com.mycompany.myapp.repository.VendedorRepository;
import com.mycompany.myapp.service.dto.VendedorDTO;
import com.mycompany.myapp.service.mapper.VendedorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vendedor}.
 */
@Service
@Transactional
public class VendedorService {

    private final Logger log = LoggerFactory.getLogger(VendedorService.class);

    private final VendedorRepository vendedorRepository;

    private final VendedorMapper vendedorMapper;

    public VendedorService(VendedorRepository vendedorRepository, VendedorMapper vendedorMapper) {
        this.vendedorRepository = vendedorRepository;
        this.vendedorMapper = vendedorMapper;
    }

    /**
     * Save a vendedor.
     *
     * @param vendedorDTO the entity to save.
     * @return the persisted entity.
     */
    public VendedorDTO save(VendedorDTO vendedorDTO) {
        log.debug("Request to save Vendedor : {}", vendedorDTO);
        Vendedor vendedor = vendedorMapper.toEntity(vendedorDTO);
        vendedor = vendedorRepository.save(vendedor);
        return vendedorMapper.toDto(vendedor);
    }

    /**
     * Partially update a vendedor.
     *
     * @param vendedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VendedorDTO> partialUpdate(VendedorDTO vendedorDTO) {
        log.debug("Request to partially update Vendedor : {}", vendedorDTO);

        return vendedorRepository
            .findById(vendedorDTO.getId())
            .map(existingVendedor -> {
                vendedorMapper.partialUpdate(existingVendedor, vendedorDTO);

                return existingVendedor;
            })
            .map(vendedorRepository::save)
            .map(vendedorMapper::toDto);
    }

    /**
     * Get all the vendedors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VendedorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vendedors");
        return vendedorRepository.findAll(pageable).map(vendedorMapper::toDto);
    }

    /**
     * Get one vendedor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VendedorDTO> findOne(Long id) {
        log.debug("Request to get Vendedor : {}", id);
        return vendedorRepository.findById(id).map(vendedorMapper::toDto);
    }

    /**
     * Delete the vendedor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vendedor : {}", id);
        vendedorRepository.deleteById(id);
    }
}
