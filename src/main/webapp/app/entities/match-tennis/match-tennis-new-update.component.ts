import { Component, OnInit } from '@angular/core';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IMatchTennis, MatchTennis } from 'app/shared/model/match-tennis.model';
import { RoundTennisService } from 'app/entities/round-tennis';
import { MatchTennisService } from 'app/entities/match-tennis/match-tennis.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { filter, map } from 'rxjs/operators';
import { Observable, concat } from 'rxjs';
import { RankingTennisService } from 'app/entities/ranking-tennis';

@Component({
  selector: 'jhi-match-tennis-new-update',
  templateUrl: './match-tennis-new-update.component.html'
})
export class MatchTennisNewUpdateComponent implements OnInit {
  isSaving: boolean;
  editEnable: boolean;
  rounds: IRoundTennis[];
  counter: number;
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
    postponed: [],
    round: [],
    visitorPlayer: [],
    localPlayer: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected matchService: MatchTennisService,
    protected roundService: RoundTennisService,
    protected playerService: PlayerTennisService,
    protected rankingService: RankingTennisService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    private fb: FormBuilder
  ) {
    this.counter = 0;
  }

  ngOnInit() {
    this.editEnable = true;
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

  clickCheckBox(event: any) {
    switch (event.target.name) {
      case 'localPlayerAbandoned':
        if (event.target.checked) {
          this.editEnable = true;
        }
        this.editForm.get(['visitorPlayerAbandoned']).setValue(false);
        this.editForm.get(['localPlayerNotPresent']).setValue(false);
        this.editForm.get(['visitorPlayerNotPresent']).setValue(false);
        this.editForm.get(['postponed']).setValue(false);
        break;
      case 'visitorPlayerAbandoned':
        if (event.target.checked) {
          this.editEnable = true;
        }
        this.editForm.get(['localPlayerAbandoned']).setValue(false);
        this.editForm.get(['localPlayerNotPresent']).setValue(false);
        this.editForm.get(['visitorPlayerNotPresent']).setValue(false);
        this.editForm.get(['postponed']).setValue(false);
        break;
      case 'localPlayerNotPresent':
        if (event.target.checked) {
          this.setCustomResults(0, 6, 0, 6, 0, 2);
        } else {
          this.clearCustomResult();
        }
        this.editForm.get(['localPlayerAbandoned']).setValue(false);
        this.editForm.get(['visitorPlayerAbandoned']).setValue(false);
        this.editForm.get(['visitorPlayerNotPresent']).setValue(false);
        this.editForm.get(['postponed']).setValue(false);
        break;
      case 'visitorPlayerNotPresent':
        if (event.target.checked) {
          this.setCustomResults(6, 0, 6, 0, 2, 0);
        } else {
          this.clearCustomResult();
        }
        this.editForm.get(['localPlayerAbandoned']).setValue(false);
        this.editForm.get(['visitorPlayerAbandoned']).setValue(false);
        this.editForm.get(['localPlayerNotPresent']).setValue(false);
        this.editForm.get(['postponed']).setValue(false);
        break;
      case 'postponed':
        if (event.target.checked) {
          this.editEnable = true;
        }
        this.editForm.get(['localPlayerAbandoned']).setValue(false);
        this.editForm.get(['visitorPlayerAbandoned']).setValue(false);
        this.editForm.get(['localPlayerNotPresent']).setValue(false);
        this.editForm.get(['visitorPlayerNotPresent']).setValue(false);
        break;
    }
  }

  setCustomResults(
    localSet1: number,
    visitorSet1: number,
    localSet2: number,
    visitorSet2: number,
    localSets: number,
    visitorResult: number
  ) {
    this.editEnable = false;
    this.editForm.get(['localPlayerSet1Result']).setValue(localSet1);
    this.editForm.get(['visitorPlayerSet1Result']).setValue(visitorSet1);
    this.editForm.get(['localPlayerSet2Result']).setValue(localSet2);
    this.editForm.get(['visitorPlayerSet2Result']).setValue(visitorSet2);
    this.editForm.get(['localPlayerSets']).setValue(localSets);
    this.editForm.get(['visitorPlayerSets']).setValue(visitorResult);
  }

  clearCustomResult() {
    this.editEnable = true;
    this.editForm.get(['localPlayerSet1Result']).setValue(null);
    this.editForm.get(['visitorPlayerSet1Result']).setValue(null);
    this.editForm.get(['localPlayerSet2Result']).setValue(null);
    this.editForm.get(['visitorPlayerSet2Result']).setValue(null);
    this.editForm.get(['localPlayerSets']).setValue(null);
    this.editForm.get(['visitorPlayerSets']).setValue(null);
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
      postponed: match.postponed,
      round: match.round,
      visitorPlayer: match.visitorPlayer,
      localPlayer: match.localPlayer
    });
  }

  save() {
    this.isSaving = true;
    const match = this.createFromForm();
    const updateLocalRankingO = this.rankingService.updateTournamentPlayerRanking(match.localPlayer.id, match.round.id);
    const updateVisitorRankingO = this.rankingService.updateTournamentPlayerRanking(match.visitorPlayer.id, match.round.id);
    if (match.id !== undefined) {
      const updateMatchO = this.matchService.update(match);
      this.subscribeToSaveUpdateResponse(concat(updateMatchO, updateLocalRankingO, updateVisitorRankingO));
    } else {
      const createO = this.matchService.create(match);
      this.subscribeToSaveUpdateResponse(concat(createO, updateLocalRankingO, updateVisitorRankingO));
    }
  }

  protected subscribeToSaveUpdateResponse(result: Observable<HttpResponse<IMatchTennis>>) {
    result.subscribe(
      () => {
        this.counter++;
        if (this.counter === 3) {
          this.onSaveSuccess();
        }
      },
      () => this.onSaveError()
    );
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

  previousState() {
    window.history.back();
  }

  trackRoundById(index: number, item: IRoundTennis) {
    return item.id;
  }

  trackPlayerById(index: number, item: IPlayerTennis) {
    return item.id;
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
      postponed: this.editForm.get(['postponed']).value,
      round: this.editForm.get(['round']).value,
      visitorPlayer: this.editForm.get(['visitorPlayer']).value,
      localPlayer: this.editForm.get(['localPlayer']).value
    };
  }
}
