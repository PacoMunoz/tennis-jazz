import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';

@Component({
  selector: 'jhi-tournament-tennis-detail',
  templateUrl: './tournament-tennis-detail.component.html'
})
export class TournamentTennisDetailComponent implements OnInit {
  tournament: ITournamentTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tournament }) => {
      this.tournament = tournament;
    });
  }

  previousState() {
    window.history.back();
  }
}
