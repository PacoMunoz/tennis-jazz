/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { TournamentGroupTennisDetailComponent } from 'app/entities/tournament-group-tennis/tournament-group-tennis-detail.component';
import { TournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

describe('Component Tests', () => {
  describe('TournamentGroupTennis Management Detail Component', () => {
    let comp: TournamentGroupTennisDetailComponent;
    let fixture: ComponentFixture<TournamentGroupTennisDetailComponent>;
    const route = ({ data: of({ tournamentGroup: new TournamentGroupTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [TournamentGroupTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TournamentGroupTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournamentGroupTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tournamentGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
