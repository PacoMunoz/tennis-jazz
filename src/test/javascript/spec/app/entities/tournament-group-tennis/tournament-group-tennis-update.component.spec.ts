/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { TournamentGroupTennisUpdateComponent } from 'app/entities/tournament-group-tennis/tournament-group-tennis-update.component';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis/tournament-group-tennis.service';
import { TournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';

describe('Component Tests', () => {
  describe('TournamentGroupTennis Management Update Component', () => {
    let comp: TournamentGroupTennisUpdateComponent;
    let fixture: ComponentFixture<TournamentGroupTennisUpdateComponent>;
    let service: TournamentGroupTennisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [TournamentGroupTennisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TournamentGroupTennisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TournamentGroupTennisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournamentGroupTennisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TournamentGroupTennis(123);
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
        const entity = new TournamentGroupTennis();
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
