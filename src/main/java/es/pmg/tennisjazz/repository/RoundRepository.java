package es.pmg.tennisjazz.repository;

import es.pmg.tennisjazz.domain.Round;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Round entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoundRepository extends JpaRepository<Round, Long>, JpaSpecificationExecutor<Round> {

}
