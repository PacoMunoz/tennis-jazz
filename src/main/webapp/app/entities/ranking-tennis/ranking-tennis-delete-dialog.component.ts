import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';
import { RankingTennisService } from './ranking-tennis.service';

@Component({
  selector: 'jhi-ranking-tennis-delete-dialog',
  templateUrl: './ranking-tennis-delete-dialog.component.html'
})
export class RankingTennisDeleteDialogComponent {
  ranking: IRankingTennis;

  constructor(
    protected rankingService: RankingTennisService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rankingService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'rankingListModification',
        content: 'Deleted an ranking'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ranking-tennis-delete-popup',
  template: ''
})
export class RankingTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ranking }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RankingTennisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.ranking = ranking;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ranking-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ranking-tennis', { outlets: { popup: null } }]);
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
