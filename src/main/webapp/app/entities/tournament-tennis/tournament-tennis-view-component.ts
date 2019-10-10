import { Component, OnDestroy, OnInit } from '@angular/core';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { ActivatedRoute } from '@angular/router';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-tournament-tennis-view',
  templateUrl: './tournament-tennis-view-component.html'
})
export class TournamentTennisViewComponent implements OnInit {
  tournament: ITournamentTennis;
  tournamentGroups: ITournamentGroupTennis[];
  selectedPrincipalTab = 0;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected tournamentGroupTennisService: TournamentGroupTennisService,
    private jhiAlertService: JhiAlertService
  ) {
    this.tournamentGroups = [];
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(param => {
      if (param['fromsu'] != null) {
        this.selectedPrincipalTab = 1;
      }
    });
    this.activatedRoute.data.subscribe(({ tournament }) => {
      this.tournament = tournament;
      this.tournamentGroupTennisService
        .query({
          'tournamentId.equals': tournament.id,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<ITournamentGroupTennis[]>) => this.setTournamentGroups(res.body),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
    });
  }

  protected setTournamentGroups(data: ITournamentGroupTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.tournamentGroups.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  sort() {
    return ['name' + ',' + 'asc'];
  }
}
