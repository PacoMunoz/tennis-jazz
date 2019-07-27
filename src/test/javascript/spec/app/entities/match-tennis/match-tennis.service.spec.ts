/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { MatchTennisService } from 'app/entities/match-tennis/match-tennis.service';
import { IMatchTennis, MatchTennis } from 'app/shared/model/match-tennis.model';

describe('Service Tests', () => {
  describe('MatchTennis Service', () => {
    let injector: TestBed;
    let service: MatchTennisService;
    let httpMock: HttpTestingController;
    let elemDefault: IMatchTennis;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(MatchTennisService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new MatchTennis(0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a MatchTennis', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new MatchTennis(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a MatchTennis', async () => {
        const returnedFromService = Object.assign(
          {
            player1Set1Result: 1,
            player2Set1Result: 1,
            player1Set2Result: 1,
            player2Set2Result: 1,
            player1Set3Result: 1,
            player2Set3Result: 1,
            localPlayerSets: 1,
            visitorPlayerSets: 1,
            localPlayerAbandoned: true,
            visitorPlayerAbandoned: true,
            localPlayerNotPresent: true,
            visitorPlayerNotPresent: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of MatchTennis', async () => {
        const returnedFromService = Object.assign(
          {
            player1Set1Result: 1,
            player2Set1Result: 1,
            player1Set2Result: 1,
            player2Set2Result: 1,
            player1Set3Result: 1,
            player2Set3Result: 1,
            localPlayerSets: 1,
            visitorPlayerSets: 1,
            localPlayerAbandoned: true,
            visitorPlayerAbandoned: true,
            localPlayerNotPresent: true,
            visitorPlayerNotPresent: true
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MatchTennis', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
