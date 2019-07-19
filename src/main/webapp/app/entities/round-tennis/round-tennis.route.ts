import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RoundTennis } from 'app/shared/model/round-tennis.model';
import { RoundTennisService } from './round-tennis.service';
import { RoundTennisComponent } from './round-tennis.component';
import { RoundTennisDetailComponent } from './round-tennis-detail.component';
import { RoundTennisUpdateComponent } from './round-tennis-update.component';
import { RoundTennisDeletePopupComponent } from './round-tennis-delete-dialog.component';
import { IRoundTennis } from 'app/shared/model/round-tennis.model';

@Injectable({ providedIn: 'root' })
export class RoundTennisResolve implements Resolve<IRoundTennis> {
  constructor(private service: RoundTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRoundTennis> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<RoundTennis>) => response.ok),
        map((round: HttpResponse<RoundTennis>) => round.body)
      );
    }
    return of(new RoundTennis());
  }
}

export const roundRoute: Routes = [
  {
    path: '',
    component: RoundTennisComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.round.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RoundTennisDetailComponent,
    resolve: {
      round: RoundTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.round.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RoundTennisUpdateComponent,
    resolve: {
      round: RoundTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.round.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RoundTennisUpdateComponent,
    resolve: {
      round: RoundTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.round.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const roundPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RoundTennisDeletePopupComponent,
    resolve: {
      round: RoundTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.round.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
