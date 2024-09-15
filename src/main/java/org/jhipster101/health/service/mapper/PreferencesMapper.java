package org.jhipster101.health.service.mapper;

import org.jhipster101.health.domain.Preferences;
import org.jhipster101.health.domain.User;
import org.jhipster101.health.service.dto.PreferencesDTO;
import org.jhipster101.health.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Preferences} and its DTO {@link PreferencesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PreferencesMapper extends EntityMapper<PreferencesDTO, Preferences> {
    @Mapping(target = "manytoone", source = "manytoone", qualifiedByName = "userLogin")
    PreferencesDTO toDto(Preferences s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
