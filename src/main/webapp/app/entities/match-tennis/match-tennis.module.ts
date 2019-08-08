import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared';
import {
  MatchTennisComponent,
  MatchTennisDetailComponent,
  MatchTennisUpdateComponent,
  MatchTennisDeletePopupComponent,
  MatchTennisDeleteDialogComponent,
  matchRoute,
  matchPopupRoute
} from './';

const ENTITY_STATES = [...matchRoute, ...matchPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MatchTennisComponent,
    MatchTennisDetailComponent,
    MatchTennisUpdateComponent,
    MatchTennisDeleteDialogComponent,
    MatchTennisDeletePopupComponent
  ],
  entryComponents: [MatchTennisComponent, MatchTennisUpdateComponent, MatchTennisDeleteDialogComponent, MatchTennisDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzMatchTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
