import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

@Component({
  selector: 'jhi-tournament-group-tennis-detail',
  templateUrl: './tournament-group-tennis-detail.component.html'
})
export class TournamentGroupTennisDetailComponent implements OnInit {
  tournamentGroup: ITournamentGroupTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tournamentGroup }) => {
      this.tournamentGroup = tournamentGroup;
    });
  }

  previousState() {
    window.history.back();
  }
}
