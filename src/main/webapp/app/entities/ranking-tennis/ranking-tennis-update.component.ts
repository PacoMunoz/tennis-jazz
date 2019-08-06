import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRankingTennis, RankingTennis } from 'app/shared/model/ranking-tennis.model';
import { RankingTennisService } from './ranking-tennis.service';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';

@Component({
  selector: 'jhi-ranking-tennis-update',
  templateUrl: './ranking-tennis-update.component.html'
})
export class RankingTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  tournamentgroups: ITournamentGroupTennis[];

  players: IPlayerTennis[];

  editForm = this.fb.group({
    id: [],
    points: [],
    gamesWin: [],
    gamesLoss: [],
    setsWin: [],
    setsLost: [],
    matchesPlayed: [],
    matchesWined: [],
    matchesLoss: [],
    matchesNotPresent: [],
    matchesAbandoned: [],
    tournamentGroup: [],
    player: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rankingService: RankingTennisService,
    protected tournamentGroupService: TournamentGroupTennisService,
    protected playerService: PlayerTennisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ ranking }) => {
      this.updateForm(ranking);
    });
    this.tournamentGroupService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournamentGroupTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournamentGroupTennis[]>) => response.body)
      )
      .subscribe((res: ITournamentGroupTennis[]) => (this.tournamentgroups = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.playerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPlayerTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPlayerTennis[]>) => response.body)
      )
      .subscribe((res: IPlayerTennis[]) => (this.players = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(ranking: IRankingTennis) {
    this.editForm.patchValue({
      id: ranking.id,
      points: ranking.points,
      gamesWin: ranking.gamesWin,
      gamesLoss: ranking.gamesLoss,
      setsWin: ranking.setsWin,
      setsLost: ranking.setsLost,
      matchesPlayed: ranking.matchesPlayed,
      matchesWined: ranking.matchesWined,
      matchesLoss: ranking.matchesLoss,
      matchesNotPresent: ranking.matchesNotPresent,
      matchesAbandoned: ranking.matchesAbandoned,
      tournamentGroup: ranking.tournamentGroup,
      player: ranking.player
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const ranking = this.createFromForm();
    if (ranking.id !== undefined) {
      this.subscribeToSaveResponse(this.rankingService.update(ranking));
    } else {
      this.subscribeToSaveResponse(this.rankingService.create(ranking));
    }
  }

  private createFromForm(): IRankingTennis {
    return {
      ...new RankingTennis(),
      id: this.editForm.get(['id']).value,
      points: this.editForm.get(['points']).value,
      gamesWin: this.editForm.get(['gamesWin']).value,
      gamesLoss: this.editForm.get(['gamesLoss']).value,
      setsWin: this.editForm.get(['setsWin']).value,
      setsLost: this.editForm.get(['setsLost']).value,
      matchesPlayed: this.editForm.get(['matchesPlayed']).value,
      matchesWined: this.editForm.get(['matchesWined']).value,
      matchesLoss: this.editForm.get(['matchesLoss']).value,
      matchesNotPresent: this.editForm.get(['matchesNotPresent']).value,
      matchesAbandoned: this.editForm.get(['matchesAbandoned']).value,
      tournamentGroup: this.editForm.get(['tournamentGroup']).value,
      player: this.editForm.get(['player']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRankingTennis>>) {
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

  trackPlayerById(index: number, item: IPlayerTennis) {
    return item.id;
  }
}
