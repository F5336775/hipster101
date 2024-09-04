package org.jhipster101.health.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link org.jhipster101.health.domain.Points} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PointsDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    private Integer exercise;

    private Integer meals;

    private Integer alcohol;

    @Size(max = 140)
    private String notes;

    private UserDTO manytoone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getExercise() {
        return exercise;
    }

    public void setExercise(Integer exercise) {
        this.exercise = exercise;
    }

    public Integer getMeals() {
        return meals;
    }

    public void setMeals(Integer meals) {
        this.meals = meals;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(o instanceof PointsDTO)) {
            return false;
        }

        PointsDTO pointsDTO = (PointsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pointsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PointsDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", exercise=" + getExercise() +
            ", meals=" + getMeals() +
            ", alcohol=" + getAlcohol() +
            ", notes='" + getNotes() + "'" +
            ", manytoone=" + getManytoone() +
            "}";
    }
}
