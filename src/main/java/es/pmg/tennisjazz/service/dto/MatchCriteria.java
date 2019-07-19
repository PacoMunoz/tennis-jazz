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
 * Criteria class for the {@link es.pmg.tennisjazz.domain.Match} entity. This class is used
 * in {@link es.pmg.tennisjazz.web.rest.MatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /matches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MatchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter player1Set1Result;

    private IntegerFilter player2Set1Result;

    private IntegerFilter player1Set2Result;

    private IntegerFilter player2Set2Result;

    private IntegerFilter player1Set3Result;

    private IntegerFilter player2Set3Result;

    private LongFilter roundId;

    private LongFilter visitorPlayerId;

    private LongFilter localPlayerId;

    public MatchCriteria(){
    }

    public MatchCriteria(MatchCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.player1Set1Result = other.player1Set1Result == null ? null : other.player1Set1Result.copy();
        this.player2Set1Result = other.player2Set1Result == null ? null : other.player2Set1Result.copy();
        this.player1Set2Result = other.player1Set2Result == null ? null : other.player1Set2Result.copy();
        this.player2Set2Result = other.player2Set2Result == null ? null : other.player2Set2Result.copy();
        this.player1Set3Result = other.player1Set3Result == null ? null : other.player1Set3Result.copy();
        this.player2Set3Result = other.player2Set3Result == null ? null : other.player2Set3Result.copy();
        this.roundId = other.roundId == null ? null : other.roundId.copy();
        this.visitorPlayerId = other.visitorPlayerId == null ? null : other.visitorPlayerId.copy();
        this.localPlayerId = other.localPlayerId == null ? null : other.localPlayerId.copy();
    }

    @Override
    public MatchCriteria copy() {
        return new MatchCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPlayer1Set1Result() {
        return player1Set1Result;
    }

    public void setPlayer1Set1Result(IntegerFilter player1Set1Result) {
        this.player1Set1Result = player1Set1Result;
    }

    public IntegerFilter getPlayer2Set1Result() {
        return player2Set1Result;
    }

    public void setPlayer2Set1Result(IntegerFilter player2Set1Result) {
        this.player2Set1Result = player2Set1Result;
    }

    public IntegerFilter getPlayer1Set2Result() {
        return player1Set2Result;
    }

    public void setPlayer1Set2Result(IntegerFilter player1Set2Result) {
        this.player1Set2Result = player1Set2Result;
    }

    public IntegerFilter getPlayer2Set2Result() {
        return player2Set2Result;
    }

    public void setPlayer2Set2Result(IntegerFilter player2Set2Result) {
        this.player2Set2Result = player2Set2Result;
    }

    public IntegerFilter getPlayer1Set3Result() {
        return player1Set3Result;
    }

    public void setPlayer1Set3Result(IntegerFilter player1Set3Result) {
        this.player1Set3Result = player1Set3Result;
    }

    public IntegerFilter getPlayer2Set3Result() {
        return player2Set3Result;
    }

    public void setPlayer2Set3Result(IntegerFilter player2Set3Result) {
        this.player2Set3Result = player2Set3Result;
    }

    public LongFilter getRoundId() {
        return roundId;
    }

    public void setRoundId(LongFilter roundId) {
        this.roundId = roundId;
    }

    public LongFilter getVisitorPlayerId() {
        return visitorPlayerId;
    }

    public void setVisitorPlayerId(LongFilter visitorPlayerId) {
        this.visitorPlayerId = visitorPlayerId;
    }

    public LongFilter getLocalPlayerId() {
        return localPlayerId;
    }

    public void setLocalPlayerId(LongFilter localPlayerId) {
        this.localPlayerId = localPlayerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MatchCriteria that = (MatchCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(player1Set1Result, that.player1Set1Result) &&
            Objects.equals(player2Set1Result, that.player2Set1Result) &&
            Objects.equals(player1Set2Result, that.player1Set2Result) &&
            Objects.equals(player2Set2Result, that.player2Set2Result) &&
            Objects.equals(player1Set3Result, that.player1Set3Result) &&
            Objects.equals(player2Set3Result, that.player2Set3Result) &&
            Objects.equals(roundId, that.roundId) &&
            Objects.equals(visitorPlayerId, that.visitorPlayerId) &&
            Objects.equals(localPlayerId, that.localPlayerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        player1Set1Result,
        player2Set1Result,
        player1Set2Result,
        player2Set2Result,
        player1Set3Result,
        player2Set3Result,
        roundId,
        visitorPlayerId,
        localPlayerId
        );
    }

    @Override
    public String toString() {
        return "MatchCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (player1Set1Result != null ? "player1Set1Result=" + player1Set1Result + ", " : "") +
                (player2Set1Result != null ? "player2Set1Result=" + player2Set1Result + ", " : "") +
                (player1Set2Result != null ? "player1Set2Result=" + player1Set2Result + ", " : "") +
                (player2Set2Result != null ? "player2Set2Result=" + player2Set2Result + ", " : "") +
                (player1Set3Result != null ? "player1Set3Result=" + player1Set3Result + ", " : "") +
                (player2Set3Result != null ? "player2Set3Result=" + player2Set3Result + ", " : "") +
                (roundId != null ? "roundId=" + roundId + ", " : "") +
                (visitorPlayerId != null ? "visitorPlayerId=" + visitorPlayerId + ", " : "") +
                (localPlayerId != null ? "localPlayerId=" + localPlayerId + ", " : "") +
            "}";
    }

}
