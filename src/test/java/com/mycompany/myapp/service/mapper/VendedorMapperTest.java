package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VendedorMapperTest {

    private VendedorMapper vendedorMapper;

    @BeforeEach
    public void setUp() {
        vendedorMapper = new VendedorMapperImpl();
    }
}
