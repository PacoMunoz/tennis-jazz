/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { GenderTennisUpdateComponent } from 'app/entities/gender-tennis/gender-tennis-update.component';
import { GenderTennisService } from 'app/entities/gender-tennis/gender-tennis.service';
import { GenderTennis } from 'app/shared/model/gender-tennis.model';

describe('Component Tests', () => {
  describe('GenderTennis Management Update Component', () => {
    let comp: GenderTennisUpdateComponent;
    let fixture: ComponentFixture<GenderTennisUpdateComponent>;
    let service: GenderTennisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [GenderTennisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GenderTennisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GenderTennisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GenderTennisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new GenderTennis(123);
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
        const entity = new GenderTennis();
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
