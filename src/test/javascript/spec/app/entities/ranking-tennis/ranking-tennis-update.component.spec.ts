/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { RankingTennisUpdateComponent } from 'app/entities/ranking-tennis/ranking-tennis-update.component';
import { RankingTennisService } from 'app/entities/ranking-tennis/ranking-tennis.service';
import { RankingTennis } from 'app/shared/model/ranking-tennis.model';

describe('Component Tests', () => {
  describe('RankingTennis Management Update Component', () => {
    let comp: RankingTennisUpdateComponent;
    let fixture: ComponentFixture<RankingTennisUpdateComponent>;
    let service: RankingTennisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [RankingTennisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(RankingTennisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RankingTennisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RankingTennisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RankingTennis(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new RankingTennis();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
