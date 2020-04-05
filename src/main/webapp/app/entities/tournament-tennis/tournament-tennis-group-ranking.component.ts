import { Component, Input, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { RankingTennisService } from 'app/entities/ranking-tennis';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { DomSanitizer } from '@angular/platform-browser';

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

  constructor(
    private rankingTennisService: RankingTennisService,
    protected sanitizer: DomSanitizer,
    private jhiAlertService: JhiAlertService
  ) {
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
      data[i].player.avatar = this.sanitizerAvatar(data[i].player);
      this.ranking.push(data[i]);
    }
  }

  protected sanitizerAvatar(data: IPlayerTennis): any {
    const thumbnail = 'data:' + data.avatarContentType + ';base64,' + data.avatar;
    return this.sanitizer.bypassSecurityTrustUrl(thumbnail);
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
