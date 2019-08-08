import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoundTennis } from 'app/shared/model/round-tennis.model';

@Component({
  selector: 'jhi-round-tennis-detail',
  templateUrl: './round-tennis-detail.component.html'
})
export class RoundTennisDetailComponent implements OnInit {
  round: IRoundTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ round }) => {
      this.round = round;
    });
  }

  previousState() {
    window.history.back();
  }
}
