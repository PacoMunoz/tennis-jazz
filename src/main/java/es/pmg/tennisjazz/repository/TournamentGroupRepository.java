package es.pmg.tennisjazz.repository;

import es.pmg.tennisjazz.domain.TournamentGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TournamentGroup entity.
 */
@Repository
public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long>, JpaSpecificationExecutor<TournamentGroup> {

    @Query(value = "select distinct tournamentGroup from TournamentGroup tournamentGroup left join fetch tournamentGroup.players",
        countQuery = "select count(distinct tournamentGroup) from TournamentGroup tournamentGroup")
    Page<TournamentGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tournamentGroup from TournamentGroup tournamentGroup left join fetch tournamentGroup.players")
    List<TournamentGroup> findAllWithEagerRelationships();

    @Query("select tournamentGroup from TournamentGroup tournamentGroup left join fetch tournamentGroup.players where tournamentGroup.id =:id")
    Optional<TournamentGroup> findOneWithEagerRelationships(@Param("id") Long id);

}
