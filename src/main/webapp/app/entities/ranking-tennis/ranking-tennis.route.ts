import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { RankingTennis } from 'app/shared/model/ranking-tennis.model';
import { RankingTennisService } from './ranking-tennis.service';
import { RankingTennisComponent } from './ranking-tennis.component';
import { RankingTennisDetailComponent } from './ranking-tennis-detail.component';
import { RankingTennisUpdateComponent } from './ranking-tennis-update.component';
import { RankingTennisDeletePopupComponent } from './ranking-tennis-delete-dialog.component';
import { IRankingTennis } from 'app/shared/model/ranking-tennis.model';

@Injectable({ providedIn: 'root' })
export class RankingTennisResolve implements Resolve<IRankingTennis> {
  constructor(private service: RankingTennisService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRankingTennis> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<RankingTennis>) => response.ok),
        map((ranking: HttpResponse<RankingTennis>) => ranking.body)
      );
    }
    return of(new RankingTennis());
  }
}

export const rankingRoute: Routes = [
  {
    path: '',
    component: RankingTennisComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.ranking.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RankingTennisDetailComponent,
    resolve: {
      ranking: RankingTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.ranking.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RankingTennisUpdateComponent,
    resolve: {
      ranking: RankingTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.ranking.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RankingTennisUpdateComponent,
    resolve: {
      ranking: RankingTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.ranking.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const rankingPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RankingTennisDeletePopupComponent,
    resolve: {
      ranking: RankingTennisResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'tennisJazzApp.ranking.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
