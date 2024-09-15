package org.jhipster101.health.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BloodpressureCriteriaTest {

    @Test
    void newBloodpressureCriteriaHasAllFiltersNullTest() {
        var bloodpressureCriteria = new BloodpressureCriteria();
        assertThat(bloodpressureCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void bloodpressureCriteriaFluentMethodsCreatesFiltersTest() {
        var bloodpressureCriteria = new BloodpressureCriteria();

        setAllFilters(bloodpressureCriteria);

        assertThat(bloodpressureCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void bloodpressureCriteriaCopyCreatesNullFilterTest() {
        var bloodpressureCriteria = new BloodpressureCriteria();
        var copy = bloodpressureCriteria.copy();

        assertThat(bloodpressureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(bloodpressureCriteria)
        );
    }

    @Test
    void bloodpressureCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bloodpressureCriteria = new BloodpressureCriteria();
        setAllFilters(bloodpressureCriteria);

        var copy = bloodpressureCriteria.copy();

        assertThat(bloodpressureCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(bloodpressureCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bloodpressureCriteria = new BloodpressureCriteria();

        assertThat(bloodpressureCriteria).hasToString("BloodpressureCriteria{}");
    }

    private static void setAllFilters(BloodpressureCriteria bloodpressureCriteria) {
        bloodpressureCriteria.id();
        bloodpressureCriteria.datetime();
        bloodpressureCriteria.systolic();
        bloodpressureCriteria.diastolic();
        bloodpressureCriteria.userId();
        bloodpressureCriteria.distinct();
    }

    private static Condition<BloodpressureCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDatetime()) &&
                condition.apply(criteria.getSystolic()) &&
                condition.apply(criteria.getDiastolic()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BloodpressureCriteria> copyFiltersAre(
        BloodpressureCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDatetime(), copy.getDatetime()) &&
                condition.apply(criteria.getSystolic(), copy.getSystolic()) &&
                condition.apply(criteria.getDiastolic(), copy.getDiastolic()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
