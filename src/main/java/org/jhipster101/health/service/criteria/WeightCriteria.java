package org.jhipster101.health.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.jhipster101.health.domain.Weight} entity. This class is used
 * in {@link org.jhipster101.health.web.rest.WeightResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /weights?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeightCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter datetime;

    private IntegerFilter weight;

    private LongFilter manytooneId;

    private Boolean distinct;

    public WeightCriteria() {}

    public WeightCriteria(WeightCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.datetime = other.optionalDatetime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.weight = other.optionalWeight().map(IntegerFilter::copy).orElse(null);
        this.manytooneId = other.optionalManytooneId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WeightCriteria copy() {
        return new WeightCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getDatetime() {
        return datetime;
    }

    public Optional<ZonedDateTimeFilter> optionalDatetime() {
        return Optional.ofNullable(datetime);
    }

    public ZonedDateTimeFilter datetime() {
        if (datetime == null) {
            setDatetime(new ZonedDateTimeFilter());
        }
        return datetime;
    }

    public void setDatetime(ZonedDateTimeFilter datetime) {
        this.datetime = datetime;
    }

    public IntegerFilter getWeight() {
        return weight;
    }

    public Optional<IntegerFilter> optionalWeight() {
        return Optional.ofNullable(weight);
    }

    public IntegerFilter weight() {
        if (weight == null) {
            setWeight(new IntegerFilter());
        }
        return weight;
    }

    public void setWeight(IntegerFilter weight) {
        this.weight = weight;
    }

    public LongFilter getManytooneId() {
        return manytooneId;
    }

    public Optional<LongFilter> optionalManytooneId() {
        return Optional.ofNullable(manytooneId);
    }

    public LongFilter manytooneId() {
        if (manytooneId == null) {
            setManytooneId(new LongFilter());
        }
        return manytooneId;
    }

    public void setManytooneId(LongFilter manytooneId) {
        this.manytooneId = manytooneId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WeightCriteria that = (WeightCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(datetime, that.datetime) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(manytooneId, that.manytooneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datetime, weight, manytooneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeightCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDatetime().map(f -> "datetime=" + f + ", ").orElse("") +
            optionalWeight().map(f -> "weight=" + f + ", ").orElse("") +
            optionalManytooneId().map(f -> "manytooneId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
