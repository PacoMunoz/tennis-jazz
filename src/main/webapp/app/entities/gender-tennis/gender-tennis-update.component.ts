import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IGenderTennis, GenderTennis } from 'app/shared/model/gender-tennis.model';
import { GenderTennisService } from './gender-tennis.service';

@Component({
  selector: 'jhi-gender-tennis-update',
  templateUrl: './gender-tennis-update.component.html'
})
export class GenderTennisUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected genderService: GenderTennisService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ gender }) => {
      this.updateForm(gender);
    });
  }

  updateForm(gender: IGenderTennis) {
    this.editForm.patchValue({
      id: gender.id,
      name: gender.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const gender = this.createFromForm();
    if (gender.id !== undefined) {
      this.subscribeToSaveResponse(this.genderService.update(gender));
    } else {
      this.subscribeToSaveResponse(this.genderService.create(gender));
    }
  }

  private createFromForm(): IGenderTennis {
    return {
      ...new GenderTennis(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGenderTennis>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
