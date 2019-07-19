import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayerTennis, PlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from './player-tennis.service';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';

@Component({
  selector: 'jhi-player-tennis-update',
  templateUrl: './player-tennis-update.component.html'
})
export class PlayerTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  tournamentgroups: ITournamentGroupTennis[];

  editForm = this.fb.group({
    id: [],
    name: [],
    surname: [],
    email: [],
    phone: [],
    other: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerService: PlayerTennisService,
    protected tournamentGroupService: TournamentGroupTennisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);
    });
    this.tournamentGroupService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournamentGroupTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournamentGroupTennis[]>) => response.body)
      )
      .subscribe((res: ITournamentGroupTennis[]) => (this.tournamentgroups = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(player: IPlayerTennis) {
    this.editForm.patchValue({
      id: player.id,
      name: player.name,
      surname: player.surname,
      email: player.email,
      phone: player.phone,
      other: player.other
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  private createFromForm(): IPlayerTennis {
    return {
      ...new PlayerTennis(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      surname: this.editForm.get(['surname']).value,
      email: this.editForm.get(['email']).value,
      phone: this.editForm.get(['phone']).value,
      other: this.editForm.get(['other']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerTennis>>) {
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

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
