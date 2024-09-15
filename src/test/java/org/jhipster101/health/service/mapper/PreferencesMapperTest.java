package org.jhipster101.health.service.mapper;

import static org.jhipster101.health.domain.PreferencesAsserts.*;
import static org.jhipster101.health.domain.PreferencesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PreferencesMapperTest {

    private PreferencesMapper preferencesMapper;

    @BeforeEach
    void setUp() {
        preferencesMapper = new PreferencesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPreferencesSample1();
        var actual = preferencesMapper.toEntity(preferencesMapper.toDto(expected));
        assertPreferencesAllPropertiesEquals(expected, actual);
    }
}
