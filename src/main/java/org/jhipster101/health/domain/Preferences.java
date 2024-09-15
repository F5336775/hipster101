package org.jhipster101.health.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "preferences")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "weekelygoal")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String weekelygoal;

    @Column(name = "weightunits")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String weightunits;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User manytoone;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Preferences id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWeekelygoal() {
        return this.weekelygoal;
    }

    public Preferences weekelygoal(String weekelygoal) {
        this.setWeekelygoal(weekelygoal);
        return this;
    }

    public void setWeekelygoal(String weekelygoal) {
        this.weekelygoal = weekelygoal;
    }

    public String getWeightunits() {
        return this.weightunits;
    }

    public Preferences weightunits(String weightunits) {
        this.setWeightunits(weightunits);
        return this;
    }

    public void setWeightunits(String weightunits) {
        this.weightunits = weightunits;
    }

    public User getManytoone() {
        return this.manytoone;
    }

    public void setManytoone(User user) {
        this.manytoone = user;
    }

    public Preferences manytoone(User user) {
        this.setManytoone(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preferences)) {
            return false;
        }
        return getId() != null && getId().equals(((Preferences) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + getId() +
            ", weekelygoal='" + getWeekelygoal() + "'" +
            ", weightunits='" + getWeightunits() + "'" +
            "}";
    }
}
