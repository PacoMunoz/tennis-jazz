import { Component, OnInit } from '@angular/core';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { IMatchTennis, MatchTennis } from 'app/shared/model/match-tennis.model';
import { TournamentTennisService } from 'app/entities/tournament-tennis';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';
import { RoundTennisService } from 'app/entities/round-tennis';
import { MatchTennisService } from 'app/entities/match-tennis/match-tennis.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { filter, map } from 'rxjs/operators';
import { Observable, merge, concat } from 'rxjs';

@Component({
  selector: 'jhi-match-tennis-newUpdate',
  templateUrl: './match-tennis-new-update.component.html'
})
export class MatchTennisNewUpdateComponent implements OnInit {
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
    /* Approach from observables
        const group = 1;

        const player = 1;

        let groups: ITournamentGroupTennis[] = [];

        let rounds: IRoundTennis[] = [];

        let matches: IMatchTennis[] = [];

        let round: number[] = [];

        const roundsObservable = this.roundService
            .query({
                'tournamentGroupId.equals': group
            })
            .pipe(
                map((res: HttpResponse<IRoundTennis[]>) => res.body),
                map((response: IRoundTennis[]) => {
                    console.log('Los rounds son : ' + response.length);
                    for (let i = 0; i < response.length; i++) {
                        round.push(response[i].id);
                    }
                    console.log(' Tras Los rounds son: ' + round);
                })
            );

        const matchesObservable1 = this.matchService
            .query({
                'localPlayerId.equals': player,
                'roundId.in': round
            })
            .pipe(
                map((res: HttpResponse<IMatchTennis[]>) => res.body),
                map((response: IMatchTennis[]) => (matches = matches.concat(response)))
            );

        const matchesObservable3 = this.matchService
            .query({
                'visitorPlayerId.equals': player,
                'roundId.in': round
            })
            .pipe(
                map((res: HttpResponse<IMatchTennis[]>) => res.body),
                map((response: IMatchTennis[]) => (matches = matches.concat(response)))
            );

        const getMatches = merge(matchesObservable1, matchesObservable3);

        concat(roundsObservable, getMatches).subscribe(() => console.log('El numero de partidos es: ' + matches.length));
        */
    /*
                Real save method
                this.isSaving = true;
                const match = this.createFromForm();
                if (match.id !== undefined) {
                    this.subscribeToSaveResponse(this.matchService.update(match));
                } else {
                    this.subscribeToSaveResponse(this.matchService.create(match));
                }*/
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
