/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { RankingTennisDetailComponent } from 'app/entities/ranking-tennis/ranking-tennis-detail.component';
import { RankingTennis } from 'app/shared/model/ranking-tennis.model';

describe('Component Tests', () => {
  describe('RankingTennis Management Detail Component', () => {
    let comp: RankingTennisDetailComponent;
    let fixture: ComponentFixture<RankingTennisDetailComponent>;
    const route = ({ data: of({ ranking: new RankingTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [RankingTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RankingTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RankingTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ranking).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
