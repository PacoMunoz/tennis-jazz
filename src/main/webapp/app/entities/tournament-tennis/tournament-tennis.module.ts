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
import { TournamentTennisGroupRankingComponent } from 'app/entities/tournament-tennis/tournament-tennis-group-ranking.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TournamentTennisViewComponent } from 'app/entities/tournament-tennis/tournament-tennis-view-component';
import { TournamentTennisGroupRounds } from 'app/entities/tournament-tennis/tournament-tennis-group-rounds';
import { TournamentTennisGroupRoundMatchComponent } from 'app/entities/tournament-tennis/tournament-tennis-group-round-match.component';
import { TournamentTennisGroupPlayerComponent } from 'app/entities/tournament-tennis/tournament-tennis-group-player.component';

const ENTITY_STATES = [...tournamentRoute, ...tournamentPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES), FlexLayoutModule],
  declarations: [
    TournamentTennisComponent,
    TournamentTennisDetailComponent,
    TournamentTennisUpdateComponent,
    TournamentTennisDeleteDialogComponent,
    TournamentTennisDeletePopupComponent,
    TournamentTennisGroupListComponent,
    TournamentTennisViewComponent,
    TournamentTennisGroupRankingComponent,
    TournamentTennisGroupRounds,
    TournamentTennisGroupRoundMatchComponent,
    TournamentTennisGroupPlayerComponent
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
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
