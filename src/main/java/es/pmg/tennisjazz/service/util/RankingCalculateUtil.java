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
     * @param player the player
     * @param matches the matches to see
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
     * @param player the player
     * @param matches the matches to see
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
     * @param player the player
     * @param matches the matches to see
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
     * Calculate total set won by a player in a sets of matches
     * @param player the player
     * @param matches the matches
     * @return total sets won
     */
    public static Integer calculateSetsWon(Player player, List<Match> matches) {
        Integer totalSetsWon = 0;
        for (Match match: matches) {
            if (isMatchPlayed(match)) {
                if (isLocalPlayer(player, match)) {
                    totalSetsWon = totalSetsWon + calculateLocalPlayerSetsWon(match);
                } else {
                    totalSetsWon = totalSetsWon + calculateVisitorPlayerSetsWon(match);
                }
            }
        }
        return totalSetsWon;
    }

    /**
     * Calculate total sets loss by a player in a set of matches
     * @param player the player
     * @param matches the matches
     * @return the total sets loss
     */
    public static Integer calculateSetsLoss(Player player, List<Match> matches) {
        Integer totalSetsLoss = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)) {
                if (isLocalPlayer(player, match)) {
                    totalSetsLoss = totalSetsLoss + calculateVisitorPlayerSetsWon(match);
                } else {
                    totalSetsLoss = totalSetsLoss + calculateLocalPlayerSetsWon(match);
                }
            }
        }
        return totalSetsLoss;
    }

    /**
     * Calculate the player loss matches in a set of matches
     * @param player the player
     * @param matches the matches
     * @return the total won matches
     */
    public static Integer calculateMatchesWon(Player player, List<Match> matches) {
        Integer totalMatchesWon = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)){
                if (getMatchResult(player, match).equals(WON)){
                    totalMatchesWon ++;
                }
            }
        }
        return totalMatchesWon;
    }

    /**
     * Calculate the player loss matches in a set of matches
     * @param player the player
     * @param matches the matches
     * @return the total loss matches
     */
    public static Integer calculateMatchesLoss(Player player, List<Match> matches) {
        Integer totalMatchesLoss = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)) {
                if (getMatchResult(player, match).equals(LOSS)) {
                    totalMatchesLoss++;
                }
            }
        }
        return totalMatchesLoss;
    }

    /**
     * Calculate the player no present matches in a sets of matches
     * @param player the player
     * @param matches the matches
     * @return total not present matches
     */
    public static Integer calculateMatchesNotPresent(Player player, List<Match> matches) {
        Integer totalMatchesNotPresent = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)) {
                if (getMatchResult(player, match).equals(NOPRESENT)) {
                    totalMatchesNotPresent++;
                }
            }
        }
        return totalMatchesNotPresent;
    }

    /**
     * Calculate the player abandoned matches in a set of matches
     * @param player the player
     * @param matches the matches
     * @return total abandoned matches
     */
    public static Integer calculateMatchesAbandoned(Player player, List<Match> matches) {
        Integer totalMatchesAbandoned = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)) {
                if (getMatchResult(player, match).equals(ABANDONED)) {
                    totalMatchesAbandoned++;
                }
            }
        }
        return totalMatchesAbandoned;
    }

    /**
     * Calculate the number of tie breaks played in a set of matches
     *
     * @param matches the matches
     * @return total of played tie breaks
     */
    public static Integer calculateTieBreaksPlayed(List<Match> matches) {
        Integer totalTieBreaksPlayed = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)) {
                totalTieBreaksPlayed = totalTieBreaksPlayed + calculateTieBreaksPlayed(match);
            }
        }
        return totalTieBreaksPlayed;
    }

    /**
     * Calculate tie breaks won by a player in a set of matches
     * @param player the player
     * @param matches the matches
     * @return total tie breaks won
     */
    public static Integer calculateTieBreaksWon(Player player, List<Match> matches) {
        Integer totalTieBreaksWon = 0;
        for (Match match : matches){
            if (isMatchPlayed(match)) {
                if (isLocalPlayer(player, match)) {
                    totalTieBreaksWon = totalTieBreaksWon + calculateLocalPlayerTieBreaksWon(match);
                } else {
                    totalTieBreaksWon = totalTieBreaksWon + calculateVisitorPlayerTieBreaksWon(match);
                }
            }
        }
        return totalTieBreaksWon;
    }

    /**
     * Calculate the number of matches played from a set of math
     * @param matches the matches
     * @return total matches played
     */
    public static Integer calculateMatchesPlayed (List<Match> matches) {
        Integer totalMatchesPlayed = 0;
        for (Match match : matches) {
            if (isMatchPlayed(match)) {
                totalMatchesPlayed ++;
            }
        }
        return totalMatchesPlayed;
    }

    /**
     * Calculate the number of sets won by a local player in a match
     *
     * @param match the match
     * @return total sets won
     */
    private static Integer calculateLocalPlayerSetsWon(Match match) {
        return match.getLocalPlayerSets() != null ? match.getLocalPlayerSets() : 0;
    }

    /**
     * Calculate the number of sets won by a visitor player in a match
     *
     * @param match the match
     * @return total sets won
     */
    private static Integer calculateVisitorPlayerSetsWon(Match match) {
        return match.getVisitorPlayerSets() != null ? match.getVisitorPlayerSets() : 0;
    }

    /**
     * Sum total local player games won in three sets
     * @param match the matches
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
     * @param match the match
     * @return total games
     */
    private static Integer calculateVisitorPlayerGamesWon(Match match) {
        Integer set1Games = match.getVisitorPlayerSet1Result() != null ? match.getVisitorPlayerSet1Result() : 0;
        Integer set2Games = match.getVisitorPlayerSet2Result() != null ? match.getVisitorPlayerSet2Result() : 0;
        Integer set3Games = match.getVisitorPlayerSet3Result() != null ? match.getVisitorPlayerSet3Result() : 0;
        return set1Games + set2Games + set3Games;
    }

    /**
     * Calculate the local player won tie breaks in one match
     * @param match the match
     * @return total won tie breaks
     */
    private static Integer calculateLocalPlayerTieBreaksWon(Match match) {
        Integer totalTieBreaksWon = 0;
        if (isSet1TieBreaksPlayed(match)) {
            if (match.getLocalPlayerTBSet1Result() > match.getVisitorPlayerTBSet1Result()) totalTieBreaksWon ++;
        }
        if (isSet2TieBreaksPlayed(match)) {
            if (match.getLocalPlayerTBSet2Result() > match.getVisitorPlayerTBSet2Result()) totalTieBreaksWon ++;
        }
        if (isSet3TieBreaksPlayed(match)) {
            if (match.getLocalPlayerTBSet3Result() > match.getVisitorPlayerTBSet3Result()) totalTieBreaksWon ++;
        }
        return totalTieBreaksWon;
    }

    /**
     * Calculate the visitor player won tie breaks in one match
     * @param match the match
     * @return total won tie breaks
     */
    private static Integer calculateVisitorPlayerTieBreaksWon(Match match) {
        Integer totalTieBreaksWon = 0;
        if (isSet1TieBreaksPlayed(match)) {
            if (match.getLocalPlayerTBSet1Result() < match.getVisitorPlayerTBSet1Result()) totalTieBreaksWon ++;
        }
        if (isSet2TieBreaksPlayed(match)) {
            if (match.getLocalPlayerTBSet2Result() < match.getVisitorPlayerTBSet2Result()) totalTieBreaksWon ++;
        }
        if (isSet3TieBreaksPlayed(match)) {
            if (match.getLocalPlayerTBSet3Result() < match.getVisitorPlayerTBSet3Result()) totalTieBreaksWon ++;
        }
        return totalTieBreaksWon;
    }

    /**
     * Check if tie break was played in first set
     * @param match the match
     * @return true is played
     */
    private static boolean isSet1TieBreaksPlayed(Match match) {
        return match.getLocalPlayerTBSet1Result() != null && match.getVisitorPlayerTBSet1Result() != null;
    }

    /**
     * Check if tie break was played in second set
     * @param match the match
     * @return true is played
     */
    private static boolean isSet2TieBreaksPlayed(Match match) {
        return match.getLocalPlayerTBSet2Result() != null && match.getVisitorPlayerTBSet2Result() != null;
    }

    /**
     * Check if tie break was played in third set
     * @param match the match
     * @return true is played
     */
    private static boolean isSet3TieBreaksPlayed(Match match) {
        return match.getLocalPlayerTBSet3Result() != null && match.getVisitorPlayerTBSet3Result() != null;
    }

    /**
     * Get the match result for a player
     * @param player the player
     * @param match the match
     * @return String that represent the result
     */
    private static String getMatchResult(Player player, Match match){
        if (isMatchPlayed(match)) {
            if (isLocalPlayer(player, match)) {
                if (match.isLocalPlayerAbandoned() != null && match.isLocalPlayerAbandoned()) return ABANDONED;
                if (match.isVisitorPlayerAbandoned() != null && match.isVisitorPlayerAbandoned()) return WON;
                if (match.isLocalPlayerNotPresent() != null && match.isLocalPlayerNotPresent()) return NOPRESENT;
                if (match.isVisitorPlayerNotPresent() != null && match.isVisitorPlayerNotPresent()) return WON;
                if (match.getLocalPlayerSets() != null && match.getVisitorPlayerSets() != null && match.getLocalPlayerSets() > match.getVisitorPlayerSets()) return WON;
                if (match.getLocalPlayerSets() != null && match.getVisitorPlayerSets() != null && match.getLocalPlayerSets() < match.getVisitorPlayerSets()) return LOSS;
            } else {
                if (match.isVisitorPlayerAbandoned() != null && match.isVisitorPlayerAbandoned()) return ABANDONED;
                if (match.isLocalPlayerAbandoned() != null && match.isLocalPlayerAbandoned()) return WON;
                if (match.isVisitorPlayerNotPresent() != null && match.isVisitorPlayerNotPresent()) return NOPRESENT;
                if (match.isLocalPlayerNotPresent() != null && match.isLocalPlayerNotPresent()) return WON;
                if (match.getVisitorPlayerSets() != null && match.getLocalPlayerSets() != null && match.getVisitorPlayerSets() > match.getLocalPlayerSets()) return WON;
                if (match.getVisitorPlayerSets() != null && match.getLocalPlayerSets() != null && match.getVisitorPlayerSets() < match.getLocalPlayerSets()) return LOSS;
            }
            return NN;
        } else {
            return NN;
        }
    }

    /**
     * Check if player is the local in match
     * @param player the player
     * @param match the match
     * @return true if local
     */
    private static boolean isLocalPlayer(Player player, Match match){
        return match.getLocalPlayer().equals(player);
    }

    /**
     * Check if match has been played
     * @param match the match
     * @return true if played
     */
    private static boolean isMatchPlayed(Match match) {
        return match.isLocalPlayerAbandoned() != null || match.isVisitorPlayerAbandoned() != null
            || match.isLocalPlayerNotPresent() != null || match.isVisitorPlayerNotPresent() != null
            || (match.getLocalPlayerSets() != null && match.getVisitorPlayerSets() != null);
    }

    /**
     * Calculate the number of tie breaks played in a match
     * @param match the match
     * @return total tie breaks played.
     */
    private static Integer calculateTieBreaksPlayed(Match match) {
        Integer totalTieBreaksPlayed = 0;
        if (match.getLocalPlayerTBSet1Result() != null) totalTieBreaksPlayed ++;
        if (match.getLocalPlayerTBSet2Result() != null) totalTieBreaksPlayed ++;
        if (match.getLocalPlayerTBSet3Result() != null) totalTieBreaksPlayed ++;
        return totalTieBreaksPlayed;
    }
}
