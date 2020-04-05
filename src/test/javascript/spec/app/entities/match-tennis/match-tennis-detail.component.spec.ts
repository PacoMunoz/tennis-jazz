import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { MatchTennisDetailComponent } from 'app/entities/match-tennis/match-tennis-detail.component';
import { MatchTennis } from 'app/shared/model/match-tennis.model';

describe('Component Tests', () => {
  describe('MatchTennis Management Detail Component', () => {
    let comp: MatchTennisDetailComponent;
    let fixture: ComponentFixture<MatchTennisDetailComponent>;
    const route = ({ data: of({ match: new MatchTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [MatchTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MatchTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MatchTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.match).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
