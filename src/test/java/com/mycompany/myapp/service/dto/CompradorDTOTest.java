package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompradorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompradorDTO.class);
        CompradorDTO compradorDTO1 = new CompradorDTO();
        compradorDTO1.setId(1L);
        CompradorDTO compradorDTO2 = new CompradorDTO();
        assertThat(compradorDTO1).isNotEqualTo(compradorDTO2);
        compradorDTO2.setId(compradorDTO1.getId());
        assertThat(compradorDTO1).isEqualTo(compradorDTO2);
        compradorDTO2.setId(2L);
        assertThat(compradorDTO1).isNotEqualTo(compradorDTO2);
        compradorDTO1.setId(null);
        assertThat(compradorDTO1).isNotEqualTo(compradorDTO2);
    }
}
