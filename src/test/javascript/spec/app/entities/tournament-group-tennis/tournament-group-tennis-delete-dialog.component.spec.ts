/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TennisJazzTestModule } from '../../../test.module';
import { TournamentGroupTennisDeleteDialogComponent } from 'app/entities/tournament-group-tennis/tournament-group-tennis-delete-dialog.component';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis/tournament-group-tennis.service';

describe('Component Tests', () => {
  describe('TournamentGroupTennis Management Delete Component', () => {
    let comp: TournamentGroupTennisDeleteDialogComponent;
    let fixture: ComponentFixture<TournamentGroupTennisDeleteDialogComponent>;
    let service: TournamentGroupTennisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [TournamentGroupTennisDeleteDialogComponent]
      })
        .overrideTemplate(TournamentGroupTennisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TournamentGroupTennisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TournamentGroupTennisService);
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
