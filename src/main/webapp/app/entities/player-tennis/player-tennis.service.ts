import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

type EntityResponseType = HttpResponse<IPlayerTennis>;
type EntityArrayResponseType = HttpResponse<IPlayerTennis[]>;

@Injectable({ providedIn: 'root' })
export class PlayerTennisService {
  public resourceUrl = SERVER_API_URL + 'api/players';

  constructor(protected http: HttpClient) {}

  create(player: IPlayerTennis): Observable<EntityResponseType> {
    return this.http.post<IPlayerTennis>(this.resourceUrl, player, { observe: 'response' });
  }

  update(player: IPlayerTennis): Observable<EntityResponseType> {
    return this.http.put<IPlayerTennis>(this.resourceUrl, player, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayerTennis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayerTennis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
