package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Comprador;
import com.mycompany.myapp.repository.CompradorRepository;
import com.mycompany.myapp.service.dto.CompradorDTO;
import com.mycompany.myapp.service.mapper.CompradorMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompradorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompradorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_NACIMIENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_NACIMIENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_CARNET = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CARNET = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/compradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private CompradorMapper compradorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompradorMockMvc;

    private Comprador comprador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprador createEntity(EntityManager em) {
        Comprador comprador = new Comprador()
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .dni(DEFAULT_DNI)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO)
            .fechaCarnet(DEFAULT_FECHA_CARNET);
        return comprador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprador createUpdatedEntity(EntityManager em) {
        Comprador comprador = new Comprador()
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .dni(UPDATED_DNI)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaCarnet(UPDATED_FECHA_CARNET);
        return comprador;
    }

    @BeforeEach
    public void initTest() {
        comprador = createEntity(em);
    }

    @Test
    @Transactional
    void createComprador() throws Exception {
        int databaseSizeBeforeCreate = compradorRepository.findAll().size();
        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);
        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compradorDTO)))
            .andExpect(status().isCreated());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeCreate + 1);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testComprador.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testComprador.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testComprador.getFechaCarnet()).isEqualTo(DEFAULT_FECHA_CARNET);
    }

    @Test
    @Transactional
    void createCompradorWithExistingId() throws Exception {
        // Create the Comprador with an existing ID
        comprador.setId(1L);
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        int databaseSizeBeforeCreate = compradorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compradorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompradors() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        // Get all the compradorList
        restCompradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprador.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaCarnet").value(hasItem(DEFAULT_FECHA_CARNET.toString())));
    }

    @Test
    @Transactional
    void getComprador() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        // Get the comprador
        restCompradorMockMvc
            .perform(get(ENTITY_API_URL_ID, comprador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comprador.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.fechaCarnet").value(DEFAULT_FECHA_CARNET.toString()));
    }

    @Test
    @Transactional
    void getNonExistingComprador() throws Exception {
        // Get the comprador
        restCompradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewComprador() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();

        // Update the comprador
        Comprador updatedComprador = compradorRepository.findById(comprador.getId()).get();
        // Disconnect from session so that the updates on updatedComprador are not directly saved in db
        em.detach(updatedComprador);
        updatedComprador
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .dni(UPDATED_DNI)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaCarnet(UPDATED_FECHA_CARNET);
        CompradorDTO compradorDTO = compradorMapper.toDto(updatedComprador);

        restCompradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compradorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComprador.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testComprador.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testComprador.getFechaCarnet()).isEqualTo(UPDATED_FECHA_CARNET);
    }

    @Test
    @Transactional
    void putNonExistingComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compradorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompradorWithPatch() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();

        // Update the comprador using partial update
        Comprador partialUpdatedComprador = new Comprador();
        partialUpdatedComprador.setId(comprador.getId());

        partialUpdatedComprador.apellidos(UPDATED_APELLIDOS).fechaNacimiento(UPDATED_FECHA_NACIMIENTO).fechaCarnet(UPDATED_FECHA_CARNET);

        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComprador))
            )
            .andExpect(status().isOk());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testComprador.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testComprador.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testComprador.getFechaCarnet()).isEqualTo(UPDATED_FECHA_CARNET);
    }

    @Test
    @Transactional
    void fullUpdateCompradorWithPatch() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();

        // Update the comprador using partial update
        Comprador partialUpdatedComprador = new Comprador();
        partialUpdatedComprador.setId(comprador.getId());

        partialUpdatedComprador
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .dni(UPDATED_DNI)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO)
            .fechaCarnet(UPDATED_FECHA_CARNET);

        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprador.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComprador))
            )
            .andExpect(status().isOk());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
        Comprador testComprador = compradorList.get(compradorList.size() - 1);
        assertThat(testComprador.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testComprador.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testComprador.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testComprador.getFechaNacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testComprador.getFechaCarnet()).isEqualTo(UPDATED_FECHA_CARNET);
    }

    @Test
    @Transactional
    void patchNonExistingComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compradorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComprador() throws Exception {
        int databaseSizeBeforeUpdate = compradorRepository.findAll().size();
        comprador.setId(count.incrementAndGet());

        // Create the Comprador
        CompradorDTO compradorDTO = compradorMapper.toDto(comprador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompradorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compradorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comprador in the database
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComprador() throws Exception {
        // Initialize the database
        compradorRepository.saveAndFlush(comprador);

        int databaseSizeBeforeDelete = compradorRepository.findAll().size();

        // Delete the comprador
        restCompradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, comprador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comprador> compradorList = compradorRepository.findAll();
        assertThat(compradorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
