/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TournamentTennisService } from 'app/entities/tournament-tennis/tournament-tennis.service';
import { ITournamentTennis, TournamentTennis } from 'app/shared/model/tournament-tennis.model';

describe('Service Tests', () => {
  describe('TournamentTennis Service', () => {
    let injector: TestBed;
    let service: TournamentTennisService;
    let httpMock: HttpTestingController;
    let elemDefault: ITournamentTennis;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(TournamentTennisService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new TournamentTennis(0, 'AAAAAAA', currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a TournamentTennis', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            startDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new TournamentTennis(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a TournamentTennis', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            inProgress: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of TournamentTennis', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            inProgress: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            startDate: currentDate
          },
          returnedFromService
        );
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

      it('should delete a TournamentTennis', async () => {
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
