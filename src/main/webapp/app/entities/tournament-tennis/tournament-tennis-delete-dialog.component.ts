import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { TournamentTennisService } from './tournament-tennis.service';

@Component({
  selector: 'jhi-tournament-tennis-delete-dialog',
  templateUrl: './tournament-tennis-delete-dialog.component.html'
})
export class TournamentTennisDeleteDialogComponent {
  tournament: ITournamentTennis;

  constructor(
    protected tournamentService: TournamentTennisService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.tournamentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'tournamentListModification',
        content: 'Deleted an tournament'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-tournament-tennis-delete-popup',
  template: ''
})
export class TournamentTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tournament }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TournamentTennisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.tournament = tournament;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/tournament-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/tournament-tennis', { outlets: { popup: null } }]);
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
