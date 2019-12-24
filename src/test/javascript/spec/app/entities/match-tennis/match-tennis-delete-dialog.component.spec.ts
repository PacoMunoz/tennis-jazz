import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TennisJazzTestModule } from '../../../test.module';
import { MatchTennisDeleteDialogComponent } from 'app/entities/match-tennis/match-tennis-delete-dialog.component';
import { MatchTennisService } from 'app/entities/match-tennis/match-tennis.service';

describe('Component Tests', () => {
  describe('MatchTennis Management Delete Component', () => {
    let comp: MatchTennisDeleteDialogComponent;
    let fixture: ComponentFixture<MatchTennisDeleteDialogComponent>;
    let service: MatchTennisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [MatchTennisDeleteDialogComponent]
      })
        .overrideTemplate(MatchTennisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MatchTennisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MatchTennisService);
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
