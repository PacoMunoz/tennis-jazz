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

    @Column(name = "local_player_set_1_result")
    private Integer localPlayerSet1Result;

    @Column(name = "visitor_player_set_1_result")
    private Integer visitorPlayerSet1Result;

    @Column(name = "local_player_set_2_result")
    private Integer localPlayerSet2Result;

    @Column(name = "visitor_player_set_2_result")
    private Integer visitorPlayerSet2Result;

    @Column(name = "local_player_set_3_result")
    private Integer localPlayerSet3Result;

    @Column(name = "visitor_player_set_3_result")
    private Integer visitorPlayerSet3Result;

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

    public Integer getLocalPlayerSet1Result() {
        return localPlayerSet1Result;
    }

    public Match localPlayerSet1Result(Integer localPlayerSet1Result) {
        this.localPlayerSet1Result = localPlayerSet1Result;
        return this;
    }

    public void setLocalPlayerSet1Result(Integer localPlayerSet1Result) {
        this.localPlayerSet1Result = localPlayerSet1Result;
    }

    public Integer getVisitorPlayerSet1Result() {
        return visitorPlayerSet1Result;
    }

    public Match visitorPlayerSet1Result(Integer visitorPlayerSet1Result) {
        this.visitorPlayerSet1Result = visitorPlayerSet1Result;
        return this;
    }

    public void setVisitorPlayerSet1Result(Integer visitorPlayerSet1Result) {
        this.visitorPlayerSet1Result = visitorPlayerSet1Result;
    }

    public Integer getLocalPlayerSet2Result() {
        return localPlayerSet2Result;
    }

    public Match localPlayerSet2Result(Integer localPlayerSet2Result) {
        this.localPlayerSet2Result = localPlayerSet2Result;
        return this;
    }

    public void setLocalPlayerSet2Result(Integer localPlayerSet2Result) {
        this.localPlayerSet2Result = localPlayerSet2Result;
    }

    public Integer getVisitorPlayerSet2Result() {
        return visitorPlayerSet2Result;
    }

    public Match visitorPlayerSet2Result(Integer visitorPlayerSet2Result) {
        this.visitorPlayerSet2Result = visitorPlayerSet2Result;
        return this;
    }

    public void setVisitorPlayerSet2Result(Integer visitorPlayerSet2Result) {
        this.visitorPlayerSet2Result = visitorPlayerSet2Result;
    }

    public Integer getLocalPlayerSet3Result() {
        return localPlayerSet3Result;
    }

    public Match localPlayerSet3Result(Integer localPlayerSet3Result) {
        this.localPlayerSet3Result = localPlayerSet3Result;
        return this;
    }

    public void setLocalPlayerSet3Result(Integer localPlayerSet3Result) {
        this.localPlayerSet3Result = localPlayerSet3Result;
    }

    public Integer getVisitorPlayerSet3Result() {
        return visitorPlayerSet3Result;
    }

    public Match visitorPlayerSet3Result(Integer visitorPlayerSet3Result) {
        this.visitorPlayerSet3Result = visitorPlayerSet3Result;
        return this;
    }

    public void setVisitorPlayerSet3Result(Integer visitorPlayerSet3Result) {
        this.visitorPlayerSet3Result = visitorPlayerSet3Result;
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
            ", localPlayerSet1Result=" + getLocalPlayerSet1Result() +
            ", visitorPlayerSet1Result=" + getVisitorPlayerSet1Result() +
            ", localPlayerSet2Result=" + getLocalPlayerSet2Result() +
            ", visitorPlayerSet2Result=" + getVisitorPlayerSet2Result() +
            ", localPlayerSet3Result=" + getLocalPlayerSet3Result() +
            ", visitorPlayerSet3Result=" + getVisitorPlayerSet3Result() +
            ", localPlayerSets=" + getLocalPlayerSets() +
            ", visitorPlayerSets=" + getVisitorPlayerSets() +
            ", localPlayerAbandoned='" + isLocalPlayerAbandoned() + "'" +
            ", visitorPlayerAbandoned='" + isVisitorPlayerAbandoned() + "'" +
            ", localPlayerNotPresent='" + isLocalPlayerNotPresent() + "'" +
            ", visitorPlayerNotPresent='" + isVisitorPlayerNotPresent() + "'" +
            "}";
    }
}
