package es.pmg.tennisjazz.repository;

import es.pmg.tennisjazz.domain.Gender;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Gender entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenderRepository extends JpaRepository<Gender, Long>, JpaSpecificationExecutor<Gender> {

}
