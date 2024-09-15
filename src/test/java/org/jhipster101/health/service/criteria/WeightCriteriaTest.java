package org.jhipster101.health.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WeightCriteriaTest {

    @Test
    void newWeightCriteriaHasAllFiltersNullTest() {
        var weightCriteria = new WeightCriteria();
        assertThat(weightCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void weightCriteriaFluentMethodsCreatesFiltersTest() {
        var weightCriteria = new WeightCriteria();

        setAllFilters(weightCriteria);

        assertThat(weightCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void weightCriteriaCopyCreatesNullFilterTest() {
        var weightCriteria = new WeightCriteria();
        var copy = weightCriteria.copy();

        assertThat(weightCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(weightCriteria)
        );
    }

    @Test
    void weightCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var weightCriteria = new WeightCriteria();
        setAllFilters(weightCriteria);

        var copy = weightCriteria.copy();

        assertThat(weightCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(weightCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var weightCriteria = new WeightCriteria();

        assertThat(weightCriteria).hasToString("WeightCriteria{}");
    }

    private static void setAllFilters(WeightCriteria weightCriteria) {
        weightCriteria.id();
        weightCriteria.datetime();
        weightCriteria.weight();
        weightCriteria.manytooneId();
        weightCriteria.distinct();
    }

    private static Condition<WeightCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDatetime()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getManytooneId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WeightCriteria> copyFiltersAre(WeightCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDatetime(), copy.getDatetime()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getManytooneId(), copy.getManytooneId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
