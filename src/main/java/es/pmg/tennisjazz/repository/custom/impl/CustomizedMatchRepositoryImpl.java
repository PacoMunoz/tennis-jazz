package es.pmg.tennisjazz.repository.custom.impl;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.domain.Round;
import es.pmg.tennisjazz.repository.custom.CustomizedMatchRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
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

        Path<Long> localPlayerPath = match.get("localPlayer").get("id");
        Path<Long> visitorPlayerPath = match.get("visitorPlayer").get("id");
        Path<Long> roundPath = match.get("round").get("id");

        List<Long> roundsId = new ArrayList<>();
        for (Round round: rounds) {
            roundsId.add(round.getId());
        }

        Predicate preEqLocalPlayer = cb.equal(localPlayerPath, player.getId());
        Predicate preEqVisitorPlayer = cb.equal(visitorPlayerPath, player.getId());
        Predicate preInRound = cb.and(roundPath.in(roundsId));
        Predicate prePlayer = cb.or(preEqLocalPlayer, preEqVisitorPlayer);
        Predicate finalPredicate = cb.and(prePlayer, preInRound);

        query.select(match).where(finalPredicate);
        return entityManager.createQuery(query).getResultList();
    }
}
