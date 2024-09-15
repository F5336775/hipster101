package org.jhipster101.health.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link org.jhipster101.health.domain.Bloodpressure} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BloodpressureDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime datetime;

    private Integer systolic;

    private Integer diastolic;

    private UserDTO user;

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

    public Integer getSystolic() {
        return systolic;
    }

    public void setSystolic(Integer systolic) {
        this.systolic = systolic;
    }

    public Integer getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Integer diastolic) {
        this.diastolic = diastolic;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BloodpressureDTO)) {
            return false;
        }

        BloodpressureDTO bloodpressureDTO = (BloodpressureDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bloodpressureDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BloodpressureDTO{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", systolic=" + getSystolic() +
            ", diastolic=" + getDiastolic() +
            ", user=" + getUser() +
            "}";
    }
}
