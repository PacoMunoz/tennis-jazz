import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { ITournamentTennis, TournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { TournamentTennisService } from './tournament-tennis.service';

@Component({
  selector: 'jhi-tournament-tennis-update',
  templateUrl: './tournament-tennis-update.component.html'
})
export class TournamentTennisUpdateComponent implements OnInit {
  isSaving: boolean;
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [],
    inProgress: [],
    winPoints: [null, [Validators.required]],
    lossPoints: [null, [Validators.required]],
    notPresentPoints: [],
    injuredPoints: []
  });

  constructor(protected tournamentService: TournamentTennisService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tournament }) => {
      this.updateForm(tournament);
    });
  }

  updateForm(tournament: ITournamentTennis) {
    this.editForm.patchValue({
      id: tournament.id,
      name: tournament.name,
      startDate: tournament.startDate,
      endDate: tournament.endDate,
      inProgress: tournament.inProgress,
      winPoints: tournament.winPoints,
      lossPoints: tournament.lossPoints,
      notPresentPoints: tournament.notPresentPoints,
      injuredPoints: tournament.injuredPoints
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tournament = this.createFromForm();
    if (tournament.id !== undefined) {
      this.subscribeToSaveResponse(this.tournamentService.update(tournament));
    } else {
      this.subscribeToSaveResponse(this.tournamentService.create(tournament));
    }
  }

  private createFromForm(): ITournamentTennis {
    return {
      ...new TournamentTennis(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      startDate: this.editForm.get(['startDate']).value,
      endDate: this.editForm.get(['endDate']).value,
      inProgress: this.editForm.get(['inProgress']).value,
      winPoints: this.editForm.get(['winPoints']).value,
      lossPoints: this.editForm.get(['lossPoints']).value,
      notPresentPoints: this.editForm.get(['notPresentPoints']).value,
      injuredPoints: this.editForm.get(['injuredPoints']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournamentTennis>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
