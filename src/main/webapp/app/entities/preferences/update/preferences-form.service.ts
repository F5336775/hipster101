import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPreferences, NewPreferences } from '../preferences.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPreferences for edit and NewPreferencesFormGroupInput for create.
 */
type PreferencesFormGroupInput = IPreferences | PartialWithRequiredKeyOf<NewPreferences>;

type PreferencesFormDefaults = Pick<NewPreferences, 'id'>;

type PreferencesFormGroupContent = {
  id: FormControl<IPreferences['id'] | NewPreferences['id']>;
  weekelygoal: FormControl<IPreferences['weekelygoal']>;
  weightunits: FormControl<IPreferences['weightunits']>;
  manytoone: FormControl<IPreferences['manytoone']>;
};

export type PreferencesFormGroup = FormGroup<PreferencesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PreferencesFormService {
  createPreferencesFormGroup(preferences: PreferencesFormGroupInput = { id: null }): PreferencesFormGroup {
    const preferencesRawValue = {
      ...this.getFormDefaults(),
      ...preferences,
    };
    return new FormGroup<PreferencesFormGroupContent>({
      id: new FormControl(
        { value: preferencesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      weekelygoal: new FormControl(preferencesRawValue.weekelygoal),
      weightunits: new FormControl(preferencesRawValue.weightunits),
      manytoone: new FormControl(preferencesRawValue.manytoone),
    });
  }

  getPreferences(form: PreferencesFormGroup): IPreferences | NewPreferences {
    return form.getRawValue() as IPreferences | NewPreferences;
  }

  resetForm(form: PreferencesFormGroup, preferences: PreferencesFormGroupInput): void {
    const preferencesRawValue = { ...this.getFormDefaults(), ...preferences };
    form.reset(
      {
        ...preferencesRawValue,
        id: { value: preferencesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PreferencesFormDefaults {
    return {
      id: null,
    };
  }
}
