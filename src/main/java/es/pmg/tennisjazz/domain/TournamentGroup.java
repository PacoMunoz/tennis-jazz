package es.pmg.tennisjazz.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TournamentGroup.
 */
@Entity
@Table(name = "tournament_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TournamentGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("tournamentGroups")
    private Tournament tournament;

    @OneToMany(mappedBy = "tournamentGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Round> rounds = new HashSet<>();

    @OneToMany(mappedBy = "tournamentGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ranking> rankings = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "tournament_group_players",
               joinColumns = @JoinColumn(name = "tournament_group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "players_id", referencedColumnName = "id"))
    private Set<Player> players = new HashSet<>();

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

    public TournamentGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public TournamentGroup tournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Set<Round> getRounds() {
        return rounds;
    }

    public TournamentGroup rounds(Set<Round> rounds) {
        this.rounds = rounds;
        return this;
    }

    public TournamentGroup addRounds(Round round) {
        this.rounds.add(round);
        round.setTournamentGroup(this);
        return this;
    }

    public TournamentGroup removeRounds(Round round) {
        this.rounds.remove(round);
        round.setTournamentGroup(null);
        return this;
    }

    public void setRounds(Set<Round> rounds) {
        this.rounds = rounds;
    }

    public Set<Ranking> getRankings() {
        return rankings;
    }

    public TournamentGroup rankings(Set<Ranking> rankings) {
        this.rankings = rankings;
        return this;
    }

    public TournamentGroup addRankings(Ranking ranking) {
        this.rankings.add(ranking);
        ranking.setTournamentGroup(this);
        return this;
    }

    public TournamentGroup removeRankings(Ranking ranking) {
        this.rankings.remove(ranking);
        ranking.setTournamentGroup(null);
        return this;
    }

    public void setRankings(Set<Ranking> rankings) {
        this.rankings = rankings;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public TournamentGroup players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public TournamentGroup addPlayers(Player player) {
        this.players.add(player);
        player.getGroups().add(this);
        return this;
    }

    public TournamentGroup removePlayers(Player player) {
        this.players.remove(player);
        player.getGroups().remove(this);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TournamentGroup)) {
            return false;
        }
        return id != null && id.equals(((TournamentGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TournamentGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
