package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Marca;
import com.mycompany.myapp.service.dto.MarcaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Marca} and its DTO {@link MarcaDTO}.
 */
@Mapper(componentModel = "spring", uses = { CocheMapper.class })
public interface MarcaMapper extends EntityMapper<MarcaDTO, Marca> {
    @Mapping(target = "coche", source = "coche", qualifiedByName = "id")
    MarcaDTO toDto(Marca s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MarcaDTO toDtoId(Marca marca);
}
