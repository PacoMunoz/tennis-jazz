import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { from, Observable, of, pipe, Subscription } from 'rxjs';
import { filter, map, mergeMap, switchMap, tap } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { AccountService } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { TournamentGroupTennisService } from './tournament-group-tennis.service';
import { RoundTennisService } from 'app/entities/round-tennis';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { RankingTennisService } from 'app/entities/ranking-tennis';
import { IRankingTennis, RankingTennis } from 'app/shared/model/ranking-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { error } from 'util';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { flatMap } from 'tslint/lib/utils';

@Component({
  selector: 'jhi-tournament-group-tennis',
  templateUrl: './tournament-group-tennis.component.html'
})
export class TournamentGroupTennisComponent implements OnInit, OnDestroy {
  tournamentGroups: ITournamentGroupTennis[];
  currentAccount: any;
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  constructor(
    protected tournamentGroupService: TournamentGroupTennisService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected parseLinks: JhiParseLinks,
    protected accountService: AccountService,
    protected roundTennisService: RoundTennisService,
    protected matchTennisService: MatchTennisService,
    protected rankingTennisService: RankingTennisService,
    protected playerTennisService: PlayerTennisService
  ) {
    this.tournamentGroups = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.tournamentGroupService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ITournamentGroupTennis[]>) => this.paginateTournamentGroups(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  reset() {
    this.page = 0;
    this.tournamentGroups = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInTournamentGroups();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ITournamentGroupTennis) {
    return item.id;
  }

  registerChangeInTournamentGroups() {
    this.eventSubscriber = this.eventManager.subscribe('tournamentGroupListModification', response => this.reset());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTournamentGroups(data: ITournamentGroupTennis[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.tournamentGroups.push(data[i]);
    }
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  createAllPlayerRanking() {
    this.playerTennisService
      .query()
      .pipe(
        switchMap(players => from(players.body)),
        map(player => {
          let ranking: IRankingTennis = {};
          ranking.player = player;
          ranking.tournamentGroup = this.tournamentGroups[0];
          return this.rankingTennisService.create(ranking).subscribe();
        })
      )
      .subscribe();
  }
  updateRanking() {
    for (let i = 0; i < this.tournamentGroups.length; i++) {
      if (this.tournamentGroups[i].id === 1) {
        this.roundTennisService
          .query({
            'tournamentGroupId.equals': this.tournamentGroups[i].id
          })
          .subscribe(
            (res: HttpResponse<IRoundTennis[]>) => this.getRoundMatches(res.body, this.tournamentGroups[i]),
            (res: HttpErrorResponse) => this.onError(res.message)
          );
      }
    }
  }

  updateRanking_2() {
    /*for (let i = 0; i < this.tournamentGroups.length; i++) {
      this.getGroupRounds(this.tournamentGroups[i]);
    }*/
    console.log('empieza la fiesta');
    for (let i = 0; i < this.tournamentGroups.length; i++) {
      this.roundTennisService
        .query({
          'tournamentGroupId.equals': this.tournamentGroups[i].id
        })
        .pipe(
          switchMap(rounds => from(rounds.body)),
          mergeMap(round =>
            this.matchTennisService.query({ 'roundId.equals': round.id }).pipe(
              switchMap(matches => from(matches.body)),
              tap(match => console.log('Partido entre Jugador 1 ' + match.localPlayer.name + ' y jugador 2 ' + match.visitorPlayer.name))
            )
          )
        );
    }
  }

  getGroupRounds(group: ITournamentGroupTennis) {
    console.log('getGroupsRounds para el grupo : ' + group.id);
    this.roundTennisService
      .query({
        'tournamentGroupId.equals': group.id
      })
      .subscribe(
        (res: HttpResponse<IRoundTennis[]>) => this.getRoundMatches(res.body, group),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  getRoundMatches(rounds: IRoundTennis[], group: ITournamentGroupTennis) {
    console.log('getRoundMatches para el grupo: ' + group.id);
    for (let i = 0; i < rounds.length; i++) {
      console.log('Para la jornada: ' + rounds[i].name);
      this.matchTennisService
        .query({ 'roundId.equals': rounds[i].id })
        .subscribe(
          (res: HttpResponse<IMatchTennis[]>) => this.generateRankings(res.body, group),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
  }

  generateRankings(matches: IMatchTennis[], group: ITournamentGroupTennis) {
    console.log('El número de partidos son : ' + matches.length);
    for (let i = 0; i < matches.length; i++) {
      this.generateRankingJugadoresLocales(matches[i], group);
    }
  }

  generateRankingJugadoresLocales(match: IMatchTennis, group: ITournamentGroupTennis) {
    console.log('Generando ranking para el partido: ' + match.id + ' de la jornada: ' + match.round.name + ' y el grupo :' + group.name);
    this.rankingTennisService
      .query({
        'playerId.equals': match.localPlayer.id,
        'tournamentGroupId.equals': group.id
      })
      .subscribe(
        (res: HttpResponse<IRankingTennis[]>) => {
          if (res.body.length === 0) {
            console.log('No hay ranking para estos datos.');
            this.createRankingLocalPlayer(match.localPlayer, group, match);
          } else {
            console.log('Si hay ranking para estos datos.');
            this.updateRankingLocalPlayer(res.body[0], match);
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  generateRankingJugadoresVisitantes(match: IMatchTennis, group: ITournamentGroupTennis) {
    console.log('Generando ranking para el partido: ' + match.id + ' de la jornada: ' + match.round.name + ' y el grupo :' + group.name);
    this.rankingTennisService
      .query({
        'playerId.equals': match.visitorPlayer.id,
        'tournamentGroupId.equals': group.id
      })
      .subscribe(
        (res: HttpResponse<IRankingTennis[]>) => {
          if (res.body.length === 0) {
            console.log('No hay ranking para estos datos.');
            this.createRankingLocalPlayer(match.visitorPlayer, group, match);
          } else {
            console.log('Si hay ranking para estos datos.');
            this.updateRankingLocalPlayer(res.body[0], match);
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  generateRanking(match: IMatchTennis, group: ITournamentGroupTennis) {
    console.log('Generando ranking para el partido' + match.id);
    this.rankingTennisService
      .query({
        'playerId.equals': match.localPlayer.id,
        'tournamentGroupId.equals': group.id
      })
      .subscribe(
        (res: HttpResponse<IRankingTennis[]>) => {
          if (res.body.length === 0) {
            this.createRankingLocalPlayer(match.localPlayer, group, match);
          } else {
            this.updateRankingLocalPlayer(res.body[0], match);
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );

    this.rankingTennisService
      .query({
        'playerId.equals': match.visitorPlayer.id,
        'tournamentGroupId.equals': group.id
      })
      .subscribe(
        (res: HttpResponse<IRankingTennis[]>) => {
          if (res.body.length === 0) {
            this.createRankingVisitorPlayer(match.visitorPlayer, group, match);
          } else {
            this.updateRankingVisitorPlayer(res.body[0], match);
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  createRankingLocalPlayer(player: IPlayerTennis, group: ITournamentGroupTennis, match: IMatchTennis) {
    console.log('Creando ranking para el Jugador Local : ' + player.name + ' en el grupo : ' + group.name);
    const ranking: IRankingTennis = {};
    ranking.player = player;
    ranking.tournamentGroup = group;
    ranking.gamesWin =
      (match.player1Set1Result != null ? match.player1Set1Result : 0) +
      (match.player1Set2Result != null ? match.player1Set2Result : 0) +
      (match.player1Set3Result != null ? match.player1Set3Result : 0);
    ranking.gamesLoss =
      (match.player2Set1Result != null ? match.player2Set1Result : 0) +
      (match.player2Set2Result != null ? match.player2Set2Result : 0) +
      (match.player2Set3Result != null ? match.player2Set3Result : 0);
    // si ha ganado dos sets, si el visitante se ha retirado o si se ha lesionado entonces ha ganado el partido
    if (match.localPlayerSets === 2 || match.visitorPlayerAbandoned || match.visitorPlayerNotPresent) {
      ranking.matchesWined = 1;
      ranking.matchesLoss = 0;
      ranking.points = 3;
    } else {
      ranking.matchesWined = 0;
      ranking.matchesLoss = 1;
      ranking.points = 1;
    }
    ranking.setsWin = match.localPlayerSets;
    ranking.setsLost = match.visitorPlayerSets;
    ranking.matchesPlayed = 1;
    this.rankingTennisService
      .create(ranking)
      .subscribe((res: HttpResponse<any>) => console.log(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  createRankingVisitorPlayer(player: IPlayerTennis, group: ITournamentGroupTennis, match: IMatchTennis) {
    console.log('Creando ranking para el Jugador Visitante : ' + player.name + ' en el grupo : ' + group.name);
    const ranking: IRankingTennis = {};
    ranking.player = player;
    ranking.tournamentGroup = group;
    ranking.gamesLoss =
      (match.player1Set1Result != null ? match.player1Set1Result : 0) +
      (match.player1Set2Result != null ? match.player1Set2Result : 0) +
      (match.player1Set3Result != null ? match.player1Set3Result : 0);
    ranking.gamesWin =
      (match.player2Set1Result != null ? match.player2Set1Result : 0) +
      (match.player2Set2Result != null ? match.player2Set2Result : 0) +
      (match.player2Set3Result != null ? match.player2Set3Result : 0);
    // si ha ganado dos sets, si el visitante se ha retirado o si se ha lesionado entonces ha ganado el partido
    if (match.visitorPlayerSets === 2 || match.localPlayerAbandoned || match.localPlayerNotPresent) {
      ranking.matchesWined = 1;
      ranking.matchesLoss = 0;
      ranking.points = 3;
    } else {
      ranking.matchesWined = 0;
      ranking.matchesLoss = 1;
      ranking.points = 1;
    }
    ranking.setsWin = match.visitorPlayerSets;
    ranking.setsLost = match.localPlayerSets;
    ranking.matchesPlayed = 1;
    this.rankingTennisService
      .create(ranking)
      .subscribe((res: HttpResponse<any>) => console.log(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateRankingLocalPlayer(ranking: IRankingTennis, match: IMatchTennis) {
    console.log('Actualizando ranking para el Jugador Local : ' + ranking.player.name + ' en el grupo : ' + ranking.tournamentGroup.name);
    if (match.localPlayerSets === 2 || match.visitorPlayerAbandoned || match.visitorPlayerNotPresent) {
      ranking.points += 3;
      ranking.matchesWined += 1;
    } else {
      ranking.points += 1;
      ranking.matchesLoss += 1;
    }
    ranking.gamesWin +=
      (match.player1Set1Result != null ? match.player1Set1Result : 0) +
      (match.player1Set2Result != null ? match.player1Set2Result : 0) +
      (match.player1Set3Result != null ? match.player1Set3Result : 0);
    ranking.gamesLoss +=
      (match.player2Set1Result != null ? match.player2Set1Result : 0) +
      (match.player2Set2Result != null ? match.player2Set2Result : 0) +
      (match.player2Set3Result != null ? match.player2Set3Result : 0);
    ranking.setsWin += match.localPlayerSets;
    ranking.setsLost += match.visitorPlayerSets;
    ranking.matchesPlayed += 1;
    this.rankingTennisService
      .update(ranking)
      .subscribe((res: HttpResponse<any>) => console.log(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateRankingVisitorPlayer(ranking: IRankingTennis, match: IMatchTennis) {
    console.log(
      'Actualizando ranking para el Jugador Visitante : ' + ranking.player.name + ' en el grupo : ' + ranking.tournamentGroup.name
    );
    if (match.visitorPlayerSets === 2 || match.localPlayerAbandoned || match.localPlayerNotPresent) {
      ranking.matchesWined += 1;
      ranking.points += 3;
    } else {
      ranking.matchesLoss += 1;
      ranking.points += 1;
    }
    ranking.gamesLoss +=
      (match.player1Set1Result != null ? match.player1Set1Result : 0) +
      (match.player1Set2Result != null ? match.player1Set2Result : 0) +
      (match.player1Set3Result != null ? match.player1Set3Result : 0);
    ranking.gamesWin +=
      (match.player2Set1Result != null ? match.player2Set1Result : 0) +
      (match.player2Set2Result != null ? match.player2Set2Result : 0) +
      (match.player2Set3Result != null ? match.player2Set3Result : 0);
    ranking.setsWin += match.visitorPlayerSets;
    ranking.setsLost += match.localPlayerSets;
    ranking.matchesPlayed += 1;
    this.rankingTennisService
      .update(ranking)
      .subscribe((res: HttpResponse<any>) => console.log(res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }
}
