import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITournamentGroupTennis, TournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from './tournament-group-tennis.service';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { TournamentTennisService } from 'app/entities/tournament-tennis';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';

@Component({
  selector: 'jhi-tournament-group-tennis-update',
  templateUrl: './tournament-group-tennis-update.component.html'
})
export class TournamentGroupTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  tournaments: ITournamentTennis[];

  players: IPlayerTennis[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    tournament: [],
    players: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tournamentGroupService: TournamentGroupTennisService,
    protected tournamentService: TournamentTennisService,
    protected playerService: PlayerTennisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tournamentGroup }) => {
      this.updateForm(tournamentGroup);
    });
    this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournamentTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournamentTennis[]>) => response.body)
      )
      .subscribe((res: ITournamentTennis[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayerTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayerTennis[]>) => response.body)
      )
      .subscribe((res: IPlayerTennis[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(tournamentGroup: ITournamentGroupTennis) {
    this.editForm.patchValue({
      id: tournamentGroup.id,
      name: tournamentGroup.name,
      tournament: tournamentGroup.tournament,
      players: tournamentGroup.players
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tournamentGroup = this.createFromForm();
    if (tournamentGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.tournamentGroupService.update(tournamentGroup));
    } else {
      this.subscribeToSaveResponse(this.tournamentGroupService.create(tournamentGroup));
    }
  }

  private createFromForm(): ITournamentGroupTennis {
    return {
      ...new TournamentGroupTennis(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      tournament: this.editForm.get(['tournament']).value,
      players: this.editForm.get(['players']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITournamentGroupTennis>>) {
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

  trackTournamentById(index: number, item: ITournamentTennis) {
    return item.id;
  }

  trackPlayerById(index: number, item: IPlayerTennis) {
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
