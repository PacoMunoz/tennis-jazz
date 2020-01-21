import { Component, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { JhiAlertService, JhiDataUtils, JhiLanguageService } from 'ng-jhipster';
import { AccountService, IUser, JhiLanguageHelper, UserService } from 'app/core';
import { filter, map, switchMap } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IPlayerTennis, PlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from 'app/entities/player-tennis';
import { IGenderTennis } from 'app/shared/model/gender-tennis.model';
import { GenderTennisService } from 'app/entities/gender-tennis';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html'
})
export class SettingsComponent implements OnInit {
  error: string;
  success: string;
  languages: any[];
  genders: IGenderTennis[];
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
    imageUrl: [],
    phone: [null, [Validators.required]],
    other: [],
    avatar: [null, []],
    avatarContentType: [],
    gender: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    private accountService: AccountService,
    private fb: FormBuilder,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private userService: UserService,
    protected elementRef: ElementRef,
    private playerService: PlayerTennisService,
    private genderService: GenderTennisService,
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
    this.genderService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGenderTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGenderTennis[]>) => response.body)
      )
      .subscribe((res: IGenderTennis[]) => (this.genders = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  save() {
    window.scrollTo(0, 0);
    this.savePlayer();
    this.saveAccount();
  }

  saveAccount() {
    const settingsAccount = this.accountFromForm();
    this.accountService.save(settingsAccount).subscribe(
      () => {
        this.error = null;
        this.success = 'OK';
        this.accountService
          .identity(true)
          .then(account => {
            this.updateForm(account);
          })
          .finally(() => {
            if (this.currentAccount != null) {
              this.getPlayer(this.currentAccount.login);
            }
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

  savePlayer() {
    const player = this.createFromForm();
    this.playerService.update(player).subscribe(
      () => {
        this.error = null;
        this.success = 'Ok';
      },
      () => {
        this.success = null;
        this.error = 'ERROR';
      }
    );
  }

  getPlayer(login: any) {
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
        (res: HttpResponse<IPlayerTennis[]>) => {
          this.player = res.body != null && res.body.length === 1 ? res.body[0] : null;
          this.updatePlayerForm(this.player);
        },
        (error: HttpErrorResponse) => this.onError(error.message)
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

  private createFromForm(): IPlayerTennis {
    return {
      ...new PlayerTennis(),
      id: this.player.id,
      name: this.settingsForm.get(['firstName']).value,
      surname: this.settingsForm.get(['lastName']).value,
      email: this.settingsForm.get(['email']).value,
      phone: this.settingsForm.get(['phone']).value,
      other: this.settingsForm.get(['other']).value,
      avatarContentType: this.settingsForm.get(['avatarContentType']).value,
      avatar: this.settingsForm.get(['avatar']).value,
      gender: this.settingsForm.get(['gender']).value,
      user: this.player.user
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

  updatePlayerForm(player: IPlayerTennis) {
    this.settingsForm.patchValue({
      phone: player.phone,
      other: player.other,
      avatar: player.avatar,
      avatarContentType: player.avatarContentType,
      gender: player.gender,
      user: player.user
    });
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.settingsForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.settingsForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  trackGenderById(index: number, item: IGenderTennis) {
    return item.id;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
