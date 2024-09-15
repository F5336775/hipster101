package org.jhipster101.health.service.mapper;

import org.jhipster101.health.domain.Bloodpressure;
import org.jhipster101.health.domain.User;
import org.jhipster101.health.service.dto.BloodpressureDTO;
import org.jhipster101.health.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bloodpressure} and its DTO {@link BloodpressureDTO}.
 */
@Mapper(componentModel = "spring")
public interface BloodpressureMapper extends EntityMapper<BloodpressureDTO, Bloodpressure> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    BloodpressureDTO toDto(Bloodpressure s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
