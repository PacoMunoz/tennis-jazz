package es.pmg.tennisjazz.repository.custom.impl;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.repository.custom.CustomizedMatchRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CustomizedMatchRepositoryImpl implements CustomizedMatchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Match> buscarTodosPorJugadorYJornadas(Player player, List<Round> rounds) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Match> query = cb.createQuery(Match.class);
        Root<Match> match = query.from(Match.class);

        Path<Long> playerPath = match.get("localPlayer").get("id");

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(playerPath, player.getId()));

        query.select(match).where(predicates.toArray(new Predicate[predicates.size()]));

        System.out.println("Saliendo del metodo");

        return entityManager.createQuery(query).getResultList();

        //Path<Player> playerPath = match.get("localPlayer");
        //List<Predicate> predicates = new ArrayList<>();
/*

        for (Round roundParam: rounds) {
            predicates.add(cb.isMember(roundPath, roundParam));
        }
*/
        //return null;
    }

}
