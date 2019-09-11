import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

@Component({
  selector: 'jhi-player-tennis-detail',
  templateUrl: './player-tennis-detail.component.html'
})
export class PlayerTennisDetailComponent implements OnInit {
  player: IPlayerTennis;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.player = player;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
