import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGenderTennis } from 'app/shared/model/gender-tennis.model';
import { GenderTennisService } from './gender-tennis.service';

@Component({
  selector: 'jhi-gender-tennis-delete-dialog',
  templateUrl: './gender-tennis-delete-dialog.component.html'
})
export class GenderTennisDeleteDialogComponent {
  gender: IGenderTennis;

  constructor(protected genderService: GenderTennisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.genderService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'genderListModification',
        content: 'Deleted an gender'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-gender-tennis-delete-popup',
  template: ''
})
export class GenderTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ gender }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GenderTennisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.gender = gender;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/gender-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/gender-tennis', { outlets: { popup: null } }]);
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
