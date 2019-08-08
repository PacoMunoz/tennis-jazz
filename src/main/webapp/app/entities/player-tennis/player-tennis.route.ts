import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from './player-tennis.service';
import { PlayerTennisComponent } from './player-tennis.component';
import { PlayerTennisDetailComponent } from './player-tennis-detail.component';
import { PlayerTennisUpdateComponent } from './player-tennis-update.component';
import { PlayerTennisDeletePopupComponent } from './player-tennis-delete-dialog.component';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

@Injectable({ providedIn: 'root' })
export class PlayerTennisResolve implements Resolve<IPlayerTennis> {
  constructor(private service: PlayerTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPlayerTennis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PlayerTennis>) => response.ok),
        map((player: HttpResponse<PlayerTennis>) => player.body)
      );
    }
    return of(new PlayerTennis());
  }
}

export const playerRoute: Routes = [
  {
    path: '',
    component: PlayerTennisComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlayerTennisDetailComponent,
    resolve: {
      player: PlayerTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlayerTennisUpdateComponent,
    resolve: {
      player: PlayerTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlayerTennisUpdateComponent,
    resolve: {
      player: PlayerTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.player.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const playerPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PlayerTennisDeletePopupComponent,
    resolve: {
      player: PlayerTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.player.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
