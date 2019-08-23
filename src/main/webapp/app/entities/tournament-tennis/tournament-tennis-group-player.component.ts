import { Component, Input, OnInit } from '@angular/core';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-tournament-tennis-group-player',
  templateUrl: './tournament-tennis-group-player.component.html'
})
export class TournamentTennisGroupPlayerComponent implements OnInit {
  @Input() group: ITournamentGroupTennis;
  players: IPlayerTennis[];

  constructor(protected playerTennisService: PlayerTennisService, protected jhiAlertService: JhiAlertService) {
    this.players = [];
  }

  ngOnInit(): void {
    this.playerTennisService
      .query({
        'groupsId.equals': this.group.id
      })
      .subscribe(
        (res: HttpResponse<IPlayerTennis[]>) => this.setPlayers(res.body),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  protected setPlayers(data: IPlayerTennis[]) {
    for (let i = 0; i < data.length; i++) {
      this.players.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
