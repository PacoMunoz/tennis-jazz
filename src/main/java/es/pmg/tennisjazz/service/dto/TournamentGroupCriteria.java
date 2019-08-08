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
 * Criteria class for the {@link es.pmg.tennisjazz.domain.TournamentGroup} entity. This class is used
 * in {@link es.pmg.tennisjazz.web.rest.TournamentGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tournament-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TournamentGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter tournamentId;

    private LongFilter roundsId;

    private LongFilter rankingsId;

    private LongFilter playersId;

    public TournamentGroupCriteria(){
    }

    public TournamentGroupCriteria(TournamentGroupCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.roundsId = other.roundsId == null ? null : other.roundsId.copy();
        this.rankingsId = other.rankingsId == null ? null : other.rankingsId.copy();
        this.playersId = other.playersId == null ? null : other.playersId.copy();
    }

    @Override
    public TournamentGroupCriteria copy() {
        return new TournamentGroupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    public LongFilter getRoundsId() {
        return roundsId;
    }

    public void setRoundsId(LongFilter roundsId) {
        this.roundsId = roundsId;
    }

    public LongFilter getRankingsId() {
        return rankingsId;
    }

    public void setRankingsId(LongFilter rankingsId) {
        this.rankingsId = rankingsId;
    }

    public LongFilter getPlayersId() {
        return playersId;
    }

    public void setPlayersId(LongFilter playersId) {
        this.playersId = playersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TournamentGroupCriteria that = (TournamentGroupCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(roundsId, that.roundsId) &&
            Objects.equals(rankingsId, that.rankingsId) &&
            Objects.equals(playersId, that.playersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        tournamentId,
        roundsId,
        rankingsId,
        playersId
        );
    }

    @Override
    public String toString() {
        return "TournamentGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
                (roundsId != null ? "roundsId=" + roundsId + ", " : "") +
                (rankingsId != null ? "rankingsId=" + rankingsId + ", " : "") +
                (playersId != null ? "playersId=" + playersId + ", " : "") +
            "}";
    }

}
