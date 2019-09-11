import { Component, Input, OnInit } from '@angular/core';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'jhi-tournament-tennis-group-player',
  templateUrl: './tournament-tennis-group-player.component.html'
})
export class TournamentTennisGroupPlayerComponent implements OnInit {
  @Input() group;
  players: IPlayerTennis[];

  constructor(
    protected playerTennisService: PlayerTennisService,
    protected jhiAlertService: JhiAlertService,
    protected sanitizer: DomSanitizer
  ) {
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
      let thumbnail = 'data:' + data[i].avatarContentType + ';base64,' + data[i].avatar;
      data[i].avatar = this.sanitizer.bypassSecurityTrustUrl(thumbnail);
      this.players.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
