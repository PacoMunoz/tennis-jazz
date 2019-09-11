import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GenderTennis } from 'app/shared/model/gender-tennis.model';
import { GenderTennisService } from './gender-tennis.service';
import { GenderTennisComponent } from './gender-tennis.component';
import { GenderTennisDetailComponent } from './gender-tennis-detail.component';
import { GenderTennisUpdateComponent } from './gender-tennis-update.component';
import { GenderTennisDeletePopupComponent } from './gender-tennis-delete-dialog.component';
import { IGenderTennis } from 'app/shared/model/gender-tennis.model';

@Injectable({ providedIn: 'root' })
export class GenderTennisResolve implements Resolve<IGenderTennis> {
  constructor(private service: GenderTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGenderTennis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<GenderTennis>) => response.ok),
        map((gender: HttpResponse<GenderTennis>) => gender.body)
      );
    }
    return of(new GenderTennis());
  }
}

export const genderRoute: Routes = [
  {
    path: '',
    component: GenderTennisComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.gender.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GenderTennisDetailComponent,
    resolve: {
      gender: GenderTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.gender.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GenderTennisUpdateComponent,
    resolve: {
      gender: GenderTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.gender.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GenderTennisUpdateComponent,
    resolve: {
      gender: GenderTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.gender.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const genderPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GenderTennisDeletePopupComponent,
    resolve: {
      gender: GenderTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.gender.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
