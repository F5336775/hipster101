package org.jhipster101.health.service.mapper;

import static org.jhipster101.health.domain.PointsAsserts.*;
import static org.jhipster101.health.domain.PointsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PointsMapperTest {

    private PointsMapper pointsMapper;

    @BeforeEach
    void setUp() {
        pointsMapper = new PointsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPointsSample1();
        var actual = pointsMapper.toEntity(pointsMapper.toDto(expected));
        assertPointsAllPropertiesEquals(expected, actual);
    }
}
