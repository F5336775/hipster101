package org.jhipster101.health.service.mapper;

import static org.jhipster101.health.domain.BloodpressureAsserts.*;
import static org.jhipster101.health.domain.BloodpressureTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BloodpressureMapperTest {

    private BloodpressureMapper bloodpressureMapper;

    @BeforeEach
    void setUp() {
        bloodpressureMapper = new BloodpressureMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBloodpressureSample1();
        var actual = bloodpressureMapper.toEntity(bloodpressureMapper.toDto(expected));
        assertBloodpressureAllPropertiesEquals(expected, actual);
    }
}
