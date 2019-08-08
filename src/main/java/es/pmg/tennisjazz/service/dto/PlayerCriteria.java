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
 * Criteria class for the {@link es.pmg.tennisjazz.domain.Player} entity. This class is used
 * in {@link es.pmg.tennisjazz.web.rest.PlayerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /players?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter surname;

    private StringFilter email;

    private StringFilter phone;

    private StringFilter other;

    private LongFilter visitorMatchesId;

    private LongFilter localMatchesId;

    private LongFilter rankingsId;

    private LongFilter groupsId;

    public PlayerCriteria(){
    }

    public PlayerCriteria(PlayerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.surname = other.surname == null ? null : other.surname.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.other = other.other == null ? null : other.other.copy();
        this.visitorMatchesId = other.visitorMatchesId == null ? null : other.visitorMatchesId.copy();
        this.localMatchesId = other.localMatchesId == null ? null : other.localMatchesId.copy();
        this.rankingsId = other.rankingsId == null ? null : other.rankingsId.copy();
        this.groupsId = other.groupsId == null ? null : other.groupsId.copy();
    }

    @Override
    public PlayerCriteria copy() {
        return new PlayerCriteria(this);
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

    public StringFilter getSurname() {
        return surname;
    }

    public void setSurname(StringFilter surname) {
        this.surname = surname;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getOther() {
        return other;
    }

    public void setOther(StringFilter other) {
        this.other = other;
    }

    public LongFilter getVisitorMatchesId() {
        return visitorMatchesId;
    }

    public void setVisitorMatchesId(LongFilter visitorMatchesId) {
        this.visitorMatchesId = visitorMatchesId;
    }

    public LongFilter getLocalMatchesId() {
        return localMatchesId;
    }

    public void setLocalMatchesId(LongFilter localMatchesId) {
        this.localMatchesId = localMatchesId;
    }

    public LongFilter getRankingsId() {
        return rankingsId;
    }

    public void setRankingsId(LongFilter rankingsId) {
        this.rankingsId = rankingsId;
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
        final PlayerCriteria that = (PlayerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(surname, that.surname) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(other, that.other) &&
            Objects.equals(visitorMatchesId, that.visitorMatchesId) &&
            Objects.equals(localMatchesId, that.localMatchesId) &&
            Objects.equals(rankingsId, that.rankingsId) &&
            Objects.equals(groupsId, that.groupsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        surname,
        email,
        phone,
        other,
        visitorMatchesId,
        localMatchesId,
        rankingsId,
        groupsId
        );
    }

    @Override
    public String toString() {
        return "PlayerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (surname != null ? "surname=" + surname + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (other != null ? "other=" + other + ", " : "") +
                (visitorMatchesId != null ? "visitorMatchesId=" + visitorMatchesId + ", " : "") +
                (localMatchesId != null ? "localMatchesId=" + localMatchesId + ", " : "") +
                (rankingsId != null ? "rankingsId=" + rankingsId + ", " : "") +
                (groupsId != null ? "groupsId=" + groupsId + ", " : "") +
            "}";
    }

}
