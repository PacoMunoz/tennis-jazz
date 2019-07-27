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

    @Column(name = "games_win")
    private Integer gamesWin;

    @Column(name = "games_loss")
    private Integer gamesLoss;

    @Column(name = "sets_win")
    private Integer setsWin;

    @Column(name = "sets_lost")
    private Integer setsLost;

    @Column(name = "matches_played")
    private Integer matchesPlayed;

    @Column(name = "matches_wined")
    private Integer matchesWined;

    @Column(name = "matches_loss")
    private Integer matchesLoss;

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

    public Integer getGamesWin() {
        return gamesWin;
    }

    public Ranking gamesWin(Integer gamesWin) {
        this.gamesWin = gamesWin;
        return this;
    }

    public void setGamesWin(Integer gamesWin) {
        this.gamesWin = gamesWin;
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

    public Integer getSetsWin() {
        return setsWin;
    }

    public Ranking setsWin(Integer setsWin) {
        this.setsWin = setsWin;
        return this;
    }

    public void setSetsWin(Integer setsWin) {
        this.setsWin = setsWin;
    }

    public Integer getSetsLost() {
        return setsLost;
    }

    public Ranking setsLost(Integer setsLost) {
        this.setsLost = setsLost;
        return this;
    }

    public void setSetsLost(Integer setsLost) {
        this.setsLost = setsLost;
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

    public Integer getMatchesWined() {
        return matchesWined;
    }

    public Ranking matchesWined(Integer matchesWined) {
        this.matchesWined = matchesWined;
        return this;
    }

    public void setMatchesWined(Integer matchesWined) {
        this.matchesWined = matchesWined;
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
            ", gamesWin=" + getGamesWin() +
            ", gamesLoss=" + getGamesLoss() +
            ", setsWin=" + getSetsWin() +
            ", setsLost=" + getSetsLost() +
            ", matchesPlayed=" + getMatchesPlayed() +
            ", matchesWined=" + getMatchesWined() +
            ", matchesLoss=" + getMatchesLoss() +
            "}";
    }
}
