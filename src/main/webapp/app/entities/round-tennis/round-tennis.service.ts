import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';

type EntityResponseType = HttpResponse<IRoundTennis>;
type EntityArrayResponseType = HttpResponse<IRoundTennis[]>;

@Injectable({ providedIn: 'root' })
export class RoundTennisService {
  public resourceUrl = SERVER_API_URL + 'api/rounds';

  constructor(protected http: HttpClient) {}

  create(round: IRoundTennis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(round);
    return this.http
      .post<IRoundTennis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(round: IRoundTennis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(round);
    return this.http
      .put<IRoundTennis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRoundTennis>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRoundTennis[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  queryAll(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRoundTennis[]>(this.resourceUrl + '/all', { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(round: IRoundTennis): IRoundTennis {
    const copy: IRoundTennis = Object.assign({}, round, {
      startDate: round.startDate != null && round.startDate.isValid() ? round.startDate.format(DATE_FORMAT) : null,
      endDate: round.endDate != null && round.endDate.isValid() ? round.endDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
      res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((round: IRoundTennis) => {
        round.startDate = round.startDate != null ? moment(round.startDate) : null;
        round.endDate = round.endDate != null ? moment(round.endDate) : null;
      });
    }
    return res;
  }
}
