import { Component, OnInit } from '@angular/core';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { concatMap } from 'rxjs/operators';

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
    protected matchService: MatchTennisService
  ) {
    this.currentMatches = [];
  }

  ngOnInit(): void {
    /*TODO:
            1) Obtener el login del usuario logado
            2) Obtener el juagador asociado al usuario
            3) Obtener los partidos en curso del jugador
         */

    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.matchService
      .findAllCurrent({
        id: 1
      })
      .subscribe(
        (resp: HttpResponse<IMatchTennis[]>) => (this.currentMatches = resp.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
