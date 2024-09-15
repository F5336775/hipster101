import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { Units } from 'app/entities/enumerations/units.model';
import { PreferenceService } from '../service/preference.service';
import { IPreference } from '../preference.model';
import { PreferenceFormGroup, PreferenceFormService } from './preference-form.service';

@Component({
  standalone: true,
  selector: 'jhi-preference-update',
  templateUrl: './preference-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PreferenceUpdateComponent implements OnInit {
  isSaving = false;
  preference: IPreference | null = null;
  unitsValues = Object.keys(Units);

  usersSharedCollection: IUser[] = [];

  protected preferenceService = inject(PreferenceService);
  protected preferenceFormService = inject(PreferenceFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PreferenceFormGroup = this.preferenceFormService.createPreferenceFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ preference }) => {
      this.preference = preference;
      if (preference) {
        this.updateForm(preference);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const preference = this.preferenceFormService.getPreference(this.editForm);
    if (preference.id !== null) {
      this.subscribeToSaveResponse(this.preferenceService.update(preference));
    } else {
      this.subscribeToSaveResponse(this.preferenceService.create(preference));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPreference>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(preference: IPreference): void {
    this.preference = preference;
    this.preferenceFormService.resetForm(this.editForm, preference);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, preference.onetoone);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.preference?.onetoone)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
