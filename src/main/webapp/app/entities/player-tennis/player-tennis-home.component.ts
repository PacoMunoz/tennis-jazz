import { Component, OnInit } from '@angular/core';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { AccountService, IUser, UserService } from 'app/core';
import { switchMap } from 'rxjs/operators';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis/player-tennis.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-player-tennis-home',
  templateUrl: './player-tennis-home.component.html'
})
export class PlayerTennisHomeComponent implements OnInit {
  currentMatches: IMatchTennis[];
  currentAccount: any;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected matchService: MatchTennisService,
    protected playerService: PlayerTennisService,
    protected sanitizer: DomSanitizer,
    protected userService: UserService
  ) {
    this.currentMatches = [];
  }

  ngOnInit(): void {
    this.accountService
      .identity()
      .then(account => {
        this.currentAccount = account;
      })
      .finally(() => {
        if (this.currentAccount != null) {
          this.getHomeData(this.currentAccount.login);
        }
      });
  }

  protected getHomeData(login: any) {
    this.userService
      .find(this.currentAccount.login)
      .pipe(
        switchMap((res: HttpResponse<IUser>) =>
          this.playerService.query({
            'userId.equals': res.body.id
          })
        ),
        switchMap((res: HttpResponse<IPlayerTennis[]>) =>
          this.matchService.findAllCurrent({
            id: res.body != null && res.body.length === 1 ? res.body[0].id : 0
          })
        )
      )
      .subscribe(
        (res: HttpResponse<IMatchTennis[]>) => this.setMatches(res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  protected setMatches(data: IMatchTennis[]) {
    for (let i = 0; i < data.length; i++) {
      data[i].localPlayer.avatar = this.sanitizerAvatar(data[i].localPlayer);
      data[i].visitorPlayer.avatar = this.sanitizerAvatar(data[i].visitorPlayer);
      this.currentMatches.push(data[i]);
    }
  }

  protected sanitizerAvatar(data: IPlayerTennis): any {
    const thumbnail = 'data:' + data.avatarContentType + ';base64,' + data.avatar;
    return this.sanitizer.bypassSecurityTrustUrl(thumbnail);
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
