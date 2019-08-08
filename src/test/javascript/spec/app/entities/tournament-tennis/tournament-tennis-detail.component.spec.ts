/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { TournamentTennisDetailComponent } from 'app/entities/tournament-tennis/tournament-tennis-detail.component';
import { TournamentTennis } from 'app/shared/model/tournament-tennis.model';

describe('Component Tests', () => {
  describe('TournamentTennis Management Detail Component', () => {
    let comp: TournamentTennisDetailComponent;
    let fixture: ComponentFixture<TournamentTennisDetailComponent>;
    const route = ({ data: of({ tournament: new TournamentTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [TournamentTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TournamentTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournamentTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tournament).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
