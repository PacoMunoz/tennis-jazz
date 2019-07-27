package es.pmg.tennisjazz.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Match.
 */
@Entity
@Table(name = "jhi_match")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_1_set_1_result")
    private Integer player1Set1Result;

    @Column(name = "player_2_set_1_result")
    private Integer player2Set1Result;

    @Column(name = "player_1_set_2_result")
    private Integer player1Set2Result;

    @Column(name = "player_2_set_2_result")
    private Integer player2Set2Result;

    @Column(name = "player_1_set_3_result")
    private Integer player1Set3Result;

    @Column(name = "player_2_set_3_result")
    private Integer player2Set3Result;

    @Column(name = "local_player_sets")
    private Integer localPlayerSets;

    @Column(name = "visitor_player_sets")
    private Integer visitorPlayerSets;

    @Column(name = "local_player_abandoned")
    private Boolean localPlayerAbandoned;

    @Column(name = "visitor_player_abandoned")
    private Boolean visitorPlayerAbandoned;

    @Column(name = "local_player_not_present")
    private Boolean localPlayerNotPresent;

    @Column(name = "visitor_player_not_present")
    private Boolean visitorPlayerNotPresent;

    @ManyToOne
    @JsonIgnoreProperties("matches")
    private Round round;

    @ManyToOne
    @JsonIgnoreProperties("visitorMatches")
    private Player visitorPlayer;

    @ManyToOne
    @JsonIgnoreProperties("localMatches")
    private Player localPlayer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayer1Set1Result() {
        return player1Set1Result;
    }

    public Match player1Set1Result(Integer player1Set1Result) {
        this.player1Set1Result = player1Set1Result;
        return this;
    }

    public void setPlayer1Set1Result(Integer player1Set1Result) {
        this.player1Set1Result = player1Set1Result;
    }

    public Integer getPlayer2Set1Result() {
        return player2Set1Result;
    }

    public Match player2Set1Result(Integer player2Set1Result) {
        this.player2Set1Result = player2Set1Result;
        return this;
    }

    public void setPlayer2Set1Result(Integer player2Set1Result) {
        this.player2Set1Result = player2Set1Result;
    }

    public Integer getPlayer1Set2Result() {
        return player1Set2Result;
    }

    public Match player1Set2Result(Integer player1Set2Result) {
        this.player1Set2Result = player1Set2Result;
        return this;
    }

    public void setPlayer1Set2Result(Integer player1Set2Result) {
        this.player1Set2Result = player1Set2Result;
    }

    public Integer getPlayer2Set2Result() {
        return player2Set2Result;
    }

    public Match player2Set2Result(Integer player2Set2Result) {
        this.player2Set2Result = player2Set2Result;
        return this;
    }

    public void setPlayer2Set2Result(Integer player2Set2Result) {
        this.player2Set2Result = player2Set2Result;
    }

    public Integer getPlayer1Set3Result() {
        return player1Set3Result;
    }

    public Match player1Set3Result(Integer player1Set3Result) {
        this.player1Set3Result = player1Set3Result;
        return this;
    }

    public void setPlayer1Set3Result(Integer player1Set3Result) {
        this.player1Set3Result = player1Set3Result;
    }

    public Integer getPlayer2Set3Result() {
        return player2Set3Result;
    }

    public Match player2Set3Result(Integer player2Set3Result) {
        this.player2Set3Result = player2Set3Result;
        return this;
    }

    public void setPlayer2Set3Result(Integer player2Set3Result) {
        this.player2Set3Result = player2Set3Result;
    }

    public Integer getLocalPlayerSets() {
        return localPlayerSets;
    }

    public Match localPlayerSets(Integer localPlayerSets) {
        this.localPlayerSets = localPlayerSets;
        return this;
    }

    public void setLocalPlayerSets(Integer localPlayerSets) {
        this.localPlayerSets = localPlayerSets;
    }

    public Integer getVisitorPlayerSets() {
        return visitorPlayerSets;
    }

    public Match visitorPlayerSets(Integer visitorPlayerSets) {
        this.visitorPlayerSets = visitorPlayerSets;
        return this;
    }

    public void setVisitorPlayerSets(Integer visitorPlayerSets) {
        this.visitorPlayerSets = visitorPlayerSets;
    }

    public Boolean isLocalPlayerAbandoned() {
        return localPlayerAbandoned;
    }

    public Match localPlayerAbandoned(Boolean localPlayerAbandoned) {
        this.localPlayerAbandoned = localPlayerAbandoned;
        return this;
    }

    public void setLocalPlayerAbandoned(Boolean localPlayerAbandoned) {
        this.localPlayerAbandoned = localPlayerAbandoned;
    }

    public Boolean isVisitorPlayerAbandoned() {
        return visitorPlayerAbandoned;
    }

    public Match visitorPlayerAbandoned(Boolean visitorPlayerAbandoned) {
        this.visitorPlayerAbandoned = visitorPlayerAbandoned;
        return this;
    }

    public void setVisitorPlayerAbandoned(Boolean visitorPlayerAbandoned) {
        this.visitorPlayerAbandoned = visitorPlayerAbandoned;
    }

    public Boolean isLocalPlayerNotPresent() {
        return localPlayerNotPresent;
    }

    public Match localPlayerNotPresent(Boolean localPlayerNotPresent) {
        this.localPlayerNotPresent = localPlayerNotPresent;
        return this;
    }

    public void setLocalPlayerNotPresent(Boolean localPlayerNotPresent) {
        this.localPlayerNotPresent = localPlayerNotPresent;
    }

    public Boolean isVisitorPlayerNotPresent() {
        return visitorPlayerNotPresent;
    }

    public Match visitorPlayerNotPresent(Boolean visitorPlayerNotPresent) {
        this.visitorPlayerNotPresent = visitorPlayerNotPresent;
        return this;
    }

    public void setVisitorPlayerNotPresent(Boolean visitorPlayerNotPresent) {
        this.visitorPlayerNotPresent = visitorPlayerNotPresent;
    }

    public Round getRound() {
        return round;
    }

    public Match round(Round round) {
        this.round = round;
        return this;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Player getVisitorPlayer() {
        return visitorPlayer;
    }

    public Match visitorPlayer(Player player) {
        this.visitorPlayer = player;
        return this;
    }

    public void setVisitorPlayer(Player player) {
        this.visitorPlayer = player;
    }

    public Player getLocalPlayer() {
        return localPlayer;
    }

    public Match localPlayer(Player player) {
        this.localPlayer = player;
        return this;
    }

    public void setLocalPlayer(Player player) {
        this.localPlayer = player;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        return id != null && id.equals(((Match) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Match{" +
            "id=" + getId() +
            ", player1Set1Result=" + getPlayer1Set1Result() +
            ", player2Set1Result=" + getPlayer2Set1Result() +
            ", player1Set2Result=" + getPlayer1Set2Result() +
            ", player2Set2Result=" + getPlayer2Set2Result() +
            ", player1Set3Result=" + getPlayer1Set3Result() +
            ", player2Set3Result=" + getPlayer2Set3Result() +
            ", localPlayerSets=" + getLocalPlayerSets() +
            ", visitorPlayerSets=" + getVisitorPlayerSets() +
            ", localPlayerAbandoned='" + isLocalPlayerAbandoned() + "'" +
            ", visitorPlayerAbandoned='" + isVisitorPlayerAbandoned() + "'" +
            ", localPlayerNotPresent='" + isLocalPlayerNotPresent() + "'" +
            ", visitorPlayerNotPresent='" + isVisitorPlayerNotPresent() + "'" +
            "}";
    }
}
