import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
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

      elemDefault = new MatchTennis(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a MatchTennis', () => {
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

      it('should update a MatchTennis', () => {
        const returnedFromService = Object.assign(
          {
            localPlayerSet1Result: 1,
            visitorPlayerSet1Result: 1,
            localPlayerTBSet1Result: 1,
            visitorPlayerTBSet1Result: 1,
            localPlayerTBSet2Result: 1,
            visitorPlayerTBSet2Result: 1,
            localPlayerTBSet3Result: 1,
            visitorPlayerTBSet3Result: 1,
            localPlayerSet2Result: 1,
            visitorPlayerSet2Result: 1,
            localPlayerSet3Result: 1,
            visitorPlayerSet3Result: 1,
            localPlayerSets: 1,
            visitorPlayerSets: 1,
            localPlayerAbandoned: true,
            visitorPlayerAbandoned: true,
            localPlayerNotPresent: true,
            visitorPlayerNotPresent: true,
            postponed: true
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

      it('should return a list of MatchTennis', () => {
        const returnedFromService = Object.assign(
          {
            localPlayerSet1Result: 1,
            visitorPlayerSet1Result: 1,
            localPlayerTBSet1Result: 1,
            visitorPlayerTBSet1Result: 1,
            localPlayerTBSet2Result: 1,
            visitorPlayerTBSet2Result: 1,
            localPlayerTBSet3Result: 1,
            visitorPlayerTBSet3Result: 1,
            localPlayerSet2Result: 1,
            visitorPlayerSet2Result: 1,
            localPlayerSet3Result: 1,
            visitorPlayerSet3Result: 1,
            localPlayerSets: 1,
            visitorPlayerSets: 1,
            localPlayerAbandoned: true,
            visitorPlayerAbandoned: true,
            localPlayerNotPresent: true,
            visitorPlayerNotPresent: true,
            postponed: true
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

      it('should delete a MatchTennis', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

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
