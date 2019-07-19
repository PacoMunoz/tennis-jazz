import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared';
import {
  TournamentGroupTennisComponent,
  TournamentGroupTennisDetailComponent,
  TournamentGroupTennisUpdateComponent,
  TournamentGroupTennisDeletePopupComponent,
  TournamentGroupTennisDeleteDialogComponent,
  tournamentGroupRoute,
  tournamentGroupPopupRoute
} from './';
import { TournamentGroupTennisRoundsListComponent } from 'app/entities/tournament-group-tennis/tournament-group-tennis-rounds-list.component';
import { RoundTennisMatchListComponent } from 'app/entities/round-tennis/round-tennis-match-list.component';

const ENTITY_STATES = [...tournamentGroupRoute, ...tournamentGroupPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TournamentGroupTennisComponent,
    TournamentGroupTennisDetailComponent,
    TournamentGroupTennisUpdateComponent,
    TournamentGroupTennisDeleteDialogComponent,
    TournamentGroupTennisDeletePopupComponent,
    TournamentGroupTennisRoundsListComponent,
    RoundTennisMatchListComponent
  ],
  entryComponents: [
    TournamentGroupTennisComponent,
    TournamentGroupTennisUpdateComponent,
    TournamentGroupTennisDeleteDialogComponent,
    TournamentGroupTennisDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzTournamentGroupTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
