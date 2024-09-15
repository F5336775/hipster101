package org.jhipster101.health.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PreferencesCriteriaTest {

    @Test
    void newPreferencesCriteriaHasAllFiltersNullTest() {
        var preferencesCriteria = new PreferencesCriteria();
        assertThat(preferencesCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void preferencesCriteriaFluentMethodsCreatesFiltersTest() {
        var preferencesCriteria = new PreferencesCriteria();

        setAllFilters(preferencesCriteria);

        assertThat(preferencesCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void preferencesCriteriaCopyCreatesNullFilterTest() {
        var preferencesCriteria = new PreferencesCriteria();
        var copy = preferencesCriteria.copy();

        assertThat(preferencesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(preferencesCriteria)
        );
    }

    @Test
    void preferencesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var preferencesCriteria = new PreferencesCriteria();
        setAllFilters(preferencesCriteria);

        var copy = preferencesCriteria.copy();

        assertThat(preferencesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(preferencesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var preferencesCriteria = new PreferencesCriteria();

        assertThat(preferencesCriteria).hasToString("PreferencesCriteria{}");
    }

    private static void setAllFilters(PreferencesCriteria preferencesCriteria) {
        preferencesCriteria.id();
        preferencesCriteria.weekelygoal();
        preferencesCriteria.weightunits();
        preferencesCriteria.manytooneId();
        preferencesCriteria.distinct();
    }

    private static Condition<PreferencesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getWeekelygoal()) &&
                condition.apply(criteria.getWeightunits()) &&
                condition.apply(criteria.getManytooneId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PreferencesCriteria> copyFiltersAre(PreferencesCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getWeekelygoal(), copy.getWeekelygoal()) &&
                condition.apply(criteria.getWeightunits(), copy.getWeightunits()) &&
                condition.apply(criteria.getManytooneId(), copy.getManytooneId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
