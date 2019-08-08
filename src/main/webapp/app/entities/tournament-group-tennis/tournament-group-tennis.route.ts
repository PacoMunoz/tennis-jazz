import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from './tournament-group-tennis.service';
import { TournamentGroupTennisComponent } from './tournament-group-tennis.component';
import { TournamentGroupTennisDetailComponent } from './tournament-group-tennis-detail.component';
import { TournamentGroupTennisUpdateComponent } from './tournament-group-tennis-update.component';
import { TournamentGroupTennisDeletePopupComponent } from './tournament-group-tennis-delete-dialog.component';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisRoundsListComponent } from 'app/entities/tournament-group-tennis/tournament-group-tennis-rounds-list.component';

@Injectable({ providedIn: 'root' })
export class TournamentGroupTennisResolve implements Resolve<ITournamentGroupTennis> {
  constructor(private service: TournamentGroupTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITournamentGroupTennis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TournamentGroupTennis>) => response.ok),
        map((tournamentGroup: HttpResponse<TournamentGroupTennis>) => tournamentGroup.body)
      );
    }
    return of(new TournamentGroupTennis());
  }
}

export const tournamentGroupRoute: Routes = [
  {
    path: '',
    component: TournamentGroupTennisComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.tournamentGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TournamentGroupTennisDetailComponent,
    resolve: {
      tournamentGroup: TournamentGroupTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.tournamentGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view-rounds',
    component: TournamentGroupTennisRoundsListComponent,
    resolve: {
      tournamentGroup: TournamentGroupTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.tournamentGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TournamentGroupTennisUpdateComponent,
    resolve: {
      tournamentGroup: TournamentGroupTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.tournamentGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TournamentGroupTennisUpdateComponent,
    resolve: {
      tournamentGroup: TournamentGroupTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.tournamentGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const tournamentGroupPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TournamentGroupTennisDeletePopupComponent,
    resolve: {
      tournamentGroup: TournamentGroupTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.tournamentGroup.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
