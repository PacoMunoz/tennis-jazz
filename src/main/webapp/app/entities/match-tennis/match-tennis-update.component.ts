import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMatchTennis, MatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from './match-tennis.service';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { RoundTennisService } from 'app/entities/round-tennis';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';

@Component({
  selector: 'jhi-match-tennis-update',
  templateUrl: './match-tennis-update.component.html'
})
export class MatchTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  rounds: IRoundTennis[];

  players: IPlayerTennis[];

  editForm = this.fb.group({
    id: [],
    localPlayerSet1Result: [],
    visitorPlayerSet1Result: [],
    localPlayerTBSet1Result: [],
    visitorPlayerTBSet1Result: [],
    localPlayerTBSet2Result: [],
    visitorPlayerTBSet2Result: [],
    localPlayerTBSet3Result: [],
    visitorPlayerTBSet3Result: [],
    localPlayerSet2Result: [],
    visitorPlayerSet2Result: [],
    localPlayerSet3Result: [],
    visitorPlayerSet3Result: [],
    localPlayerSets: [],
    visitorPlayerSets: [],
    localPlayerAbandoned: [],
    visitorPlayerAbandoned: [],
    localPlayerNotPresent: [],
    visitorPlayerNotPresent: [],
    round: [],
    visitorPlayer: [],
    localPlayer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected matchService: MatchTennisService,
    protected roundService: RoundTennisService,
    protected playerService: PlayerTennisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ match }) => {
      this.updateForm(match);
    });
    this.roundService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRoundTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRoundTennis[]>) => response.body)
      )
      .subscribe((res: IRoundTennis[]) => (this.rounds = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayerTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayerTennis[]>) => response.body)
      )
      .subscribe((res: IPlayerTennis[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(match: IMatchTennis) {
    this.editForm.patchValue({
      id: match.id,
      localPlayerSet1Result: match.localPlayerSet1Result,
      visitorPlayerSet1Result: match.visitorPlayerSet1Result,
      localPlayerTBSet1Result: match.localPlayerTBSet1Result,
      visitorPlayerTBSet1Result: match.visitorPlayerTBSet1Result,
      localPlayerTBSet2Result: match.localPlayerTBSet2Result,
      visitorPlayerTBSet2Result: match.visitorPlayerTBSet2Result,
      localPlayerTBSet3Result: match.localPlayerTBSet3Result,
      visitorPlayerTBSet3Result: match.visitorPlayerTBSet3Result,
      localPlayerSet2Result: match.localPlayerSet2Result,
      visitorPlayerSet2Result: match.visitorPlayerSet2Result,
      localPlayerSet3Result: match.localPlayerSet3Result,
      visitorPlayerSet3Result: match.visitorPlayerSet3Result,
      localPlayerSets: match.localPlayerSets,
      visitorPlayerSets: match.visitorPlayerSets,
      localPlayerAbandoned: match.localPlayerAbandoned,
      visitorPlayerAbandoned: match.visitorPlayerAbandoned,
      localPlayerNotPresent: match.localPlayerNotPresent,
      visitorPlayerNotPresent: match.visitorPlayerNotPresent,
      round: match.round,
      visitorPlayer: match.visitorPlayer,
      localPlayer: match.localPlayer
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const match = this.createFromForm();
    if (match.id !== undefined) {
      this.subscribeToSaveResponse(this.matchService.update(match));
    } else {
      this.subscribeToSaveResponse(this.matchService.create(match));
    }
  }

  private createFromForm(): IMatchTennis {
    return {
      ...new MatchTennis(),
      id: this.editForm.get(['id']).value,
      localPlayerSet1Result: this.editForm.get(['localPlayerSet1Result']).value,
      visitorPlayerSet1Result: this.editForm.get(['visitorPlayerSet1Result']).value,
      localPlayerTBSet1Result: this.editForm.get(['localPlayerTBSet1Result']).value,
      visitorPlayerTBSet1Result: this.editForm.get(['visitorPlayerTBSet1Result']).value,
      localPlayerTBSet2Result: this.editForm.get(['localPlayerTBSet2Result']).value,
      visitorPlayerTBSet2Result: this.editForm.get(['visitorPlayerTBSet2Result']).value,
      localPlayerTBSet3Result: this.editForm.get(['localPlayerTBSet3Result']).value,
      visitorPlayerTBSet3Result: this.editForm.get(['visitorPlayerTBSet3Result']).value,
      localPlayerSet2Result: this.editForm.get(['localPlayerSet2Result']).value,
      visitorPlayerSet2Result: this.editForm.get(['visitorPlayerSet2Result']).value,
      localPlayerSet3Result: this.editForm.get(['localPlayerSet3Result']).value,
      visitorPlayerSet3Result: this.editForm.get(['visitorPlayerSet3Result']).value,
      localPlayerSets: this.editForm.get(['localPlayerSets']).value,
      visitorPlayerSets: this.editForm.get(['visitorPlayerSets']).value,
      localPlayerAbandoned: this.editForm.get(['localPlayerAbandoned']).value,
      visitorPlayerAbandoned: this.editForm.get(['visitorPlayerAbandoned']).value,
      localPlayerNotPresent: this.editForm.get(['localPlayerNotPresent']).value,
      visitorPlayerNotPresent: this.editForm.get(['visitorPlayerNotPresent']).value,
      round: this.editForm.get(['round']).value,
      visitorPlayer: this.editForm.get(['visitorPlayer']).value,
      localPlayer: this.editForm.get(['localPlayer']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatchTennis>>) {
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

  trackRoundById(index: number, item: IRoundTennis) {
    return item.id;
  }

  trackPlayerById(index: number, item: IPlayerTennis) {
    return item.id;
  }
}
