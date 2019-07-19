import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from './match-tennis.service';

@Component({
  selector: 'jhi-match-tennis-delete-dialog',
  templateUrl: './match-tennis-delete-dialog.component.html'
})
export class MatchTennisDeleteDialogComponent {
  match: IMatchTennis;

  constructor(protected matchService: MatchTennisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.matchService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'matchListModification',
        content: 'Deleted an match'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-match-tennis-delete-popup',
  template: ''
})
export class MatchTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ match }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MatchTennisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.match = match;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/match-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/match-tennis', { outlets: { popup: null } }]);
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
