import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { RoundTennisService } from 'app/entities/round-tennis';

@Component({
  selector: 'jhi-group-tennis-round-list',
  templateUrl: './tournament-group-tennis-rounds-list.component.html'
})
export class TournamentGroupTennisRoundsListComponent implements OnInit {
  group: ITournamentGroupTennis;
  rounds: IRoundTennis[];

  constructor(
    private roundTennisService: RoundTennisService,
    private activatedRoute: ActivatedRoute,
    private jhiAlertService: JhiAlertService
  ) {
    this.rounds = [];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tournamentGroup }) => {
      this.group = tournamentGroup;
    });
    this.roundTennisService
      .query({
        'tournamentGroupId.equals': this.group.id,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IRoundTennis[]>) => this.getRounds(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  sort() {
    return ['startDate' + ',' + 'desc'];
  }
  protected getRounds(data: IRoundTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.rounds.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
