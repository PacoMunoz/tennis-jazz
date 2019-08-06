/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { RankingTennisService } from 'app/entities/ranking-tennis/ranking-tennis.service';
import { IRankingTennis, RankingTennis } from 'app/shared/model/ranking-tennis.model';

describe('Service Tests', () => {
  describe('RankingTennis Service', () => {
    let injector: TestBed;
    let service: RankingTennisService;
    let httpMock: HttpTestingController;
    let elemDefault: IRankingTennis;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(RankingTennisService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new RankingTennis(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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

      it('should create a RankingTennis', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new RankingTennis(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a RankingTennis', async () => {
        const returnedFromService = Object.assign(
          {
            points: 1,
            gamesWin: 1,
            gamesLoss: 1,
            setsWin: 1,
            setsLost: 1,
            matchesPlayed: 1,
            matchesWined: 1,
            matchesLoss: 1,
            matchesNotPresent: 1,
            matchesAbandoned: 1
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

      it('should return a list of RankingTennis', async () => {
        const returnedFromService = Object.assign(
          {
            points: 1,
            gamesWin: 1,
            gamesLoss: 1,
            setsWin: 1,
            setsLost: 1,
            matchesPlayed: 1,
            matchesWined: 1,
            matchesLoss: 1,
            matchesNotPresent: 1,
            matchesAbandoned: 1
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

      it('should delete a RankingTennis', async () => {
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
