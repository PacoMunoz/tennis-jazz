import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisService } from './match-tennis.service';
import { MatchTennisComponent } from './match-tennis.component';
import { MatchTennisDetailComponent } from './match-tennis-detail.component';
import { MatchTennisUpdateComponent } from './match-tennis-update.component';
import { MatchTennisDeletePopupComponent } from './match-tennis-delete-dialog.component';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { MatchTennisNewUpdateComponent } from 'app/entities/match-tennis/match-tennis-new-update.component';
import { MatchTennisRouteAccessService } from 'app/entities/match-tennis/match-tennis-route-access.service';

@Injectable({ providedIn: 'root' })
export class MatchTennisResolve implements Resolve<IMatchTennis> {
  constructor(private service: MatchTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMatchTennis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MatchTennis>) => response.ok),
        map((match: HttpResponse<MatchTennis>) => match.body)
      );
    }
    return of(new MatchTennis());
  }
}

export const matchRoute: Routes = [
  {
    path: '',
    component: MatchTennisComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MatchTennisDetailComponent,
    resolve: {
      match: MatchTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MatchTennisUpdateComponent,
    resolve: {
      match: MatchTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MatchTennisUpdateComponent,
    resolve: {
      match: MatchTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/new-or-update',
    component: MatchTennisNewUpdateComponent,
    resolve: {
      match: MatchTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/new-or-update-current',
    component: MatchTennisNewUpdateComponent,
    resolve: {
      match: MatchTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [MatchTennisRouteAccessService]
  }
];

export const matchPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MatchTennisDeletePopupComponent,
    resolve: {
      match: MatchTennisResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'tennisJazzApp.match.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
