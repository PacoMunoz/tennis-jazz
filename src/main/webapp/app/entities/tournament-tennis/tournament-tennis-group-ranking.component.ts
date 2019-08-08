import { Component, Input, OnInit } from '@angular/core';
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
  selector: 'jhi-tournament-tennis-group-ranking',
  templateUrl: './tournament-tennis-group-ranking.component.html'
})
export class TournamentTennisGroupRankingComponent implements OnInit {
  @Input() group: ITournamentGroupTennis;
  ranking: IRankingTennis[];

  constructor(private rankingTennisService: RankingTennisService, private jhiAlertService: JhiAlertService) {
    this.ranking = [];
  }

  ngOnInit(): void {
    this.rankingTennisService
      .query({
        'tournamentGroupId.equals': this.group.id,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IRankingTennis[]>) => this.setRanking(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  sort() {
    return ['points' + ',' + 'desc'];
  }

  protected setRanking(data: IRankingTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.ranking.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
