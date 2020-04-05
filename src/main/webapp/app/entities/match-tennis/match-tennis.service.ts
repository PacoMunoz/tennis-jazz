import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';

type EntityResponseType = HttpResponse<IMatchTennis>;
type EntityArrayResponseType = HttpResponse<IMatchTennis[]>;

@Injectable({ providedIn: 'root' })
export class MatchTennisService {
  public resourceUrl = SERVER_API_URL + 'api/matches';

  constructor(protected http: HttpClient) {}

  create(match: IMatchTennis): Observable<EntityResponseType> {
    return this.http.post<IMatchTennis>(this.resourceUrl, match, { observe: 'response' });
  }

  update(match: IMatchTennis): Observable<EntityResponseType> {
    return this.http.put<IMatchTennis>(this.resourceUrl, match, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatchTennis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatchTennis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllCurrent(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatchTennis[]>(`${this.resourceUrl}/current-matches`, { params: options, observe: 'response' });
  }
}
