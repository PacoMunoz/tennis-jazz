package es.pmg.tennisjazz.repository;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.repository.custom.CustomizedMatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Match entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match>, CustomizedMatchRepository {

    @Query(
        value = "SELECT * FROM JHI_MATCH M \n" +
                    "INNER JOIN ROUND R \n" +
                    "ON M.ROUND_ID = R.ID \n" +
                    "WHERE (M.VISITOR_PLAYER_ID = ?1 OR M.LOCAL_PLAYER_ID = ?1)\n" +
                        "AND (R.START_DATE  <= NOW() AND R.END_DATE >= NOW())",
        countQuery = "SELECT COUNT(*) FROM JHI_MATCH M \n" +
                        "INNER JOIN ROUND R \n" +
                        "ON M.ROUND_ID = R.ID \n" +
                        "WHERE (M.VISITOR_PLAYER_ID = ?1 OR M.LOCAL_PLAYER_ID = ?1)\n" +
                            "AND (R.START_DATE  <= NOW() AND R.END_DATE >= NOW())",
        nativeQuery = true)
    Page<Match> buscarPartidosEnCurso(Long idPlayer, Pageable pageable);
}
