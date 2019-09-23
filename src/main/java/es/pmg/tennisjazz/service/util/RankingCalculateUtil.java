package es.pmg.tennisjazz.service.util;

import es.pmg.tennisjazz.domain.Match;
import es.pmg.tennisjazz.domain.Player;

import java.util.List;

public final class RankingCalculateUtil {

    private static final String ABANDONED = "Abandoned";
    private static final Integer ABANDONED_POINTS = 1;
    private static final String NOPRESENT = "NoPresent";
    private static final Integer NOPRESENT_POINTS = 0;
    private static final String WON = "Won";
    private static final Integer WON_POINTS = 3;
    private static final String LOSS = "Loss";
    private static final Integer LOSS_POINTS = 2;
    private static final String NN = "Nn";

    private RankingCalculateUtil() {
    }

    /**
     * Calculate point won for a player in a sets of matches
     * @param player
     * @param mathes
     * @return total points won.
     */
    public static Integer calculatePoints(Player player, List<Match> matches) {
        Integer totalPoints = 0;
        for (Match match : matches) {
            switch (getMatchResult(player, match)) {
                case WON:
                    totalPoints = totalPoints + WON_POINTS;
                    break;
                case LOSS:
                    totalPoints = totalPoints + LOSS_POINTS;
                    break;
                case ABANDONED:
                    totalPoints = totalPoints + ABANDONED_POINTS;
                    break;
                case NOPRESENT:
                    totalPoints = totalPoints + NOPRESENT_POINTS;
                default:
                    break;
            }
        }
        return totalPoints;
    }

    /**
     * Calculate games won for a player in a set of matches
     * @param player
     * @param matches
     * @return total won games
     */
    public static Integer calculateGamesWon(Player player, List<Match> matches) {
        Integer totalGamesWon = 0;
        for (Match match: matches) {
            if (isMatchPlayed(match)) {
                if (isLocalPlayer(player, match)) {
                    totalGamesWon = totalGamesWon + calculateLocalPlayerGamesWon(match);
                } else {
                    totalGamesWon = totalGamesWon + calculateVisitorPlayerGamesWon(match);
                }
            }
        }
        return totalGamesWon;
    }

    /**
     * Calculate games loss for a player in a set of matches
     * @param player
     * @param matches
     * @return total loss games
     */
    public static Integer calculateGamesLoss(Player player, List<Match> matches) {
        Integer totalGamesLoss = 0;
        for (Match match: matches) {
            if (isMatchPlayed(match)) {
                if (!isLocalPlayer(player, match)) {
                    totalGamesLoss = totalGamesLoss + calculateLocalPlayerGamesWon(match);
                } else {
                    totalGamesLoss = totalGamesLoss + calculateVisitorPlayerGamesWon(match);
                }
            }
        }
        return totalGamesLoss;
    }

    /**
     * Sum total local player games won in three sets
     * @param match
     * @return total games won
     */
    private static Integer calculateLocalPlayerGamesWon(Match match) {
        Integer set1Games = match.getLocalPlayerSet1Result() != null ? match.getLocalPlayerSet1Result() : 0;
        Integer set2Games = match.getLocalPlayerSet2Result() != null ? match.getLocalPlayerSet2Result() : 0;
        Integer set3Games = match.getLocalPlayerSet3Result() != null ? match.getLocalPlayerSet3Result() : 0;
        return set1Games + set2Games + set3Games;
    }

    /**
     * Sum total visitor player games in three sets
     * @param match
     * @return total games
     */
    private static Integer calculateVisitorPlayerGamesWon(Match match) {
        Integer set1Games = match.getVisitorPlayerSet1Result() != null ? match.getVisitorPlayerSet1Result() : 0;
        Integer set2Games = match.getVisitorPlayerSet2Result() != null ? match.getVisitorPlayerSet2Result() : 0;
        Integer set3Games = match.getVisitorPlayerSet3Result() != null ? match.getVisitorPlayerSet3Result() : 0;
        return set1Games + set2Games + set3Games;
    }

    /**
     * Get the match result for a player
     * @param player
     * @param match
     * @return String that represent the result
     */
    private static String getMatchResult(Player player, Match match){
        if (isMatchPlayed(match)) {
            if (isLocalPlayer(player, match)) {
                if (match.isLocalPlayerAbandoned()) return ABANDONED;
                if (match.isLocalPlayerNotPresent()) return NOPRESENT;
                if (match.getLocalPlayerSets() > match.getVisitorPlayerSets()) return WON;
                if (match.getLocalPlayerSets() < match.getVisitorPlayerSets()) return LOSS;
            } else {
                if (match.isVisitorPlayerAbandoned()) return ABANDONED;
                if (match.isVisitorPlayerNotPresent()) return NOPRESENT;
                if (match.getVisitorPlayerSets() > match.getLocalPlayerSets()) return WON;
                if (match.getVisitorPlayerSets() < match.getLocalPlayerSets()) return LOSS;
            }
            return NN;
        } else {
            return NN;
        }
    }

    /**
     * Check if palyer is the local in match
     * @param player
     * @param match
     * @return true if local
     */
    private static boolean isLocalPlayer(Player player, Match match){
        return match.getLocalPlayer().equals(player);
    }

    /**
     * Check if match has been played
     * @param match
     * @return true if played
     */
    private static boolean isMatchPlayed(Match match) {
        return match.isLocalPlayerAbandoned() != null || match.isVisitorPlayerAbandoned()
            || match.isLocalPlayerNotPresent() || match.isVisitorPlayerNotPresent()
            || (match.getLocalPlayerSets() != null && match.getVisitorPlayerSets() != null);
    }

}
