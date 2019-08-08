import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMatchTennis } from 'app/shared/model/match-tennis.model';

@Component({
  selector: 'jhi-match-tennis-detail',
  templateUrl: './match-tennis-detail.component.html'
})
export class MatchTennisDetailComponent implements OnInit {
  match: IMatchTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ match }) => {
      this.match = match;
    });
  }

  previousState() {
    window.history.back();
  }
}
