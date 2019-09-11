import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGenderTennis } from 'app/shared/model/gender-tennis.model';

type EntityResponseType = HttpResponse<IGenderTennis>;
type EntityArrayResponseType = HttpResponse<IGenderTennis[]>;

@Injectable({ providedIn: 'root' })
export class GenderTennisService {
  public resourceUrl = SERVER_API_URL + 'api/genders';

  constructor(protected http: HttpClient) {}

  create(gender: IGenderTennis): Observable<EntityResponseType> {
    return this.http.post<IGenderTennis>(this.resourceUrl, gender, { observe: 'response' });
  }

  update(gender: IGenderTennis): Observable<EntityResponseType> {
    return this.http.put<IGenderTennis>(this.resourceUrl, gender, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGenderTennis>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGenderTennis[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
