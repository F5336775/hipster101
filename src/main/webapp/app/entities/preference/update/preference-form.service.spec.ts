import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../preference.test-samples';

import { PreferenceFormService } from './preference-form.service';

describe('Preference Form Service', () => {
  let service: PreferenceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PreferenceFormService);
  });

  describe('Service methods', () => {
    describe('createPreferenceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPreferenceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            weeklyGoal: expect.any(Object),
            weightUnits: expect.any(Object),
            onetoone: expect.any(Object),
          }),
        );
      });

      it('passing IPreference should create a new form with FormGroup', () => {
        const formGroup = service.createPreferenceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            weeklyGoal: expect.any(Object),
            weightUnits: expect.any(Object),
            onetoone: expect.any(Object),
          }),
        );
      });
    });

    describe('getPreference', () => {
      it('should return NewPreference for default Preference initial value', () => {
        const formGroup = service.createPreferenceFormGroup(sampleWithNewData);

        const preference = service.getPreference(formGroup) as any;

        expect(preference).toMatchObject(sampleWithNewData);
      });

      it('should return NewPreference for empty Preference initial value', () => {
        const formGroup = service.createPreferenceFormGroup();

        const preference = service.getPreference(formGroup) as any;

        expect(preference).toMatchObject({});
      });

      it('should return IPreference', () => {
        const formGroup = service.createPreferenceFormGroup(sampleWithRequiredData);

        const preference = service.getPreference(formGroup) as any;

        expect(preference).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPreference should not enable id FormControl', () => {
        const formGroup = service.createPreferenceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPreference should disable id FormControl', () => {
        const formGroup = service.createPreferenceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
