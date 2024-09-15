package org.jhipster101.health.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.jhipster101.health.domain.Preferences} entity. This class is used
 * in {@link org.jhipster101.health.web.rest.PreferencesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PreferencesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter weekelygoal;

    private StringFilter weightunits;

    private LongFilter manytooneId;

    private Boolean distinct;

    public PreferencesCriteria() {}

    public PreferencesCriteria(PreferencesCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.weekelygoal = other.optionalWeekelygoal().map(StringFilter::copy).orElse(null);
        this.weightunits = other.optionalWeightunits().map(StringFilter::copy).orElse(null);
        this.manytooneId = other.optionalManytooneId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PreferencesCriteria copy() {
        return new PreferencesCriteria(this);
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

    public StringFilter getWeekelygoal() {
        return weekelygoal;
    }

    public Optional<StringFilter> optionalWeekelygoal() {
        return Optional.ofNullable(weekelygoal);
    }

    public StringFilter weekelygoal() {
        if (weekelygoal == null) {
            setWeekelygoal(new StringFilter());
        }
        return weekelygoal;
    }

    public void setWeekelygoal(StringFilter weekelygoal) {
        this.weekelygoal = weekelygoal;
    }

    public StringFilter getWeightunits() {
        return weightunits;
    }

    public Optional<StringFilter> optionalWeightunits() {
        return Optional.ofNullable(weightunits);
    }

    public StringFilter weightunits() {
        if (weightunits == null) {
            setWeightunits(new StringFilter());
        }
        return weightunits;
    }

    public void setWeightunits(StringFilter weightunits) {
        this.weightunits = weightunits;
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
        final PreferencesCriteria that = (PreferencesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(weekelygoal, that.weekelygoal) &&
            Objects.equals(weightunits, that.weightunits) &&
            Objects.equals(manytooneId, that.manytooneId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, weekelygoal, weightunits, manytooneId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalWeekelygoal().map(f -> "weekelygoal=" + f + ", ").orElse("") +
            optionalWeightunits().map(f -> "weightunits=" + f + ", ").orElse("") +
            optionalManytooneId().map(f -> "manytooneId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
