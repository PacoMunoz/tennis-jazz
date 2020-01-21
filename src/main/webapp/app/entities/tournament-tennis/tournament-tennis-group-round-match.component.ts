import { Component, Input, OnInit } from '@angular/core';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { AccountService } from 'app/core';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-tournament-tennis-group-round-match',
  templateUrl: './tournament-tennis-group-round-match.component.html'
})
export class TournamentTennisGroupRoundMatchComponent implements OnInit {
  @Input() round;
  @Input() isShowingAllRounds;
  matches: IMatchTennis[];

  constructor(
    protected matchTennisService: MatchTennisService,
    protected jhiAlertService: JhiAlertService,
    protected sanitizer: DomSanitizer,
    protected accountService: AccountService
  ) {
    this.matches = [];
  }

  ngOnInit(): void {
    this.matchTennisService
      .query({
        'roundId.equals': this.round.id
      })
      .subscribe(
        (res: HttpResponse<IMatchTennis[]>) => this.setMatches(res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  protected setMatches(data: IMatchTennis[]) {
    for (let i = 0; i < data.length; i++) {
      data[i].localPlayer.avatar = this.sanitizerAvatar(data[i].localPlayer);
      data[i].visitorPlayer.avatar = this.sanitizerAvatar(data[i].visitorPlayer);
      this.matches.push(data[i]);
    }
  }

  protected sanitizerAvatar(data: IPlayerTennis): any {
    const thumbnail = 'data:' + data.avatarContentType + ';base64,' + data.avatar;
    return this.sanitizer.bypassSecurityTrustUrl(thumbnail);
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }
}
