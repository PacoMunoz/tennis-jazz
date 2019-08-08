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

    private IntegerFilter localPlayerSet1Result;

    private IntegerFilter visitorPlayerSet1Result;

    private IntegerFilter localPlayerSet2Result;

    private IntegerFilter visitorPlayerSet2Result;

    private IntegerFilter localPlayerSet3Result;

    private IntegerFilter visitorPlayerSet3Result;

    private IntegerFilter localPlayerSets;

    private IntegerFilter visitorPlayerSets;

    private BooleanFilter localPlayerAbandoned;

    private BooleanFilter visitorPlayerAbandoned;

    private BooleanFilter localPlayerNotPresent;

    private BooleanFilter visitorPlayerNotPresent;

    private LongFilter roundId;

    private LongFilter visitorPlayerId;

    private LongFilter localPlayerId;

    public MatchCriteria(){
    }

    public MatchCriteria(MatchCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.localPlayerSet1Result = other.localPlayerSet1Result == null ? null : other.localPlayerSet1Result.copy();
        this.visitorPlayerSet1Result = other.visitorPlayerSet1Result == null ? null : other.visitorPlayerSet1Result.copy();
        this.localPlayerSet2Result = other.localPlayerSet2Result == null ? null : other.localPlayerSet2Result.copy();
        this.visitorPlayerSet2Result = other.visitorPlayerSet2Result == null ? null : other.visitorPlayerSet2Result.copy();
        this.localPlayerSet3Result = other.localPlayerSet3Result == null ? null : other.localPlayerSet3Result.copy();
        this.visitorPlayerSet3Result = other.visitorPlayerSet3Result == null ? null : other.visitorPlayerSet3Result.copy();
        this.localPlayerSets = other.localPlayerSets == null ? null : other.localPlayerSets.copy();
        this.visitorPlayerSets = other.visitorPlayerSets == null ? null : other.visitorPlayerSets.copy();
        this.localPlayerAbandoned = other.localPlayerAbandoned == null ? null : other.localPlayerAbandoned.copy();
        this.visitorPlayerAbandoned = other.visitorPlayerAbandoned == null ? null : other.visitorPlayerAbandoned.copy();
        this.localPlayerNotPresent = other.localPlayerNotPresent == null ? null : other.localPlayerNotPresent.copy();
        this.visitorPlayerNotPresent = other.visitorPlayerNotPresent == null ? null : other.visitorPlayerNotPresent.copy();
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

    public IntegerFilter getLocalPlayerSet1Result() {
        return localPlayerSet1Result;
    }

    public void setLocalPlayerSet1Result(IntegerFilter localPlayerSet1Result) {
        this.localPlayerSet1Result = localPlayerSet1Result;
    }

    public IntegerFilter getVisitorPlayerSet1Result() {
        return visitorPlayerSet1Result;
    }

    public void setVisitorPlayerSet1Result(IntegerFilter visitorPlayerSet1Result) {
        this.visitorPlayerSet1Result = visitorPlayerSet1Result;
    }

    public IntegerFilter getLocalPlayerSet2Result() {
        return localPlayerSet2Result;
    }

    public void setLocalPlayerSet2Result(IntegerFilter localPlayerSet2Result) {
        this.localPlayerSet2Result = localPlayerSet2Result;
    }

    public IntegerFilter getVisitorPlayerSet2Result() {
        return visitorPlayerSet2Result;
    }

    public void setVisitorPlayerSet2Result(IntegerFilter visitorPlayerSet2Result) {
        this.visitorPlayerSet2Result = visitorPlayerSet2Result;
    }

    public IntegerFilter getLocalPlayerSet3Result() {
        return localPlayerSet3Result;
    }

    public void setLocalPlayerSet3Result(IntegerFilter localPlayerSet3Result) {
        this.localPlayerSet3Result = localPlayerSet3Result;
    }

    public IntegerFilter getVisitorPlayerSet3Result() {
        return visitorPlayerSet3Result;
    }

    public void setVisitorPlayerSet3Result(IntegerFilter visitorPlayerSet3Result) {
        this.visitorPlayerSet3Result = visitorPlayerSet3Result;
    }

    public IntegerFilter getLocalPlayerSets() {
        return localPlayerSets;
    }

    public void setLocalPlayerSets(IntegerFilter localPlayerSets) {
        this.localPlayerSets = localPlayerSets;
    }

    public IntegerFilter getVisitorPlayerSets() {
        return visitorPlayerSets;
    }

    public void setVisitorPlayerSets(IntegerFilter visitorPlayerSets) {
        this.visitorPlayerSets = visitorPlayerSets;
    }

    public BooleanFilter getLocalPlayerAbandoned() {
        return localPlayerAbandoned;
    }

    public void setLocalPlayerAbandoned(BooleanFilter localPlayerAbandoned) {
        this.localPlayerAbandoned = localPlayerAbandoned;
    }

    public BooleanFilter getVisitorPlayerAbandoned() {
        return visitorPlayerAbandoned;
    }

    public void setVisitorPlayerAbandoned(BooleanFilter visitorPlayerAbandoned) {
        this.visitorPlayerAbandoned = visitorPlayerAbandoned;
    }

    public BooleanFilter getLocalPlayerNotPresent() {
        return localPlayerNotPresent;
    }

    public void setLocalPlayerNotPresent(BooleanFilter localPlayerNotPresent) {
        this.localPlayerNotPresent = localPlayerNotPresent;
    }

    public BooleanFilter getVisitorPlayerNotPresent() {
        return visitorPlayerNotPresent;
    }

    public void setVisitorPlayerNotPresent(BooleanFilter visitorPlayerNotPresent) {
        this.visitorPlayerNotPresent = visitorPlayerNotPresent;
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
            Objects.equals(localPlayerSet1Result, that.localPlayerSet1Result) &&
            Objects.equals(visitorPlayerSet1Result, that.visitorPlayerSet1Result) &&
            Objects.equals(localPlayerSet2Result, that.localPlayerSet2Result) &&
            Objects.equals(visitorPlayerSet2Result, that.visitorPlayerSet2Result) &&
            Objects.equals(localPlayerSet3Result, that.localPlayerSet3Result) &&
            Objects.equals(visitorPlayerSet3Result, that.visitorPlayerSet3Result) &&
            Objects.equals(localPlayerSets, that.localPlayerSets) &&
            Objects.equals(visitorPlayerSets, that.visitorPlayerSets) &&
            Objects.equals(localPlayerAbandoned, that.localPlayerAbandoned) &&
            Objects.equals(visitorPlayerAbandoned, that.visitorPlayerAbandoned) &&
            Objects.equals(localPlayerNotPresent, that.localPlayerNotPresent) &&
            Objects.equals(visitorPlayerNotPresent, that.visitorPlayerNotPresent) &&
            Objects.equals(roundId, that.roundId) &&
            Objects.equals(visitorPlayerId, that.visitorPlayerId) &&
            Objects.equals(localPlayerId, that.localPlayerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        localPlayerSet1Result,
        visitorPlayerSet1Result,
        localPlayerSet2Result,
        visitorPlayerSet2Result,
        localPlayerSet3Result,
        visitorPlayerSet3Result,
        localPlayerSets,
        visitorPlayerSets,
        localPlayerAbandoned,
        visitorPlayerAbandoned,
        localPlayerNotPresent,
        visitorPlayerNotPresent,
        roundId,
        visitorPlayerId,
        localPlayerId
        );
    }

    @Override
    public String toString() {
        return "MatchCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (localPlayerSet1Result != null ? "localPlayerSet1Result=" + localPlayerSet1Result + ", " : "") +
                (visitorPlayerSet1Result != null ? "visitorPlayerSet1Result=" + visitorPlayerSet1Result + ", " : "") +
                (localPlayerSet2Result != null ? "localPlayerSet2Result=" + localPlayerSet2Result + ", " : "") +
                (visitorPlayerSet2Result != null ? "visitorPlayerSet2Result=" + visitorPlayerSet2Result + ", " : "") +
                (localPlayerSet3Result != null ? "localPlayerSet3Result=" + localPlayerSet3Result + ", " : "") +
                (visitorPlayerSet3Result != null ? "visitorPlayerSet3Result=" + visitorPlayerSet3Result + ", " : "") +
                (localPlayerSets != null ? "localPlayerSets=" + localPlayerSets + ", " : "") +
                (visitorPlayerSets != null ? "visitorPlayerSets=" + visitorPlayerSets + ", " : "") +
                (localPlayerAbandoned != null ? "localPlayerAbandoned=" + localPlayerAbandoned + ", " : "") +
                (visitorPlayerAbandoned != null ? "visitorPlayerAbandoned=" + visitorPlayerAbandoned + ", " : "") +
                (localPlayerNotPresent != null ? "localPlayerNotPresent=" + localPlayerNotPresent + ", " : "") +
                (visitorPlayerNotPresent != null ? "visitorPlayerNotPresent=" + visitorPlayerNotPresent + ", " : "") +
                (roundId != null ? "roundId=" + roundId + ", " : "") +
                (visitorPlayerId != null ? "visitorPlayerId=" + visitorPlayerId + ", " : "") +
                (localPlayerId != null ? "localPlayerId=" + localPlayerId + ", " : "") +
            "}";
    }

}
