/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { TournamentTennisUpdateComponent } from 'app/entities/tournament-tennis/tournament-tennis-update.component';
import { TournamentTennisService } from 'app/entities/tournament-tennis/tournament-tennis.service';
import { TournamentTennis } from 'app/shared/model/tournament-tennis.model';

describe('Component Tests', () => {
  describe('TournamentTennis Management Update Component', () => {
    let comp: TournamentTennisUpdateComponent;
    let fixture: ComponentFixture<TournamentTennisUpdateComponent>;
    let service: TournamentTennisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [TournamentTennisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TournamentTennisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournamentTennisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournamentTennisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TournamentTennis(123);
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
        const entity = new TournamentTennis();
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
