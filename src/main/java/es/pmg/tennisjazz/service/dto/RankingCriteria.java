package es.pmg.tennisjazz.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link es.pmg.tennisjazz.domain.Ranking} entity. This class is used
 * in {@link es.pmg.tennisjazz.web.rest.RankingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rankings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RankingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter points;

    private IntegerFilter gamesWin;

    private IntegerFilter gamesLoss;

    private IntegerFilter setsWin;

    private IntegerFilter setsLost;

    private IntegerFilter matchesPlayed;

    private IntegerFilter matchesWined;

    private IntegerFilter matchesLoss;

    private LongFilter tournamentGroupId;

    private LongFilter playerId;

    public RankingCriteria(){
    }

    public RankingCriteria(RankingCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.gamesWin = other.gamesWin == null ? null : other.gamesWin.copy();
        this.gamesLoss = other.gamesLoss == null ? null : other.gamesLoss.copy();
        this.setsWin = other.setsWin == null ? null : other.setsWin.copy();
        this.setsLost = other.setsLost == null ? null : other.setsLost.copy();
        this.matchesPlayed = other.matchesPlayed == null ? null : other.matchesPlayed.copy();
        this.matchesWined = other.matchesWined == null ? null : other.matchesWined.copy();
        this.matchesLoss = other.matchesLoss == null ? null : other.matchesLoss.copy();
        this.tournamentGroupId = other.tournamentGroupId == null ? null : other.tournamentGroupId.copy();
        this.playerId = other.playerId == null ? null : other.playerId.copy();
    }

    @Override
    public RankingCriteria copy() {
        return new RankingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public IntegerFilter getGamesWin() {
        return gamesWin;
    }

    public void setGamesWin(IntegerFilter gamesWin) {
        this.gamesWin = gamesWin;
    }

    public IntegerFilter getGamesLoss() {
        return gamesLoss;
    }

    public void setGamesLoss(IntegerFilter gamesLoss) {
        this.gamesLoss = gamesLoss;
    }

    public IntegerFilter getSetsWin() {
        return setsWin;
    }

    public void setSetsWin(IntegerFilter setsWin) {
        this.setsWin = setsWin;
    }

    public IntegerFilter getSetsLost() {
        return setsLost;
    }

    public void setSetsLost(IntegerFilter setsLost) {
        this.setsLost = setsLost;
    }

    public IntegerFilter getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(IntegerFilter matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public IntegerFilter getMatchesWined() {
        return matchesWined;
    }

    public void setMatchesWined(IntegerFilter matchesWined) {
        this.matchesWined = matchesWined;
    }

    public IntegerFilter getMatchesLoss() {
        return matchesLoss;
    }

    public void setMatchesLoss(IntegerFilter matchesLoss) {
        this.matchesLoss = matchesLoss;
    }

    public LongFilter getTournamentGroupId() {
        return tournamentGroupId;
    }

    public void setTournamentGroupId(LongFilter tournamentGroupId) {
        this.tournamentGroupId = tournamentGroupId;
    }

    public LongFilter getPlayerId() {
        return playerId;
    }

    public void setPlayerId(LongFilter playerId) {
        this.playerId = playerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RankingCriteria that = (RankingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(gamesWin, that.gamesWin) &&
            Objects.equals(gamesLoss, that.gamesLoss) &&
            Objects.equals(setsWin, that.setsWin) &&
            Objects.equals(setsLost, that.setsLost) &&
            Objects.equals(matchesPlayed, that.matchesPlayed) &&
            Objects.equals(matchesWined, that.matchesWined) &&
            Objects.equals(matchesLoss, that.matchesLoss) &&
            Objects.equals(tournamentGroupId, that.tournamentGroupId) &&
            Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        points,
        gamesWin,
        gamesLoss,
        setsWin,
        setsLost,
        matchesPlayed,
        matchesWined,
        matchesLoss,
        tournamentGroupId,
        playerId
        );
    }

    @Override
    public String toString() {
        return "RankingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (gamesWin != null ? "gamesWin=" + gamesWin + ", " : "") +
                (gamesLoss != null ? "gamesLoss=" + gamesLoss + ", " : "") +
                (setsWin != null ? "setsWin=" + setsWin + ", " : "") +
                (setsLost != null ? "setsLost=" + setsLost + ", " : "") +
                (matchesPlayed != null ? "matchesPlayed=" + matchesPlayed + ", " : "") +
                (matchesWined != null ? "matchesWined=" + matchesWined + ", " : "") +
                (matchesLoss != null ? "matchesLoss=" + matchesLoss + ", " : "") +
                (tournamentGroupId != null ? "tournamentGroupId=" + tournamentGroupId + ", " : "") +
                (playerId != null ? "playerId=" + playerId + ", " : "") +
            "}";
    }

}
