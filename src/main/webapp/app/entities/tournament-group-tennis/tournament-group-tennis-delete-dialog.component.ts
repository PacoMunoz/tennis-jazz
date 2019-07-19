import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from './tournament-group-tennis.service';

@Component({
  selector: 'jhi-tournament-group-tennis-delete-dialog',
  templateUrl: './tournament-group-tennis-delete-dialog.component.html'
})
export class TournamentGroupTennisDeleteDialogComponent {
  tournamentGroup: ITournamentGroupTennis;

  constructor(
    protected tournamentGroupService: TournamentGroupTennisService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.tournamentGroupService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'tournamentGroupListModification',
        content: 'Deleted an tournamentGroup'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-tournament-group-tennis-delete-popup',
  template: ''
})
export class TournamentGroupTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tournamentGroup }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TournamentGroupTennisDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.tournamentGroup = tournamentGroup;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/tournament-group-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/tournament-group-tennis', { outlets: { popup: null } }]);
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
