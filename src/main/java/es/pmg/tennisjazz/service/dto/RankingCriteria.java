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

    private IntegerFilter gamesWon;

    private IntegerFilter gamesLoss;

    private IntegerFilter setsWon;

    private IntegerFilter setsLoss;

    private IntegerFilter matchesPlayed;

    private IntegerFilter matchesWon;

    private IntegerFilter matchesLoss;

    private IntegerFilter matchesNotPresent;

    private IntegerFilter matchesAbandoned;

    private LongFilter tournamentGroupId;

    private LongFilter playerId;

    public RankingCriteria(){
    }

    public RankingCriteria(RankingCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.gamesWon = other.gamesWon == null ? null : other.gamesWon.copy();
        this.gamesLoss = other.gamesLoss == null ? null : other.gamesLoss.copy();
        this.setsWon = other.setsWon == null ? null : other.setsWon.copy();
        this.setsLoss = other.setsLoss == null ? null : other.setsLoss.copy();
        this.matchesPlayed = other.matchesPlayed == null ? null : other.matchesPlayed.copy();
        this.matchesWon = other.matchesWon == null ? null : other.matchesWon.copy();
        this.matchesLoss = other.matchesLoss == null ? null : other.matchesLoss.copy();
        this.matchesNotPresent = other.matchesNotPresent == null ? null : other.matchesNotPresent.copy();
        this.matchesAbandoned = other.matchesAbandoned == null ? null : other.matchesAbandoned.copy();
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

    public IntegerFilter getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(IntegerFilter gamesWon) {
        this.gamesWon = gamesWon;
    }

    public IntegerFilter getGamesLoss() {
        return gamesLoss;
    }

    public void setGamesLoss(IntegerFilter gamesLoss) {
        this.gamesLoss = gamesLoss;
    }

    public IntegerFilter getSetsWon() {
        return setsWon;
    }

    public void setSetsWon(IntegerFilter setsWon) {
        this.setsWon = setsWon;
    }

    public IntegerFilter getSetsLoss() {
        return setsLoss;
    }

    public void setSetsLoss(IntegerFilter setsLoss) {
        this.setsLoss = setsLoss;
    }

    public IntegerFilter getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(IntegerFilter matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public IntegerFilter getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(IntegerFilter matchesWon) {
        this.matchesWon = matchesWon;
    }

    public IntegerFilter getMatchesLoss() {
        return matchesLoss;
    }

    public void setMatchesLoss(IntegerFilter matchesLoss) {
        this.matchesLoss = matchesLoss;
    }

    public IntegerFilter getMatchesNotPresent() {
        return matchesNotPresent;
    }

    public void setMatchesNotPresent(IntegerFilter matchesNotPresent) {
        this.matchesNotPresent = matchesNotPresent;
    }

    public IntegerFilter getMatchesAbandoned() {
        return matchesAbandoned;
    }

    public void setMatchesAbandoned(IntegerFilter matchesAbandoned) {
        this.matchesAbandoned = matchesAbandoned;
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
            Objects.equals(gamesWon, that.gamesWon) &&
            Objects.equals(gamesLoss, that.gamesLoss) &&
            Objects.equals(setsWon, that.setsWon) &&
            Objects.equals(setsLoss, that.setsLoss) &&
            Objects.equals(matchesPlayed, that.matchesPlayed) &&
            Objects.equals(matchesWon, that.matchesWon) &&
            Objects.equals(matchesLoss, that.matchesLoss) &&
            Objects.equals(matchesNotPresent, that.matchesNotPresent) &&
            Objects.equals(matchesAbandoned, that.matchesAbandoned) &&
            Objects.equals(tournamentGroupId, that.tournamentGroupId) &&
            Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        points,
        gamesWon,
        gamesLoss,
        setsWon,
        setsLoss,
        matchesPlayed,
        matchesWon,
        matchesLoss,
        matchesNotPresent,
        matchesAbandoned,
        tournamentGroupId,
        playerId
        );
    }

    @Override
    public String toString() {
        return "RankingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (gamesWon != null ? "gamesWon=" + gamesWon + ", " : "") +
                (gamesLoss != null ? "gamesLoss=" + gamesLoss + ", " : "") +
                (setsWon != null ? "setsWon=" + setsWon + ", " : "") +
                (setsLoss != null ? "setsLoss=" + setsLoss + ", " : "") +
                (matchesPlayed != null ? "matchesPlayed=" + matchesPlayed + ", " : "") +
                (matchesWon != null ? "matchesWon=" + matchesWon + ", " : "") +
                (matchesLoss != null ? "matchesLoss=" + matchesLoss + ", " : "") +
                (matchesNotPresent != null ? "matchesNotPresent=" + matchesNotPresent + ", " : "") +
                (matchesAbandoned != null ? "matchesAbandoned=" + matchesAbandoned + ", " : "") +
                (tournamentGroupId != null ? "tournamentGroupId=" + tournamentGroupId + ", " : "") +
                (playerId != null ? "playerId=" + playerId + ", " : "") +
            "}";
    }

}
