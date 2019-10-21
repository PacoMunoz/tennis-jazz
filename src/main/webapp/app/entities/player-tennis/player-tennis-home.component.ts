import { Component, OnInit } from '@angular/core';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { AccountService, IUser, UserService } from 'app/core';
import { concatAll, concatMap, map } from 'rxjs/operators';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis/player-tennis.service';

@Component({
  selector: 'jhi-player-tennis-home',
  templateUrl: './player-tennis-home.component.html'
})
export class PlayerTennisHomeComponent implements OnInit {
  currentMatches: IMatchTennis[];
  currentAccount: any;
  player: IPlayerTennis;

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected matchService: MatchTennisService,
    protected playerService: PlayerTennisService,
    protected userService: UserService
  ) {
    this.currentMatches = [];
  }

  ngOnInit(): void {
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      if (account != null) {
        this.getHomeData(this.currentAccount.login);
      }
    });
  }

  protected getHomeData(login: any) {
    this.userService.find(this.currentAccount.login).subscribe(
      (res: HttpResponse<IUser>) => {
        this.playerService
          .query({
            'userId.equals': res.body.id
          })
          .subscribe(
            (res: HttpResponse<IPlayerTennis[]>) => {
              if (res.body != null && res.body.length == 1) {
                this.matchService
                  .findAllCurrent({
                    id: res.body[0].id
                  })
                  .subscribe(
                    (res: HttpResponse<IMatchTennis[]>) => (this.currentMatches = res.body),
                    (error: HttpErrorResponse) => this.onError(error.message)
                  );
              }
            },
            (error: HttpErrorResponse) => this.onError(error.message)
          );
      },
      (error: HttpErrorResponse) => this.onError(error.message)
    );
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
