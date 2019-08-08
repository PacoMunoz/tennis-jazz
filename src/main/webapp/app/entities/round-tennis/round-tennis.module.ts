import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared';
import {
  RoundTennisComponent,
  RoundTennisDetailComponent,
  RoundTennisUpdateComponent,
  RoundTennisDeletePopupComponent,
  RoundTennisDeleteDialogComponent,
  roundRoute,
  roundPopupRoute
} from './';

const ENTITY_STATES = [...roundRoute, ...roundPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RoundTennisComponent,
    RoundTennisDetailComponent,
    RoundTennisUpdateComponent,
    RoundTennisDeleteDialogComponent,
    RoundTennisDeletePopupComponent
  ],
  entryComponents: [RoundTennisComponent, RoundTennisUpdateComponent, RoundTennisDeleteDialogComponent, RoundTennisDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzRoundTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
