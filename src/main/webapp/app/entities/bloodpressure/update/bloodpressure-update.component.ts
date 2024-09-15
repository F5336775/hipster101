import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IBloodpressure } from '../bloodpressure.model';
import { BloodpressureService } from '../service/bloodpressure.service';
import { BloodpressureFormGroup, BloodpressureFormService } from './bloodpressure-form.service';

@Component({
  standalone: true,
  selector: 'jhi-bloodpressure-update',
  templateUrl: './bloodpressure-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BloodpressureUpdateComponent implements OnInit {
  isSaving = false;
  bloodpressure: IBloodpressure | null = null;

  usersSharedCollection: IUser[] = [];

  protected bloodpressureService = inject(BloodpressureService);
  protected bloodpressureFormService = inject(BloodpressureFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BloodpressureFormGroup = this.bloodpressureFormService.createBloodpressureFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bloodpressure }) => {
      this.bloodpressure = bloodpressure;
      if (bloodpressure) {
        this.updateForm(bloodpressure);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bloodpressure = this.bloodpressureFormService.getBloodpressure(this.editForm);
    if (bloodpressure.id !== null) {
      this.subscribeToSaveResponse(this.bloodpressureService.update(bloodpressure));
    } else {
      this.subscribeToSaveResponse(this.bloodpressureService.create(bloodpressure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBloodpressure>>): void {
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

  protected updateForm(bloodpressure: IBloodpressure): void {
    this.bloodpressure = bloodpressure;
    this.bloodpressureFormService.resetForm(this.editForm, bloodpressure);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, bloodpressure.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bloodpressure?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
