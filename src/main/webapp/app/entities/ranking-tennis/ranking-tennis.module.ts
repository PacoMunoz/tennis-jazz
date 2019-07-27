import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared';
import {
  RankingTennisComponent,
  RankingTennisDetailComponent,
  RankingTennisUpdateComponent,
  RankingTennisDeletePopupComponent,
  RankingTennisDeleteDialogComponent,
  rankingRoute,
  rankingPopupRoute
} from './';

const ENTITY_STATES = [...rankingRoute, ...rankingPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RankingTennisComponent,
    RankingTennisDetailComponent,
    RankingTennisUpdateComponent,
    RankingTennisDeleteDialogComponent,
    RankingTennisDeletePopupComponent
  ],
  entryComponents: [
    RankingTennisComponent,
    RankingTennisUpdateComponent,
    RankingTennisDeleteDialogComponent,
    RankingTennisDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzRankingTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
