import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

@Component({
  selector: 'jhi-player-tennis-detail',
  templateUrl: './player-tennis-detail.component.html'
})
export class PlayerTennisDetailComponent implements OnInit {
  player: IPlayerTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.player = player;
    });
  }

  previousState() {
    window.history.back();
  }
}
