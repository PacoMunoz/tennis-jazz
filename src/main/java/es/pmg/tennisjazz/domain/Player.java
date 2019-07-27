package es.pmg.tennisjazz.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "other")
    private String other;

    @OneToMany(mappedBy = "visitorPlayer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Match> visitorMatches = new HashSet<>();

    @OneToMany(mappedBy = "localPlayer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Match> localMatches = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ranking> rankings = new HashSet<>();

    @ManyToMany(mappedBy = "players")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
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

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public Player surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public Player email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public Player phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOther() {
        return other;
    }

    public Player other(String other) {
        this.other = other;
        return this;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Set<Match> getVisitorMatches() {
        return visitorMatches;
    }

    public Player visitorMatches(Set<Match> matches) {
        this.visitorMatches = matches;
        return this;
    }

    public Player addVisitorMatches(Match match) {
        this.visitorMatches.add(match);
        match.setVisitorPlayer(this);
        return this;
    }

    public Player removeVisitorMatches(Match match) {
        this.visitorMatches.remove(match);
        match.setVisitorPlayer(null);
        return this;
    }

    public void setVisitorMatches(Set<Match> matches) {
        this.visitorMatches = matches;
    }

    public Set<Match> getLocalMatches() {
        return localMatches;
    }

    public Player localMatches(Set<Match> matches) {
        this.localMatches = matches;
        return this;
    }

    public Player addLocalMatches(Match match) {
        this.localMatches.add(match);
        match.setLocalPlayer(this);
        return this;
    }

    public Player removeLocalMatches(Match match) {
        this.localMatches.remove(match);
        match.setLocalPlayer(null);
        return this;
    }

    public void setLocalMatches(Set<Match> matches) {
        this.localMatches = matches;
    }

    public Set<Ranking> getRankings() {
        return rankings;
    }

    public Player rankings(Set<Ranking> rankings) {
        this.rankings = rankings;
        return this;
    }

    public Player addRankings(Ranking ranking) {
        this.rankings.add(ranking);
        ranking.setPlayer(this);
        return this;
    }

    public Player removeRankings(Ranking ranking) {
        this.rankings.remove(ranking);
        ranking.setPlayer(null);
        return this;
    }

    public void setRankings(Set<Ranking> rankings) {
        this.rankings = rankings;
    }

    public Set<TournamentGroup> getGroups() {
        return groups;
    }

    public Player groups(Set<TournamentGroup> tournamentGroups) {
        this.groups = tournamentGroups;
        return this;
    }

    public Player addGroups(TournamentGroup tournamentGroup) {
        this.groups.add(tournamentGroup);
        tournamentGroup.getPlayers().add(this);
        return this;
    }

    public Player removeGroups(TournamentGroup tournamentGroup) {
        this.groups.remove(tournamentGroup);
        tournamentGroup.getPlayers().remove(this);
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
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", other='" + getOther() + "'" +
            "}";
    }
}
