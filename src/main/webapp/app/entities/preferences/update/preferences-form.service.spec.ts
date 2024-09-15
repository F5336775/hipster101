import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../preferences.test-samples';

import { PreferencesFormService } from './preferences-form.service';

describe('Preferences Form Service', () => {
  let service: PreferencesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PreferencesFormService);
  });

  describe('Service methods', () => {
    describe('createPreferencesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPreferencesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            weekelygoal: expect.any(Object),
            weightunits: expect.any(Object),
            manytoone: expect.any(Object),
          }),
        );
      });

      it('passing IPreferences should create a new form with FormGroup', () => {
        const formGroup = service.createPreferencesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            weekelygoal: expect.any(Object),
            weightunits: expect.any(Object),
            manytoone: expect.any(Object),
          }),
        );
      });
    });

    describe('getPreferences', () => {
      it('should return NewPreferences for default Preferences initial value', () => {
        const formGroup = service.createPreferencesFormGroup(sampleWithNewData);

        const preferences = service.getPreferences(formGroup) as any;

        expect(preferences).toMatchObject(sampleWithNewData);
      });

      it('should return NewPreferences for empty Preferences initial value', () => {
        const formGroup = service.createPreferencesFormGroup();

        const preferences = service.getPreferences(formGroup) as any;

        expect(preferences).toMatchObject({});
      });

      it('should return IPreferences', () => {
        const formGroup = service.createPreferencesFormGroup(sampleWithRequiredData);

        const preferences = service.getPreferences(formGroup) as any;

        expect(preferences).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPreferences should not enable id FormControl', () => {
        const formGroup = service.createPreferencesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPreferences should disable id FormControl', () => {
        const formGroup = service.createPreferencesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
