import { Component, Input, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { RankingTennisService } from 'app/entities/ranking-tennis';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';

@Component({
  selector: 'jhi-tournament-tennis-group-ranking',
  templateUrl: './tournament-tennis-group-ranking.component.html'
})
export class TournamentTennisGroupRankingComponent implements OnInit {
  @Input() group;
  ranking: IRankingTennis[];
  displayedColumns = [
    '#',
    'name',
    'points',
    'matches-played',
    'matches-won',
    'matches-loss',
    'matches-not-present',
    'matches-abandoned',
    'sets-won',
    'sets-loss',
    'games-won',
    'games-loss',
    'tie-breaks-played',
    'tie-breaks-won'
  ];

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
    return [
      'points' + ',' + 'desc',
      'setsWon' + ',' + 'desc',
      'setsLoss' + ',' + 'asc',
      'gamesWon' + ',' + 'desc',
      'gamesLoss' + ',' + 'asc'
    ];
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
