import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IWeight } from '../weight.model';
import { WeightService } from '../service/weight.service';
import { WeightFormGroup, WeightFormService } from './weight-form.service';

@Component({
  standalone: true,
  selector: 'jhi-weight-update',
  templateUrl: './weight-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WeightUpdateComponent implements OnInit {
  isSaving = false;
  weight: IWeight | null = null;

  usersSharedCollection: IUser[] = [];

  protected weightService = inject(WeightService);
  protected weightFormService = inject(WeightFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WeightFormGroup = this.weightFormService.createWeightFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ weight }) => {
      this.weight = weight;
      if (weight) {
        this.updateForm(weight);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const weight = this.weightFormService.getWeight(this.editForm);
    if (weight.id !== null) {
      this.subscribeToSaveResponse(this.weightService.update(weight));
    } else {
      this.subscribeToSaveResponse(this.weightService.create(weight));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWeight>>): void {
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

  protected updateForm(weight: IWeight): void {
    this.weight = weight;
    this.weightFormService.resetForm(this.editForm, weight);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, weight.manytoone);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.weight?.manytoone)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
