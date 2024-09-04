package org.jhipster101.health.service.mapper;

import org.jhipster101.health.domain.Points;
import org.jhipster101.health.domain.User;
import org.jhipster101.health.service.dto.PointsDTO;
import org.jhipster101.health.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Points} and its DTO {@link PointsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PointsMapper extends EntityMapper<PointsDTO, Points> {
    @Mapping(target = "manytoone", source = "manytoone", qualifiedByName = "userLogin")
    PointsDTO toDto(Points s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
