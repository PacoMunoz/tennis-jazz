import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { TournamentTennisService } from './tournament-tennis.service';
import { TournamentTennisComponent } from './tournament-tennis.component';
import { TournamentTennisUpdateComponent } from './tournament-tennis-update.component';
import { TournamentTennisDeletePopupComponent } from './tournament-tennis-delete-dialog.component';
import { ITournamentTennis } from 'app/shared/model/tournament-tennis.model';
import { TournamentTennisViewComponent } from 'app/entities/tournament-tennis/tournament-tennis-view-component';

@Injectable({ providedIn: 'root' })
export class TournamentTennisResolve implements Resolve<ITournamentTennis> {
  constructor(private service: TournamentTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITournamentTennis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TournamentTennis>) => response.ok),
        map((tournament: HttpResponse<TournamentTennis>) => tournament.body)
      );
    }
    return of(new TournamentTennis());
  }
}

export const tournamentRoute: Routes = [
  {
    path: '',
    component: TournamentTennisComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.tournament.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TournamentTennisViewComponent,
    resolve: {
      tournament: TournamentTennisResolve
    },
    data: {
      authorities: [],
      pageTitle: 'tennisJazzApp.tournament.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TournamentTennisUpdateComponent,
    resolve: {
      tournament: TournamentTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.tournament.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TournamentTennisUpdateComponent,
    resolve: {
      tournament: TournamentTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.tournament.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const tournamentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TournamentTennisDeletePopupComponent,
    resolve: {
      tournament: TournamentTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.tournament.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
