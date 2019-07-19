/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { RoundTennisDetailComponent } from 'app/entities/round-tennis/round-tennis-detail.component';
import { RoundTennis } from 'app/shared/model/round-tennis.model';

describe('Component Tests', () => {
  describe('RoundTennis Management Detail Component', () => {
    let comp: RoundTennisDetailComponent;
    let fixture: ComponentFixture<RoundTennisDetailComponent>;
    const route = ({ data: of({ round: new RoundTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [RoundTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RoundTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoundTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.round).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
