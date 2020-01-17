import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { JhiAlertService, JhiLanguageService } from 'ng-jhipster';

import { AccountService, IUser, JhiLanguageHelper, UserService } from 'app/core';
import { Account } from 'app/core/user/account.model';
import { switchMap } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IPlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
  error: string;
  success: string;
  languages: any[];
  currentAccount: any;
  player: IPlayerTennis;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [false],
    authorities: [[]],
    langKey: ['en'],
    login: [],
    imageUrl: []
  });

  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private userService: UserService,
    private playerService: PlayerTennisService,
    private jhiAlertService: JhiAlertService
  ) {}

  ngOnInit() {
    this.accountService
      .identity()
      .then(account => {
        this.updateForm(account);
        this.currentAccount = account;
      })
      .finally(() => {
        if (this.currentAccount != null) {
          this.getPlayer(this.currentAccount.login);
        }
      });
    this.languageHelper.getAll().then(languages => {
      this.languages = languages;
    });
  }

  getPlayer(login: any) {
    console.log('**************************************************Entrando a obtener jugador');
    this.userService
      .find(login)
      .pipe(
        switchMap((res: HttpResponse<IUser>) =>
          this.playerService.query({
            'userId.equals': res.body.id
          })
        )
      )
      .subscribe(
        (res: HttpResponse<IPlayerTennis[]>) => (this.player = res.body != null && res.body.length === 1 ? res.body[0] : null),
        (error: HttpErrorResponse) => this.onError(error.message)
      );
  }

  save() {
    const settingsAccount = this.accountFromForm();
    this.accountService.save(settingsAccount).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
        this.accountService.identity(true).then(account => {
          this.updateForm(account);
        });
        this.languageService.getCurrent().then(current => {
          if (settingsAccount.langKey !== current) {
            this.languageService.changeLanguage(settingsAccount.langKey);
          }
        });
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  private accountFromForm(): any {
    const account = {};
    return {
      ...account,
      firstName: this.settingsForm.get('firstName').value,
      lastName: this.settingsForm.get('lastName').value,
      email: this.settingsForm.get('email').value,
      activated: this.settingsForm.get('activated').value,
      authorities: this.settingsForm.get('authorities').value,
      langKey: this.settingsForm.get('langKey').value,
      login: this.settingsForm.get('login').value,
      imageUrl: this.settingsForm.get('imageUrl').value
    };
  }

  updateForm(account: any): void {
    this.settingsForm.patchValue({
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email,
      activated: account.activated,
      authorities: account.authorities,
      langKey: account.langKey,
      login: account.login,
      imageUrl: account.imageUrl
    });
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
