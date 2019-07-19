import { Component, Input, OnInit } from '@angular/core';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-round-tennis-match-list',
  templateUrl: './round-tennis-match-list.component.html'
})
export class RoundTennisMatchListComponent implements OnInit {
  @Input() round: IRoundTennis;
  matches: IMatchTennis[];

  constructor(private matchTennisService: MatchTennisService) {
    this.matches = [];
  }

  ngOnInit(): void {
    this.matchTennisService
      .query({ 'roundId.equals': this.round.id })
      .subscribe((res: HttpResponse<IMatchTennis[]>) => this.setMatches(res.body));
  }

  setMatches(data: IMatchTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.matches.push(data[i]);
    }
  }
}
