import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TennisJazzSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

import { JhMaterialModule } from 'app/shared/jh-material.module';
@NgModule({
  imports: [JhMaterialModule, TennisJazzSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [JhMaterialModule, TennisJazzSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzSharedModule {
  static forRoot() {
    return {
      ngModule: TennisJazzSharedModule
    };
  }
}
