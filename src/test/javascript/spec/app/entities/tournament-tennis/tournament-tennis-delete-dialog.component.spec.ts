/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TennisJazzTestModule } from '../../../test.module';
import { TournamentTennisDeleteDialogComponent } from 'app/entities/tournament-tennis/tournament-tennis-delete-dialog.component';
import { TournamentTennisService } from 'app/entities/tournament-tennis/tournament-tennis.service';

describe('Component Tests', () => {
  describe('TournamentTennis Management Delete Component', () => {
    let comp: TournamentTennisDeleteDialogComponent;
    let fixture: ComponentFixture<TournamentTennisDeleteDialogComponent>;
    let service: TournamentTennisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [TournamentTennisDeleteDialogComponent]
      })
        .overrideTemplate(TournamentTennisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournamentTennisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournamentTennisService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
