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
 * Criteria class for the {@link es.pmg.tennisjazz.domain.Tournament} entity. This class is used
 * in {@link es.pmg.tennisjazz.web.rest.TournamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tournaments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TournamentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter startDate;

    private BooleanFilter inProgress;

    private IntegerFilter winPoints;

    private IntegerFilter lossPoints;

    private IntegerFilter notPresentPoints;

    private LongFilter groupsId;

    public TournamentCriteria(){
    }

    public TournamentCriteria(TournamentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.inProgress = other.inProgress == null ? null : other.inProgress.copy();
        this.winPoints = other.winPoints == null ? null : other.winPoints.copy();
        this.lossPoints = other.lossPoints == null ? null : other.lossPoints.copy();
        this.notPresentPoints = other.notPresentPoints == null ? null : other.notPresentPoints.copy();
        this.groupsId = other.groupsId == null ? null : other.groupsId.copy();
    }

    @Override
    public TournamentCriteria copy() {
        return new TournamentCriteria(this);
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

    public BooleanFilter getInProgress() {
        return inProgress;
    }

    public void setInProgress(BooleanFilter inProgress) {
        this.inProgress = inProgress;
    }

    public IntegerFilter getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(IntegerFilter winPoints) {
        this.winPoints = winPoints;
    }

    public IntegerFilter getLossPoints() {
        return lossPoints;
    }

    public void setLossPoints(IntegerFilter lossPoints) {
        this.lossPoints = lossPoints;
    }

    public IntegerFilter getNotPresentPoints() {
        return notPresentPoints;
    }

    public void setNotPresentPoints(IntegerFilter notPresentPoints) {
        this.notPresentPoints = notPresentPoints;
    }

    public LongFilter getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(LongFilter groupsId) {
        this.groupsId = groupsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TournamentCriteria that = (TournamentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(inProgress, that.inProgress) &&
            Objects.equals(winPoints, that.winPoints) &&
            Objects.equals(lossPoints, that.lossPoints) &&
            Objects.equals(notPresentPoints, that.notPresentPoints) &&
            Objects.equals(groupsId, that.groupsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        startDate,
        inProgress,
        winPoints,
        lossPoints,
        notPresentPoints,
        groupsId
        );
    }

    @Override
    public String toString() {
        return "TournamentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (inProgress != null ? "inProgress=" + inProgress + ", " : "") +
                (winPoints != null ? "winPoints=" + winPoints + ", " : "") +
                (lossPoints != null ? "lossPoints=" + lossPoints + ", " : "") +
                (notPresentPoints != null ? "notPresentPoints=" + notPresentPoints + ", " : "") +
                (groupsId != null ? "groupsId=" + groupsId + ", " : "") +
            "}";
    }

}
