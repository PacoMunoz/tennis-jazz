import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { TennisJazzSharedModule } from 'app/shared';
import {
  GenderTennisComponent,
  GenderTennisDetailComponent,
  GenderTennisUpdateComponent,
  GenderTennisDeletePopupComponent,
  GenderTennisDeleteDialogComponent,
  genderRoute,
  genderPopupRoute
} from './';

const ENTITY_STATES = [...genderRoute, ...genderPopupRoute];

@NgModule({
  imports: [TennisJazzSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    GenderTennisComponent,
    GenderTennisDetailComponent,
    GenderTennisUpdateComponent,
    GenderTennisDeleteDialogComponent,
    GenderTennisDeletePopupComponent
  ],
  entryComponents: [
    GenderTennisComponent,
    GenderTennisUpdateComponent,
    GenderTennisDeleteDialogComponent,
    GenderTennisDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzGenderTennisModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
