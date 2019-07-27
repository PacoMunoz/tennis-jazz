import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IRoundTennis, RoundTennis } from 'app/shared/model/round-tennis.model';
import { RoundTennisService } from './round-tennis.service';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';

@Component({
  selector: 'jhi-round-tennis-update',
  templateUrl: './round-tennis-update.component.html'
})
export class RoundTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  tournamentgroups: ITournamentGroupTennis[];
  startDateDp: any;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    startDate: [],
    endDate: [],
    tournamentGroup: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected roundService: RoundTennisService,
    protected tournamentGroupService: TournamentGroupTennisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ round }) => {
      this.updateForm(round);
    });
    this.tournamentGroupService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournamentGroupTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournamentGroupTennis[]>) => response.body)
      )
      .subscribe((res: ITournamentGroupTennis[]) => (this.tournamentgroups = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(round: IRoundTennis) {
    this.editForm.patchValue({
      id: round.id,
      name: round.name,
      startDate: round.startDate,
      endDate: round.endDate,
      tournamentGroup: round.tournamentGroup
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const round = this.createFromForm();
    if (round.id !== undefined) {
      this.subscribeToSaveResponse(this.roundService.update(round));
    } else {
      this.subscribeToSaveResponse(this.roundService.create(round));
    }
  }

  private createFromForm(): IRoundTennis {
    return {
      ...new RoundTennis(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      startDate: this.editForm.get(['startDate']).value,
      endDate: this.editForm.get(['endDate']).value,
      tournamentGroup: this.editForm.get(['tournamentGroup']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoundTennis>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackTournamentGroupById(index: number, item: ITournamentGroupTennis) {
    return item.id;
  }
}
