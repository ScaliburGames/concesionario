package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Vendedor;
import com.mycompany.myapp.repository.VendedorRepository;
import com.mycompany.myapp.service.dto.VendedorDTO;
import com.mycompany.myapp.service.mapper.VendedorMapper;
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
 * Integration tests for the {@link VendedorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_EMPLEADO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_EMPLEADO = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vendedors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private VendedorMapper vendedorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendedorMockMvc;

    private Vendedor vendedor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .codigoEmpleado(DEFAULT_CODIGO_EMPLEADO)
            .dni(DEFAULT_DNI);
        return vendedor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createUpdatedEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .codigoEmpleado(UPDATED_CODIGO_EMPLEADO)
            .dni(UPDATED_DNI);
        return vendedor;
    }

    @BeforeEach
    public void initTest() {
        vendedor = createEntity(em);
    }

    @Test
    @Transactional
    void createVendedor() throws Exception {
        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();
        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedorDTO)))
            .andExpect(status().isCreated());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testVendedor.getCodigoEmpleado()).isEqualTo(DEFAULT_CODIGO_EMPLEADO);
        assertThat(testVendedor.getDni()).isEqualTo(DEFAULT_DNI);
    }

    @Test
    @Transactional
    void createVendedorWithExistingId() throws Exception {
        // Create the Vendedor with an existing ID
        vendedor.setId(1L);
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendedorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVendedors() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].codigoEmpleado").value(hasItem(DEFAULT_CODIGO_EMPLEADO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)));
    }

    @Test
    @Transactional
    void getVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get the vendedor
        restVendedorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.codigoEmpleado").value(DEFAULT_CODIGO_EMPLEADO))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI));
    }

    @Test
    @Transactional
    void getNonExistingVendedor() throws Exception {
        // Get the vendedor
        restVendedorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor
        Vendedor updatedVendedor = vendedorRepository.findById(vendedor.getId()).get();
        // Disconnect from session so that the updates on updatedVendedor are not directly saved in db
        em.detach(updatedVendedor);
        updatedVendedor.nombre(UPDATED_NOMBRE).apellidos(UPDATED_APELLIDOS).codigoEmpleado(UPDATED_CODIGO_EMPLEADO).dni(UPDATED_DNI);
        VendedorDTO vendedorDTO = vendedorMapper.toDto(updatedVendedor);

        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testVendedor.getCodigoEmpleado()).isEqualTo(UPDATED_CODIGO_EMPLEADO);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
    }

    @Test
    @Transactional
    void putNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendedorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendedorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.nombre(UPDATED_NOMBRE);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testVendedor.getCodigoEmpleado()).isEqualTo(DEFAULT_CODIGO_EMPLEADO);
        assertThat(testVendedor.getDni()).isEqualTo(DEFAULT_DNI);
    }

    @Test
    @Transactional
    void fullUpdateVendedorWithPatch() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor using partial update
        Vendedor partialUpdatedVendedor = new Vendedor();
        partialUpdatedVendedor.setId(vendedor.getId());

        partialUpdatedVendedor.nombre(UPDATED_NOMBRE).apellidos(UPDATED_APELLIDOS).codigoEmpleado(UPDATED_CODIGO_EMPLEADO).dni(UPDATED_DNI);

        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendedor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendedor))
            )
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testVendedor.getCodigoEmpleado()).isEqualTo(UPDATED_CODIGO_EMPLEADO);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
    }

    @Test
    @Transactional
    void patchNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendedorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendedorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();
        vendedor.setId(count.incrementAndGet());

        // Create the Vendedor
        VendedorDTO vendedorDTO = vendedorMapper.toDto(vendedor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendedorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendedorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        int databaseSizeBeforeDelete = vendedorRepository.findAll().size();

        // Delete the vendedor
        restVendedorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendedor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
