import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBloodpresure } from '../bloodpresure.model';
import { BloodpresureService } from '../service/bloodpresure.service';
import { BloodpresureFormGroup, BloodpresureFormService } from './bloodpresure-form.service';

@Component({
  standalone: true,
  selector: 'jhi-bloodpresure-update',
  templateUrl: './bloodpresure-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BloodpresureUpdateComponent implements OnInit {
  isSaving = false;
  bloodpresure: IBloodpresure | null = null;

  protected bloodpresureService = inject(BloodpresureService);
  protected bloodpresureFormService = inject(BloodpresureFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BloodpresureFormGroup = this.bloodpresureFormService.createBloodpresureFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bloodpresure }) => {
      this.bloodpresure = bloodpresure;
      if (bloodpresure) {
        this.updateForm(bloodpresure);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bloodpresure = this.bloodpresureFormService.getBloodpresure(this.editForm);
    this.subscribeToSaveResponse(this.bloodpresureService.create(bloodpresure));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBloodpresure>>): void {
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

  protected updateForm(bloodpresure: IBloodpresure): void {
    this.bloodpresure = bloodpresure;
    this.bloodpresureFormService.resetForm(this.editForm, bloodpresure);
  }
}
