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
    gamesWon: [],
    gamesLoss: [],
    setsWon: [],
    setsLoss: [],
    matchesPlayed: [],
    matchesWon: [],
    matchesLoss: [],
    matchesNotPresent: [],
    matchesAbandoned: [],
    tieBreaksPlayed: [],
    tieBreaksWon: [],
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
      gamesWon: ranking.gamesWon,
      gamesLoss: ranking.gamesLoss,
      setsWon: ranking.setsWon,
      setsLoss: ranking.setsLoss,
      matchesPlayed: ranking.matchesPlayed,
      matchesWon: ranking.matchesWon,
      matchesLoss: ranking.matchesLoss,
      matchesNotPresent: ranking.matchesNotPresent,
      matchesAbandoned: ranking.matchesAbandoned,
      tieBreaksPlayed: ranking.tieBreaksPlayed,
      tieBreaksWon: ranking.tieBreaksWon,
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
      gamesWon: this.editForm.get(['gamesWon']).value,
      gamesLoss: this.editForm.get(['gamesLoss']).value,
      setsWon: this.editForm.get(['setsWon']).value,
      setsLoss: this.editForm.get(['setsLoss']).value,
      matchesPlayed: this.editForm.get(['matchesPlayed']).value,
      matchesWon: this.editForm.get(['matchesWon']).value,
      matchesLoss: this.editForm.get(['matchesLoss']).value,
      matchesNotPresent: this.editForm.get(['matchesNotPresent']).value,
      matchesAbandoned: this.editForm.get(['matchesAbandoned']).value,
      tieBreaksPlayed: this.editForm.get(['tieBreaksPlayed']).value,
      tieBreaksWon: this.editForm.get(['tieBreaksWon']).value,
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
