import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TennisJazzTestModule } from '../../../test.module';
import { PlayerTennisUpdateComponent } from 'app/entities/player-tennis/player-tennis-update.component';
import { PlayerTennisService } from 'app/entities/player-tennis/player-tennis.service';
import { PlayerTennis } from 'app/shared/model/player-tennis.model';

describe('Component Tests', () => {
  describe('PlayerTennis Management Update Component', () => {
    let comp: PlayerTennisUpdateComponent;
    let fixture: ComponentFixture<PlayerTennisUpdateComponent>;
    let service: PlayerTennisService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [PlayerTennisUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PlayerTennisUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayerTennisUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PlayerTennisService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PlayerTennis(123);
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
        const entity = new PlayerTennis();
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
