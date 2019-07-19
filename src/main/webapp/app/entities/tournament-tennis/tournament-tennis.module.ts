import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared';
import {
  TournamentTennisComponent,
  TournamentTennisDetailComponent,
  TournamentTennisUpdateComponent,
  TournamentTennisDeletePopupComponent,
  TournamentTennisDeleteDialogComponent,
  tournamentRoute,
  tournamentPopupRoute
} from './';
import { TournamentTennisGroupListComponent } from 'app/entities/tournament-tennis/tournament-tennis-group-list.component';

const ENTITY_STATES = [...tournamentRoute, ...tournamentPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TournamentTennisComponent,
    TournamentTennisDetailComponent,
    TournamentTennisUpdateComponent,
    TournamentTennisDeleteDialogComponent,
    TournamentTennisDeletePopupComponent,
    TournamentTennisGroupListComponent
  ],
  entryComponents: [
    TournamentTennisComponent,
    TournamentTennisUpdateComponent,
    TournamentTennisDeleteDialogComponent,
    TournamentTennisDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzTournamentTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
