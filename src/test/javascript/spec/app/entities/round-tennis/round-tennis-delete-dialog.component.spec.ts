/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TennisJazzTestModule } from '../../../test.module';
import { RoundTennisDeleteDialogComponent } from 'app/entities/round-tennis/round-tennis-delete-dialog.component';
import { RoundTennisService } from 'app/entities/round-tennis/round-tennis.service';

describe('Component Tests', () => {
  describe('RoundTennis Management Delete Component', () => {
    let comp: RoundTennisDeleteDialogComponent;
    let fixture: ComponentFixture<RoundTennisDeleteDialogComponent>;
    let service: RoundTennisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [RoundTennisDeleteDialogComponent]
      })
        .overrideTemplate(RoundTennisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RoundTennisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RoundTennisService);
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
