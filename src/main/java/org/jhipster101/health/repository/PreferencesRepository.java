package org.jhipster101.health.repository;

import java.util.List;
import java.util.Optional;
import org.jhipster101.health.domain.Preferences;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Preferences entity.
 */
@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long>, JpaSpecificationExecutor<Preferences> {
    default Optional<Preferences> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Preferences> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Preferences> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select preferences from Preferences preferences left join fetch preferences.manytoone",
        countQuery = "select count(preferences) from Preferences preferences"
    )
    Page<Preferences> findAllWithToOneRelationships(Pageable pageable);

    @Query("select preferences from Preferences preferences left join fetch preferences.manytoone")
    List<Preferences> findAllWithToOneRelationships();

    @Query("select preferences from Preferences preferences left join fetch preferences.manytoone where preferences.id =:id")
    Optional<Preferences> findOneWithToOneRelationships(@Param("id") Long id);
}
