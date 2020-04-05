import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable, isDevMode } from '@angular/core';
import { MatchTennisService } from 'app/entities/match-tennis/match-tennis.service';
import { HttpResponse } from '@angular/common/http';
import { IMatchTennis } from 'app/shared/model/match-tennis.model';
import { AccountService, LoginModalService, StateStorageService, UserRouteAccessService, UserService } from 'app/core';
import { map, switchMap } from 'rxjs/operators';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';

@Injectable({ providedIn: 'root' })
export class MatchTennisRouteAccessService implements CanActivate {
  match: IMatchTennis;

  constructor(
    private matchService: MatchTennisService,
    private playerService: PlayerTennisService,
    private userService: UserService,
    private accountService: AccountService,
    private stateStorageService: StateStorageService,
    private router: Router,
    private loginModalService: LoginModalService,
    private userRouteAccessService: UserRouteAccessService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const authorities = route.data['authorities'];
    const idMatch = route.paramMap.get('id');
    // We need to call the checkLogin / and so the accountService.identity() function, to ensure,
    // that the client has a principal too, if they already logged in by the server.
    // This could happen on a page refresh.
    return this.userRouteAccessService.checkLogin(authorities, state.url) && this.checkMatchOwner(idMatch, state.url);
  }

  checkMatchOwner(idMatch: string, url: string): Promise<boolean> {
    return this.matchService
      .find(+idMatch)
      .pipe(
        map(match => (this.match = match.body)),
        switchMap(() => this.accountService.identity()),
        switchMap(account => this.userService.find(account.login)),
        switchMap(user =>
          this.playerService.query({
            'userId.equals': user.body.id
          })
        ),
        map((response: HttpResponse<IPlayerTennis[]>) => {
          if (response.body.length !== 1) {
            this.stateStorageService.storeUrl(url);
            return false;
          }
          if (this.match.localPlayer.id === response.body[0].id || this.match.visitorPlayer.id === response.body[0].id) {
            return true;
          }
          this.stateStorageService.storeUrl(url);
          this.router.navigate(['accessdenied']);
          return false;
        })
      )
      .toPromise();
  }
}
