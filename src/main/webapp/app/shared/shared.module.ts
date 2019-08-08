import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { TennisJazzSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [TennisJazzSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [TennisJazzSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TennisJazzSharedModule {
  static forRoot() {
    return {
      ngModule: TennisJazzSharedModule
    };
  }
}
