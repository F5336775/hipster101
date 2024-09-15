import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPreference, NewPreference } from '../preference.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPreference for edit and NewPreferenceFormGroupInput for create.
 */
type PreferenceFormGroupInput = IPreference | PartialWithRequiredKeyOf<NewPreference>;

type PreferenceFormDefaults = Pick<NewPreference, 'id'>;

type PreferenceFormGroupContent = {
  id: FormControl<IPreference['id'] | NewPreference['id']>;
  weeklyGoal: FormControl<IPreference['weeklyGoal']>;
  weightUnits: FormControl<IPreference['weightUnits']>;
  onetoone: FormControl<IPreference['onetoone']>;
};

export type PreferenceFormGroup = FormGroup<PreferenceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PreferenceFormService {
  createPreferenceFormGroup(preference: PreferenceFormGroupInput = { id: null }): PreferenceFormGroup {
    const preferenceRawValue = {
      ...this.getFormDefaults(),
      ...preference,
    };
    return new FormGroup<PreferenceFormGroupContent>({
      id: new FormControl(
        { value: preferenceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      weeklyGoal: new FormControl(preferenceRawValue.weeklyGoal, {
        validators: [Validators.min(10), Validators.max(21)],
      }),
      weightUnits: new FormControl(preferenceRawValue.weightUnits),
      onetoone: new FormControl(preferenceRawValue.onetoone),
    });
  }

  getPreference(form: PreferenceFormGroup): IPreference | NewPreference {
    return form.getRawValue() as IPreference | NewPreference;
  }

  resetForm(form: PreferenceFormGroup, preference: PreferenceFormGroupInput): void {
    const preferenceRawValue = { ...this.getFormDefaults(), ...preference };
    form.reset(
      {
        ...preferenceRawValue,
        id: { value: preferenceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PreferenceFormDefaults {
    return {
      id: null,
    };
  }
}
