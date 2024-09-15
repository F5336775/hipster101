package org.jhipster101.health.service.mapper;

import org.jhipster101.health.domain.User;
import org.jhipster101.health.domain.Weight;
import org.jhipster101.health.service.dto.UserDTO;
import org.jhipster101.health.service.dto.WeightDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Weight} and its DTO {@link WeightDTO}.
 */
@Mapper(componentModel = "spring")
public interface WeightMapper extends EntityMapper<WeightDTO, Weight> {
    @Mapping(target = "manytoone", source = "manytoone", qualifiedByName = "userLogin")
    WeightDTO toDto(Weight s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
