package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompradorMapperTest {

    private CompradorMapper compradorMapper;

    @BeforeEach
    public void setUp() {
        compradorMapper = new CompradorMapperImpl();
    }
}
