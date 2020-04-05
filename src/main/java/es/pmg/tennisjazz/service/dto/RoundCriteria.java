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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link es.pmg.tennisjazz.domain.Round} entity. This class is used
 * in {@link es.pmg.tennisjazz.web.rest.RoundResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rounds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RoundCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private LongFilter tournamentGroupId;

    private LongFilter matchesId;

    public RoundCriteria(){
    }

    public RoundCriteria(RoundCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.tournamentGroupId = other.tournamentGroupId == null ? null : other.tournamentGroupId.copy();
        this.matchesId = other.matchesId == null ? null : other.matchesId.copy();
    }

    @Override
    public RoundCriteria copy() {
        return new RoundCriteria(this);
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

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getTournamentGroupId() {
        return tournamentGroupId;
    }

    public void setTournamentGroupId(LongFilter tournamentGroupId) {
        this.tournamentGroupId = tournamentGroupId;
    }

    public LongFilter getMatchesId() {
        return matchesId;
    }

    public void setMatchesId(LongFilter matchesId) {
        this.matchesId = matchesId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RoundCriteria that = (RoundCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(tournamentGroupId, that.tournamentGroupId) &&
            Objects.equals(matchesId, that.matchesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        startDate,
        endDate,
        tournamentGroupId,
        matchesId
        );
    }

    @Override
    public String toString() {
        return "RoundCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (tournamentGroupId != null ? "tournamentGroupId=" + tournamentGroupId + ", " : "") +
                (matchesId != null ? "matchesId=" + matchesId + ", " : "") +
            "}";
    }

}
