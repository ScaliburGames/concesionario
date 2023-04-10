package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Modelo;
import com.mycompany.myapp.service.dto.ModeloDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Modelo} and its DTO {@link ModeloDTO}.
 */
@Mapper(componentModel = "spring", uses = { MarcaMapper.class })
public interface ModeloMapper extends EntityMapper<ModeloDTO, Modelo> {
    @Mapping(target = "marca", source = "marca", qualifiedByName = "id")
    ModeloDTO toDto(Modelo s);
}
