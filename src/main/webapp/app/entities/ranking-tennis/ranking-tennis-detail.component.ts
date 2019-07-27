import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';

@Component({
  selector: 'jhi-ranking-tennis-detail',
  templateUrl: './ranking-tennis-detail.component.html'
})
export class RankingTennisDetailComponent implements OnInit {
  ranking: IRankingTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ranking }) => {
      this.ranking = ranking;
    });
  }

  previousState() {
    window.history.back();
  }
}
