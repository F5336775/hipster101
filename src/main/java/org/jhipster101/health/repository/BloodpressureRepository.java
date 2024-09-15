package org.jhipster101.health.repository;

import java.util.List;
import java.util.Optional;
import org.jhipster101.health.domain.Bloodpressure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bloodpressure entity.
 */
@Repository
public interface BloodpressureRepository extends JpaRepository<Bloodpressure, Long>, JpaSpecificationExecutor<Bloodpressure> {
    @Query("select bloodpressure from Bloodpressure bloodpressure where bloodpressure.user.login = ?#{authentication.name}")
    List<Bloodpressure> findByUserIsCurrentUser();

    default Optional<Bloodpressure> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Bloodpressure> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Bloodpressure> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select bloodpressure from Bloodpressure bloodpressure left join fetch bloodpressure.user",
        countQuery = "select count(bloodpressure) from Bloodpressure bloodpressure"
    )
    Page<Bloodpressure> findAllWithToOneRelationships(Pageable pageable);

    @Query("select bloodpressure from Bloodpressure bloodpressure left join fetch bloodpressure.user")
    List<Bloodpressure> findAllWithToOneRelationships();

    @Query("select bloodpressure from Bloodpressure bloodpressure left join fetch bloodpressure.user where bloodpressure.id =:id")
    Optional<Bloodpressure> findOneWithToOneRelationships(@Param("id") Long id);
}
