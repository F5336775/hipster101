package org.jhipster101.health.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jhipster101.health.domain.AssertUtils.zonedDataTimeSameInstant;

public class BloodpressureAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBloodpressureAllPropertiesEquals(Bloodpressure expected, Bloodpressure actual) {
        assertBloodpressureAutoGeneratedPropertiesEquals(expected, actual);
        assertBloodpressureAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBloodpressureAllUpdatablePropertiesEquals(Bloodpressure expected, Bloodpressure actual) {
        assertBloodpressureUpdatableFieldsEquals(expected, actual);
        assertBloodpressureUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBloodpressureAutoGeneratedPropertiesEquals(Bloodpressure expected, Bloodpressure actual) {
        assertThat(expected)
            .as("Verify Bloodpressure auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBloodpressureUpdatableFieldsEquals(Bloodpressure expected, Bloodpressure actual) {
        assertThat(expected)
            .as("Verify Bloodpressure relevant properties")
            .satisfies(e ->
                assertThat(e.getDatetime()).as("check datetime").usingComparator(zonedDataTimeSameInstant).isEqualTo(actual.getDatetime())
            )
            .satisfies(e -> assertThat(e.getSystolic()).as("check systolic").isEqualTo(actual.getSystolic()))
            .satisfies(e -> assertThat(e.getDiastolic()).as("check diastolic").isEqualTo(actual.getDiastolic()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBloodpressureUpdatableRelationshipsEquals(Bloodpressure expected, Bloodpressure actual) {
        // empty method
    }
}
