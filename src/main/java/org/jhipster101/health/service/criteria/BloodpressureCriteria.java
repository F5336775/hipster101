package org.jhipster101.health.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.jhipster101.health.domain.Bloodpressure} entity. This class is used
 * in {@link org.jhipster101.health.web.rest.BloodpressureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bloodpressures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BloodpressureCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter datetime;

    private IntegerFilter systolic;

    private IntegerFilter diastolic;

    private LongFilter userId;

    private Boolean distinct;

    public BloodpressureCriteria() {}

    public BloodpressureCriteria(BloodpressureCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.datetime = other.optionalDatetime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.systolic = other.optionalSystolic().map(IntegerFilter::copy).orElse(null);
        this.diastolic = other.optionalDiastolic().map(IntegerFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BloodpressureCriteria copy() {
        return new BloodpressureCriteria(this);
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

    public IntegerFilter getSystolic() {
        return systolic;
    }

    public Optional<IntegerFilter> optionalSystolic() {
        return Optional.ofNullable(systolic);
    }

    public IntegerFilter systolic() {
        if (systolic == null) {
            setSystolic(new IntegerFilter());
        }
        return systolic;
    }

    public void setSystolic(IntegerFilter systolic) {
        this.systolic = systolic;
    }

    public IntegerFilter getDiastolic() {
        return diastolic;
    }

    public Optional<IntegerFilter> optionalDiastolic() {
        return Optional.ofNullable(diastolic);
    }

    public IntegerFilter diastolic() {
        if (diastolic == null) {
            setDiastolic(new IntegerFilter());
        }
        return diastolic;
    }

    public void setDiastolic(IntegerFilter diastolic) {
        this.diastolic = diastolic;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final BloodpressureCriteria that = (BloodpressureCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(datetime, that.datetime) &&
            Objects.equals(systolic, that.systolic) &&
            Objects.equals(diastolic, that.diastolic) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datetime, systolic, diastolic, userId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BloodpressureCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDatetime().map(f -> "datetime=" + f + ", ").orElse("") +
            optionalSystolic().map(f -> "systolic=" + f + ", ").orElse("") +
            optionalDiastolic().map(f -> "diastolic=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
