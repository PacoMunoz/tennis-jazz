import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

type EntityResponseType = HttpResponse<ITournamentGroupTennis>;
type EntityArrayResponseType = HttpResponse<ITournamentGroupTennis[]>;

@Injectable({ providedIn: 'root' })
export class TournamentGroupTennisService {
  public resourceUrl = SERVER_API_URL + 'api/tournament-groups';

  constructor(protected http: HttpClient) {}

  create(tournamentGroup: ITournamentGroupTennis): Observable<EntityResponseType> {
    return this.http.post<ITournamentGroupTennis>(this.resourceUrl, tournamentGroup, { observe: 'response' });
  }

  update(tournamentGroup: ITournamentGroupTennis): Observable<EntityResponseType> {
    return this.http.put<ITournamentGroupTennis>(this.resourceUrl, tournamentGroup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITournamentGroupTennis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITournamentGroupTennis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
