package org.jhipster101.health.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.jhipster101.health.domain.Preferences} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PreferencesDTO implements Serializable {

    private Long id;

    private String weekelygoal;

    private String weightunits;

    private UserDTO manytoone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeekelygoal() {
        return weekelygoal;
    }

    public void setWeekelygoal(String weekelygoal) {
        this.weekelygoal = weekelygoal;
    }

    public String getWeightunits() {
        return weightunits;
    }

    public void setWeightunits(String weightunits) {
        this.weightunits = weightunits;
    }

    public UserDTO getManytoone() {
        return manytoone;
    }

    public void setManytoone(UserDTO manytoone) {
        this.manytoone = manytoone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PreferencesDTO)) {
            return false;
        }

        PreferencesDTO preferencesDTO = (PreferencesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, preferencesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesDTO{" +
            "id=" + getId() +
            ", weekelygoal='" + getWeekelygoal() + "'" +
            ", weightunits='" + getWeightunits() + "'" +
            ", manytoone=" + getManytoone() +
            "}";
    }
}
