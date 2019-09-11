package es.pmg.tennisjazz.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tournament.
 */
@Entity
@Table(name = "tournament")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tournament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "in_progress")
    private Boolean inProgress;

    @NotNull
    @Column(name = "win_points", nullable = false)
    private Integer winPoints;

    @NotNull
    @Column(name = "loss_points", nullable = false)
    private Integer lossPoints;

    @Column(name = "not_present_points")
    private Integer notPresentPoints;

    @Column(name = "injured_points")
    private Integer injuredPoints;

    @OneToMany(mappedBy = "tournament")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TournamentGroup> groups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tournament name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Tournament startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Tournament endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean isInProgress() {
        return inProgress;
    }

    public Tournament inProgress(Boolean inProgress) {
        this.inProgress = inProgress;
        return this;
    }

    public void setInProgress(Boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getWinPoints() {
        return winPoints;
    }

    public Tournament winPoints(Integer winPoints) {
        this.winPoints = winPoints;
        return this;
    }

    public void setWinPoints(Integer winPoints) {
        this.winPoints = winPoints;
    }

    public Integer getLossPoints() {
        return lossPoints;
    }

    public Tournament lossPoints(Integer lossPoints) {
        this.lossPoints = lossPoints;
        return this;
    }

    public void setLossPoints(Integer lossPoints) {
        this.lossPoints = lossPoints;
    }

    public Integer getNotPresentPoints() {
        return notPresentPoints;
    }

    public Tournament notPresentPoints(Integer notPresentPoints) {
        this.notPresentPoints = notPresentPoints;
        return this;
    }

    public void setNotPresentPoints(Integer notPresentPoints) {
        this.notPresentPoints = notPresentPoints;
    }

    public Integer getInjuredPoints() {
        return injuredPoints;
    }

    public Tournament injuredPoints(Integer injuredPoints) {
        this.injuredPoints = injuredPoints;
        return this;
    }

    public void setInjuredPoints(Integer injuredPoints) {
        this.injuredPoints = injuredPoints;
    }

    public Set<TournamentGroup> getGroups() {
        return groups;
    }

    public Tournament groups(Set<TournamentGroup> tournamentGroups) {
        this.groups = tournamentGroups;
        return this;
    }

    public Tournament addGroups(TournamentGroup tournamentGroup) {
        this.groups.add(tournamentGroup);
        tournamentGroup.setTournament(this);
        return this;
    }

    public Tournament removeGroups(TournamentGroup tournamentGroup) {
        this.groups.remove(tournamentGroup);
        tournamentGroup.setTournament(null);
        return this;
    }

    public void setGroups(Set<TournamentGroup> tournamentGroups) {
        this.groups = tournamentGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tournament)) {
            return false;
        }
        return id != null && id.equals(((Tournament) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tournament{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", inProgress='" + isInProgress() + "'" +
            ", winPoints=" + getWinPoints() +
            ", lossPoints=" + getLossPoints() +
            ", notPresentPoints=" + getNotPresentPoints() +
            ", injuredPoints=" + getInjuredPoints() +
            "}";
    }
}
