/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { GenderTennisDetailComponent } from 'app/entities/gender-tennis/gender-tennis-detail.component';
import { GenderTennis } from 'app/shared/model/gender-tennis.model';

describe('Component Tests', () => {
  describe('GenderTennis Management Detail Component', () => {
    let comp: GenderTennisDetailComponent;
    let fixture: ComponentFixture<GenderTennisDetailComponent>;
    const route = ({ data: of({ gender: new GenderTennis(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [GenderTennisDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GenderTennisDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GenderTennisDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.gender).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
