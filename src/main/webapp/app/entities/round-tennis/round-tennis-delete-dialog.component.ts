import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoundTennis } from 'app/shared/model/round-tennis.model';
import { RoundTennisService } from './round-tennis.service';

@Component({
  selector: 'jhi-round-tennis-delete-dialog',
  templateUrl: './round-tennis-delete-dialog.component.html'
})
export class RoundTennisDeleteDialogComponent {
  round: IRoundTennis;

  constructor(protected roundService: RoundTennisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.roundService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'roundListModification',
        content: 'Deleted an round'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-round-tennis-delete-popup',
  template: ''
})
export class RoundTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ round }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RoundTennisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.round = round;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/round-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/round-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
