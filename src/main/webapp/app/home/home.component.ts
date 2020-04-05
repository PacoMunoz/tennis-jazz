import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { LoginModalService, AccountService, Account } from 'app/core';
import { TournamentTennisService } from 'app/entities/tournament-tennis';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MatchTennisService } from 'app/entities/match-tennis';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
  tournaments: ITournamentTennis[];
  account: Account;
  modalRef: NgbModalRef;

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private eventManager: JhiEventManager,
    private tournamentService: TournamentTennisService,
    private matchService: MatchTennisService,
    private jhiAlertService: JhiAlertService
  ) {
    this.tournaments = [];
  }

  ngOnInit() {
    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
    this.tournamentService
      .query()
      .subscribe(
        (res: HttpResponse<ITournamentTennis[]>) => (this.tournaments = res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  registerAuthenticationSuccess() {
    this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().then(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
