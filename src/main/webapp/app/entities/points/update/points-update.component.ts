import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPoints } from '../points.model';
import { PointsService } from '../service/points.service';
import { PointsFormGroup, PointsFormService } from './points-form.service';

@Component({
  standalone: true,
  selector: 'jhi-points-update',
  templateUrl: './points-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PointsUpdateComponent implements OnInit {
  isSaving = false;
  points: IPoints | null = null;

  usersSharedCollection: IUser[] = [];

  protected pointsService = inject(PointsService);
  protected pointsFormService = inject(PointsFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PointsFormGroup = this.pointsFormService.createPointsFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ points }) => {
      this.points = points;
      if (points) {
        this.updateForm(points);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const points = this.pointsFormService.getPoints(this.editForm);
    if (points.id !== null) {
      this.subscribeToSaveResponse(this.pointsService.update(points));
    } else {
      this.subscribeToSaveResponse(this.pointsService.create(points));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPoints>>): void {
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

  protected updateForm(points: IPoints): void {
    this.points = points;
    this.pointsFormService.resetForm(this.editForm, points);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, points.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.points?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
