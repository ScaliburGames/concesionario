package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Comprador;
import com.mycompany.myapp.service.dto.CompradorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comprador} and its DTO {@link CompradorDTO}.
 */
@Mapper(componentModel = "spring", uses = { VentaMapper.class })
public interface CompradorMapper extends EntityMapper<CompradorDTO, Comprador> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "id")
    CompradorDTO toDto(Comprador s);
}
