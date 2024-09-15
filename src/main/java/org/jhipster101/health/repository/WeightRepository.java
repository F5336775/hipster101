package org.jhipster101.health.repository;

import java.util.List;
import java.util.Optional;
import org.jhipster101.health.domain.Weight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Weight entity.
 */
@Repository
public interface WeightRepository extends JpaRepository<Weight, Long>, JpaSpecificationExecutor<Weight> {
    @Query("select weight from Weight weight where weight.manytoone.login = ?#{authentication.name}")
    List<Weight> findByManytooneIsCurrentUser();

    default Optional<Weight> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Weight> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Weight> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select weight from Weight weight left join fetch weight.manytoone",
        countQuery = "select count(weight) from Weight weight"
    )
    Page<Weight> findAllWithToOneRelationships(Pageable pageable);

    @Query("select weight from Weight weight left join fetch weight.manytoone")
    List<Weight> findAllWithToOneRelationships();

    @Query("select weight from Weight weight left join fetch weight.manytoone where weight.id =:id")
    Optional<Weight> findOneWithToOneRelationships(@Param("id") Long id);
}
