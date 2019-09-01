import { Component, Input, OnInit } from '@angular/core';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-tournament-tennis-group-round-match',
  templateUrl: './tournament-tennis-group-round-match.component.html'
})
export class TournamentTennisGroupRoundMatchComponent implements OnInit {
  @Input() round;
  matches: IMatchTennis[];

  constructor(protected matchTennisService: MatchTennisService, protected jhiAlertService: JhiAlertService) {
    this.matches = [];
  }

  ngOnInit(): void {
    this.matchTennisService
      .query({
        'roundId.equals': this.round.id
      })
      .subscribe(
        (res: HttpResponse<IMatchTennis[]>) => this.setMatches(res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  protected setMatches(data: IMatchTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.matches.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
