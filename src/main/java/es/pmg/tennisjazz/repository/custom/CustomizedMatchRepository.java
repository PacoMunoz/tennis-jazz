package es.pmg.tennisjazz.repository.custom;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Player;
import es.pmg.tennisjazz.domain.Round;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CustomizedMatchRepository {

    List<Match> buscarTodosPorJugadorYJornadas(Player player, List<Round> rounds);

}
