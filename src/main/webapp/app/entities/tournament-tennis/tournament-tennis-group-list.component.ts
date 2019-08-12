import { Component, OnInit } from '@angular/core';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { RankingTennisService } from 'app/entities/ranking-tennis';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';
import { map } from 'rxjs/operators';

@Component({
  selector: 'jhi-tournament-tennis-group-list',
  templateUrl: './tournament-tennis-group-list.component.html'
})
export class TournamentTennisGroupListComponent implements OnInit {
  tournament: ITournamentTennis;
  groups: ITournamentGroupTennis[];

  constructor(
    protected activatedRoute: ActivatedRoute,
    private tournamentGroupTennisService: TournamentGroupTennisService,
    private playerTennisService: PlayerTennisService,
    private rankingTennisService: RankingTennisService,
    private jhiAlertService: JhiAlertService
  ) {
    this.groups = [];
  }

  ngOnInit(): void {
    this.activatedRoute.data
      .pipe(
        map(({ tournament }) => {
          this.tournament = tournament;
          return this.tournamentGroupTennisService
            .query({
              'tournamentId.equals': this.tournament.id
            })
            .subscribe(
              (res: HttpResponse<ITournamentGroupTennis[]>) => this.setTournamentGroups(res.body),
              (res: HttpErrorResponse) => this.onError(res.message)
            );
        })
      )
      .subscribe();

    console.log('Vamoa a buscar el sort');
    this.rankingTennisService
      .query({
        page: 0,
        size: 20,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IRankingTennis[]>) => console.log('El numero de ranking es', res.body.length),
        error => console.log('error')
      );
  }

  sort() {
    const result = ['points' + ',' + 'desc'];
    return result;
  }

  protected setTournamentGroups(data: ITournamentGroupTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.groups.push(data[i]);
    }
  }

  protected setPlayersTennis() {
    for (let i = 0; i < this.groups.length; i++) {
      this.playerTennisService
        .query({
          'groupsId.equals': this.groups[i].id
        })
        .subscribe(
          (res: HttpResponse<IPlayerTennis[]>) => this.setPlayerTennis(res.body, i),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
  }

  protected setPlayerTennis(data: IPlayerTennis[], groupId: number) {
    this.groups[groupId].players = [];
    for (let i = 0; i < data.length; i++) {
      this.groups[groupId].players.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  previousState() {
    window.history.back();
  }
}
