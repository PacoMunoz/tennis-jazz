/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TennisJazzTestModule } from '../../../test.module';
import { RankingTennisDeleteDialogComponent } from 'app/entities/ranking-tennis/ranking-tennis-delete-dialog.component';
import { RankingTennisService } from 'app/entities/ranking-tennis/ranking-tennis.service';

describe('Component Tests', () => {
  describe('RankingTennis Management Delete Component', () => {
    let comp: RankingTennisDeleteDialogComponent;
    let fixture: ComponentFixture<RankingTennisDeleteDialogComponent>;
    let service: RankingTennisService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TennisJazzTestModule],
        declarations: [RankingTennisDeleteDialogComponent]
      })
        .overrideTemplate(RankingTennisDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RankingTennisDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RankingTennisService);
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
