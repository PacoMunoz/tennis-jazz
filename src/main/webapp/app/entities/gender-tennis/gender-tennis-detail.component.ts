import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGenderTennis } from 'app/shared/model/gender-tennis.model';

@Component({
  selector: 'jhi-gender-tennis-detail',
  templateUrl: './gender-tennis-detail.component.html'
})
export class GenderTennisDetailComponent implements OnInit {
  gender: IGenderTennis;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ gender }) => {
      this.gender = gender;
    });
  }

  previousState() {
    window.history.back();
  }
}
