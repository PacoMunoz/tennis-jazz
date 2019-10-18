import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared/shared.module';
import { PlayerTennisComponent } from './player-tennis.component';
import { PlayerTennisDetailComponent } from './player-tennis-detail.component';
import { PlayerTennisUpdateComponent } from './player-tennis-update.component';
import { PlayerTennisDeletePopupComponent, PlayerTennisDeleteDialogComponent } from './player-tennis-delete-dialog.component';
import { playerRoute, playerPopupRoute } from './player-tennis.route';

const ENTITY_STATES = [...playerRoute, ...playerPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PlayerTennisComponent,
    PlayerTennisDetailComponent,
    PlayerTennisUpdateComponent,
    PlayerTennisDeleteDialogComponent,
    PlayerTennisDeletePopupComponent
  ],
  entryComponents: [
    PlayerTennisComponent,
    PlayerTennisUpdateComponent,
    PlayerTennisDeleteDialogComponent,
    PlayerTennisDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzPlayerTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
