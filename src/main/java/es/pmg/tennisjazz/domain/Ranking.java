package es.pmg.tennisjazz.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Ranking.
 */
@Entity
@Table(name = "ranking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ranking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points")
    private Integer points;

    @Column(name = "games_won")
    private Integer gamesWon;

    @Column(name = "games_loss")
    private Integer gamesLoss;

    @Column(name = "sets_won")
    private Integer setsWon;

    @Column(name = "sets_loss")
    private Integer setsLoss;

    @Column(name = "matches_played")
    private Integer matchesPlayed;

    @Column(name = "matches_won")
    private Integer matchesWon;

    @Column(name = "matches_loss")
    private Integer matchesLoss;

    @Column(name = "matches_not_present")
    private Integer matchesNotPresent;

    @Column(name = "matches_abandoned")
    private Integer matchesAbandoned;

    @Column(name = "tie_breaks_played")
    private Integer tieBreaksPlayed;

    @Column(name = "tie_breaks_won")
    private Integer tieBreaksWon;

    @ManyToOne
    @JsonIgnoreProperties("rankings")
    private TournamentGroup tournamentGroup;

    @ManyToOne
    @JsonIgnoreProperties("rankings")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public Ranking points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public Ranking gamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
        return this;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesLoss() {
        return gamesLoss;
    }

    public Ranking gamesLoss(Integer gamesLoss) {
        this.gamesLoss = gamesLoss;
        return this;
    }

    public void setGamesLoss(Integer gamesLoss) {
        this.gamesLoss = gamesLoss;
    }

    public Integer getSetsWon() {
        return setsWon;
    }

    public Ranking setsWon(Integer setsWon) {
        this.setsWon = setsWon;
        return this;
    }

    public void setSetsWon(Integer setsWon) {
        this.setsWon = setsWon;
    }

    public Integer getSetsLoss() {
        return setsLoss;
    }

    public Ranking setsLoss(Integer setsLoss) {
        this.setsLoss = setsLoss;
        return this;
    }

    public void setSetsLoss(Integer setsLoss) {
        this.setsLoss = setsLoss;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public Ranking matchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
        return this;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public Ranking matchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
        return this;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getMatchesLoss() {
        return matchesLoss;
    }

    public Ranking matchesLoss(Integer matchesLoss) {
        this.matchesLoss = matchesLoss;
        return this;
    }

    public void setMatchesLoss(Integer matchesLoss) {
        this.matchesLoss = matchesLoss;
    }

    public Integer getMatchesNotPresent() {
        return matchesNotPresent;
    }

    public Ranking matchesNotPresent(Integer matchesNotPresent) {
        this.matchesNotPresent = matchesNotPresent;
        return this;
    }

    public void setMatchesNotPresent(Integer matchesNotPresent) {
        this.matchesNotPresent = matchesNotPresent;
    }

    public Integer getMatchesAbandoned() {
        return matchesAbandoned;
    }

    public Ranking matchesAbandoned(Integer matchesAbandoned) {
        this.matchesAbandoned = matchesAbandoned;
        return this;
    }

    public void setMatchesAbandoned(Integer matchesAbandoned) {
        this.matchesAbandoned = matchesAbandoned;
    }

    public Integer getTieBreaksPlayed() {
        return tieBreaksPlayed;
    }

    public Ranking tieBreaksPlayed(Integer tieBreaksPlayed) {
        this.tieBreaksPlayed = tieBreaksPlayed;
        return this;
    }

    public void setTieBreaksPlayed(Integer tieBreaksPlayed) {
        this.tieBreaksPlayed = tieBreaksPlayed;
    }

    public Integer getTieBreaksWon() {
        return tieBreaksWon;
    }

    public Ranking tieBreaksWon(Integer tieBreaksWon) {
        this.tieBreaksWon = tieBreaksWon;
        return this;
    }

    public void setTieBreaksWon(Integer tieBreaksWon) {
        this.tieBreaksWon = tieBreaksWon;
    }

    public TournamentGroup getTournamentGroup() {
        return tournamentGroup;
    }

    public Ranking tournamentGroup(TournamentGroup tournamentGroup) {
        this.tournamentGroup = tournamentGroup;
        return this;
    }

    public void setTournamentGroup(TournamentGroup tournamentGroup) {
        this.tournamentGroup = tournamentGroup;
    }

    public Player getPlayer() {
        return player;
    }

    public Ranking player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ranking)) {
            return false;
        }
        return id != null && id.equals(((Ranking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ranking{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", gamesWon=" + getGamesWon() +
            ", gamesLoss=" + getGamesLoss() +
            ", setsWon=" + getSetsWon() +
            ", setsLoss=" + getSetsLoss() +
            ", matchesPlayed=" + getMatchesPlayed() +
            ", matchesWon=" + getMatchesWon() +
            ", matchesLoss=" + getMatchesLoss() +
            ", matchesNotPresent=" + getMatchesNotPresent() +
            ", matchesAbandoned=" + getMatchesAbandoned() +
            ", tieBreaksPlayed=" + getTieBreaksPlayed() +
            ", tieBreaksWon=" + getTieBreaksWon() +
            "}";
    }
}
