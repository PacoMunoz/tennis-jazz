import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { MatchTennisUpdateComponent } from 'app/entities/match-tennis/match-tennis-update.component';
import { MatchTennisService } from 'app/entities/match-tennis/match-tennis.service';
import { MatchTennis } from 'app/shared/model/match-tennis.model';

describe('Component Tests', () => {
  describe('MatchTennis Management Update Component', () => {
    let comp: MatchTennisUpdateComponent;
    let fixture: ComponentFixture<MatchTennisUpdateComponent>;
    let service: MatchTennisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [MatchTennisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MatchTennisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MatchTennisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MatchTennisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MatchTennis(123);
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
        const entity = new MatchTennis();
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
