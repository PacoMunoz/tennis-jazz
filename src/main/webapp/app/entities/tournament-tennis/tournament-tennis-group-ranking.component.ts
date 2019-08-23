import { Component, Input, OnInit } from '@angular/core';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { RankingTennisService } from 'app/entities/ranking-tennis';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';

@Component({
  selector: 'jhi-tournament-tennis-group-ranking',
  templateUrl: './tournament-tennis-group-ranking.component.html'
})
export class TournamentTennisGroupRankingComponent implements OnInit {
  @Input() group: ITournamentGroupTennis;
  ranking: IRankingTennis[];
  sliceEnd: number;
  showLessButton: boolean;
  sliceEndValue: number = 4;

  constructor(private rankingTennisService: RankingTennisService, private jhiAlertService: JhiAlertService) {
    this.ranking = [];
    this.sliceEnd = this.sliceEndValue;
    this.showLessButton = false;
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

  showMore() {
    this.sliceEnd = this.ranking.length;
    this.showLessButton = true;
  }

  showLess() {
    this.sliceEnd = this.sliceEndValue;
    this.showLessButton = false;
  }
}
