import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

type EntityResponseType = HttpResponse<IRankingTennis>;
type EntityArrayResponseType = HttpResponse<IRankingTennis[]>;

@Injectable({ providedIn: 'root' })
export class RankingTennisService {
  public resourceUrl = SERVER_API_URL + 'api/rankings';

  constructor(protected http: HttpClient) {}

  create(ranking: IRankingTennis): Observable<EntityResponseType> {
    return this.http.post<IRankingTennis>(this.resourceUrl, ranking, { observe: 'response' });
  }

  update(ranking: IRankingTennis): Observable<EntityResponseType> {
    return this.http.put<IRankingTennis>(this.resourceUrl, ranking, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRankingTennis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRankingTennis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  updateTournamentPlayerRanking(player: IPlayerTennis, group: ITournamentGroupTennis) {
    let points: number;
    let gamesWon: number;
    let gamesLoss: number;
    let setsWon: number;
    let setsLoss: number;
    let matchesLoss: number;
    let matchesWon: number;
    let notPresentMatches: number;
    let abandonedMathes: number;
  }
}
