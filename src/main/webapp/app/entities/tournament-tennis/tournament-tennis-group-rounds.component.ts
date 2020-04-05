import { Component, Input, OnInit } from '@angular/core';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { RoundTennisService } from 'app/entities/round-tennis';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-tournament-tennis-group-rounds',
  templateUrl: './tournament-tennis-group-rounds.component.html'
})
export class TournamentTennisGroupRoundsComponent implements OnInit {
  @Input() group;
  rounds: IRoundTennis[];
  isShowingAllRounds: boolean;

  constructor(protected roundTennisService: RoundTennisService, protected jhiAlertService: JhiAlertService) {
    this.rounds = [];
  }

  ngOnInit(): void {
    this.getCurrentRound();
  }

  getAllRounds() {
    this.isShowingAllRounds = true;
    this.roundTennisService
      .query({
        'tournamentGroupId.equals': this.group.id,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IRoundTennis[]>) => this.setRounds(res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  getCurrentRound() {
    this.isShowingAllRounds = false;
    this.roundTennisService
      .query({
        'tournamentGroupId.equals': this.group.id,
        'startDate.lessThanOrEqual': this.formatDate(new Date()),
        'endDate.greaterThanOrEqual': this.formatDate(new Date()),
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IRoundTennis[]>) => this.setRounds(res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  protected formatDate(date: Date) {
    const day = date.getDate().toString();
    const month = (date.getMonth() + 1).toString();
    const year = date.getFullYear();

    return year + '-' + month.padStart(2, '0') + '-' + day.padStart(2, '0');
  }

  protected setRounds(data: IRoundTennis[]) {
    this.rounds = [];
    for (let i = 0; i < data.length; i++) {
      this.rounds.push(data[i]);
    }
  }

  sort() {
    return ['startDate' + ',' + 'asc'];
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
