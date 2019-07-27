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
    player1Set1Result: [],
    player2Set1Result: [],
    player1Set2Result: [],
    player2Set2Result: [],
    player1Set3Result: [],
    player2Set3Result: [],
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
      player1Set1Result: match.player1Set1Result,
      player2Set1Result: match.player2Set1Result,
      player1Set2Result: match.player1Set2Result,
      player2Set2Result: match.player2Set2Result,
      player1Set3Result: match.player1Set3Result,
      player2Set3Result: match.player2Set3Result,
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
      player1Set1Result: this.editForm.get(['player1Set1Result']).value,
      player2Set1Result: this.editForm.get(['player2Set1Result']).value,
      player1Set2Result: this.editForm.get(['player1Set2Result']).value,
      player2Set2Result: this.editForm.get(['player2Set2Result']).value,
      player1Set3Result: this.editForm.get(['player1Set3Result']).value,
      player2Set3Result: this.editForm.get(['player2Set3Result']).value,
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
