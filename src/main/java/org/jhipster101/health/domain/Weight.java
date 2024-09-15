package org.jhipster101.health.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Weight.
 */
@Entity
@Table(name = "weight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "weight")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Weight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "datetime", nullable = false)
    private ZonedDateTime datetime;

    @Column(name = "weight")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer weight;

    @ManyToOne(fetch = FetchType.LAZY)
    private User manytoone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Weight id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDatetime() {
        return this.datetime;
    }

    public Weight datetime(ZonedDateTime datetime) {
        this.setDatetime(datetime);
        return this;
    }

    public void setDatetime(ZonedDateTime datetime) {
        this.datetime = datetime;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public Weight weight(Integer weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public User getManytoone() {
        return this.manytoone;
    }

    public void setManytoone(User user) {
        this.manytoone = user;
    }

    public Weight manytoone(User user) {
        this.setManytoone(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weight)) {
            return false;
        }
        return getId() != null && getId().equals(((Weight) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Weight{" +
            "id=" + getId() +
            ", datetime='" + getDatetime() + "'" +
            ", weight=" + getWeight() +
            "}";
    }
}
