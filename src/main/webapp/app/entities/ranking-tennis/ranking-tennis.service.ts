import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';
import { MatchTennisService } from 'app/entities/match-tennis';

type EntityResponseType = HttpResponse<IRankingTennis>;
type EntityArrayResponseType = HttpResponse<IRankingTennis[]>;

@Injectable({ providedIn: 'root' })
export class RankingTennisService {
  public resourceUrl = SERVER_API_URL + 'api/rankings';
  public resourceUrlUpdate = SERVER_API_URL + 'api/rankings/update';

  constructor(protected http: HttpClient, protected matchesService: MatchTennisService) {}

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

  updateTournamentPlayerRanking(idPlayer: number, idRound: number) {
    const options = createRequestOption({ idPlayer: idPlayer, idRound: idRound });
    return this.http.get<any>(this.resourceUrlUpdate, { params: options, observe: 'response' });
  }
}
