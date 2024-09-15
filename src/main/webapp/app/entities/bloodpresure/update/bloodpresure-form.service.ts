import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBloodpresure, NewBloodpresure } from '../bloodpresure.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBloodpresure for edit and NewBloodpresureFormGroupInput for create.
 */
type BloodpresureFormGroupInput = IBloodpresure | PartialWithRequiredKeyOf<NewBloodpresure>;

type BloodpresureFormDefaults = Pick<NewBloodpresure, 'id'>;

type BloodpresureFormGroupContent = {
  id: FormControl<IBloodpresure['id'] | NewBloodpresure['id']>;
};

export type BloodpresureFormGroup = FormGroup<BloodpresureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BloodpresureFormService {
  createBloodpresureFormGroup(bloodpresure: BloodpresureFormGroupInput = { id: null }): BloodpresureFormGroup {
    const bloodpresureRawValue = {
      ...this.getFormDefaults(),
      ...bloodpresure,
    };
    return new FormGroup<BloodpresureFormGroupContent>({
      id: new FormControl(
        { value: bloodpresureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
    });
  }

  getBloodpresure(form: BloodpresureFormGroup): NewBloodpresure {
    return form.getRawValue() as NewBloodpresure;
  }

  resetForm(form: BloodpresureFormGroup, bloodpresure: BloodpresureFormGroupInput): void {
    const bloodpresureRawValue = { ...this.getFormDefaults(), ...bloodpresure };
    form.reset(
      {
        ...bloodpresureRawValue,
        id: { value: bloodpresureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BloodpresureFormDefaults {
    return {
      id: null,
    };
  }
}
