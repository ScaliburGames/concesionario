package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Vendedor;
import com.mycompany.myapp.service.dto.VendedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vendedor} and its DTO {@link VendedorDTO}.
 */
@Mapper(componentModel = "spring", uses = { VentaMapper.class })
public interface VendedorMapper extends EntityMapper<VendedorDTO, Vendedor> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "id")
    VendedorDTO toDto(Vendedor s);
}
