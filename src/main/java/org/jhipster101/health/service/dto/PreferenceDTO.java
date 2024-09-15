package org.jhipster101.health.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.jhipster101.health.domain.enumeration.Units;

/**
 * A DTO for the {@link org.jhipster101.health.domain.Preference} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PreferenceDTO implements Serializable {

    private Long id;

    @Min(value = 10)
    @Max(value = 21)
    private Integer weeklyGoal;

    private Units weightUnits;

    private UserDTO onetoone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(Integer weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public Units getWeightUnits() {
        return weightUnits;
    }

    public void setWeightUnits(Units weightUnits) {
        this.weightUnits = weightUnits;
    }

    public UserDTO getOnetoone() {
        return onetoone;
    }

    public void setOnetoone(UserDTO onetoone) {
        this.onetoone = onetoone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PreferenceDTO)) {
            return false;
        }

        PreferenceDTO preferenceDTO = (PreferenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, preferenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferenceDTO{" +
            "id=" + getId() +
            ", weeklyGoal=" + getWeeklyGoal() +
            ", weightUnits='" + getWeightUnits() + "'" +
            ", onetoone=" + getOnetoone() +
            "}";
    }
}
