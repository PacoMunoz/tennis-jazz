import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from './player-tennis.service';

@Component({
  selector: 'jhi-player-tennis-delete-dialog',
  templateUrl: './player-tennis-delete-dialog.component.html'
})
export class PlayerTennisDeleteDialogComponent {
  player: IPlayerTennis;

  constructor(protected playerService: PlayerTennisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.playerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'playerListModification',
        content: 'Deleted an player'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-player-tennis-delete-popup',
  template: ''
})
export class PlayerTennisDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ player }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlayerTennisDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.player = player;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/player-tennis', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/player-tennis', { outlets: { popup: null } }]);
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
