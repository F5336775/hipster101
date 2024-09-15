package org.jhipster101.health.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link org.jhipster101.health.domain.Weight} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeightDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime datetime;

    private Integer weight;

    private UserDTO manytoone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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
        if (!(o instanceof WeightDTO)) {
            return false;
        }

        WeightDTO weightDTO = (WeightDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, weightDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeightDTO{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", weight=" + getWeight() +
            ", manytoone=" + getManytoone() +
            "}";
    }
}
