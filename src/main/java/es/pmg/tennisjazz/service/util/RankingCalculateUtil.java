package es.pmg.tennisjazz.service.util;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Player;

import java.util.List;

public final class RankingCalculateUtil {

    private static final String ABANDONED = "Abandoned";
    private static final String NOPRESENT = "NoPresent";
    private static final String WON = "Won";
    private static final String LOSS = "Loss";
    private static final String NN = "Nn";

    private RankingCalculateUtil() {
    }

    public static Integer calculatePoint(Player player, List<Match> mathes) {
        Integer totalPoints = 0;
        for (Match match : mathes) {

        }
        return null;
    }

    private String getMatchResult(Player player, Match match){
        if (isLocalPlayer(player, match)) {
            if (match.isLocalPlayerAbandoned()) return ABANDONED;
            if (match.isLocalPlayerNotPresent()) return NOPRESENT;
            if (match.getLocalPlayerSets() > match.getVisitorPlayerSets()) return WON;
            if (match.getLocalPlayerSets() < match.getVisitorPlayerSets()) return LOSS;
        }
        else {
            if (match.isVisitorPlayerAbandoned()) return ABANDONED;
            if (match.isVisitorPlayerNotPresent()) return NOPRESENT;
            if (match.getVisitorPlayerSets() > match.getLocalPlayerSets()) return WON;
            if (match.getVisitorPlayerSets() < match.getLocalPlayerSets()) return LOSS;
        }

    }

    private boolean isLocalPlayer(Player player, Match match){
        return match.getLocalPlayer().equals(player);
    }

    private boolean checkMatchPlayed() {

    }

}
