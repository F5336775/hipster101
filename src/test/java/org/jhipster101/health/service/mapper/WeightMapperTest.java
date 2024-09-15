package org.jhipster101.health.service.mapper;

import static org.jhipster101.health.domain.WeightAsserts.*;
import static org.jhipster101.health.domain.WeightTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeightMapperTest {

    private WeightMapper weightMapper;

    @BeforeEach
    void setUp() {
        weightMapper = new WeightMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWeightSample1();
        var actual = weightMapper.toEntity(weightMapper.toDto(expected));
        assertWeightAllPropertiesEquals(expected, actual);
    }
}
