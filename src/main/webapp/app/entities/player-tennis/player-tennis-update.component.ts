import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IPlayerTennis, PlayerTennis } from 'app/shared/model/player-tennis.model';
import { PlayerTennisService } from './player-tennis.service';
import { IGenderTennis } from 'app/shared/model/gender-tennis.model';
import { GenderTennisService } from 'app/entities/gender-tennis';
import { ITournamentGroupTennis } from 'app/shared/model/tournament-group-tennis.model';
import { TournamentGroupTennisService } from 'app/entities/tournament-group-tennis';

@Component({
  selector: 'jhi-player-tennis-update',
  templateUrl: './player-tennis-update.component.html'
})
export class PlayerTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  genders: IGenderTennis[];

  tournamentgroups: ITournamentGroupTennis[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    surname: [],
    email: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    other: [],
    avatar: [null, []],
    avatarContentType: [],
    gender: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected jhiAlertService: JhiAlertService,
    protected playerService: PlayerTennisService,
    protected genderService: GenderTennisService,
    protected tournamentGroupService: TournamentGroupTennisService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);
    });
    this.genderService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGenderTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGenderTennis[]>) => response.body)
      )
      .subscribe((res: IGenderTennis[]) => (this.genders = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tournamentGroupService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournamentGroupTennis[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournamentGroupTennis[]>) => response.body)
      )
      .subscribe((res: ITournamentGroupTennis[]) => (this.tournamentgroups = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(player: IPlayerTennis) {
    this.editForm.patchValue({
      id: player.id,
      name: player.name,
      surname: player.surname,
      email: player.email,
      phone: player.phone,
      other: player.other,
      avatar: player.avatar,
      avatarContentType: player.avatarContentType,
      gender: player.gender
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file = event.target.files[0];
        if (isImage && !/^image\//.test(file.type)) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.editForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      () => console.log('blob added'), // sucess
      this.onError
    );
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  private createFromForm(): IPlayerTennis {
    return {
      ...new PlayerTennis(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      surname: this.editForm.get(['surname']).value,
      email: this.editForm.get(['email']).value,
      phone: this.editForm.get(['phone']).value,
      other: this.editForm.get(['other']).value,
      avatarContentType: this.editForm.get(['avatarContentType']).value,
      avatar: this.editForm.get(['avatar']).value,
      gender: this.editForm.get(['gender']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayerTennis>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackGenderById(index: number, item: IGenderTennis) {
    return item.id;
  }

  trackTournamentGroupById(index: number, item: ITournamentGroupTennis) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
