import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bloodpresure.test-samples';

import { BloodpresureFormService } from './bloodpresure-form.service';

describe('Bloodpresure Form Service', () => {
  let service: BloodpresureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BloodpresureFormService);
  });

  describe('Service methods', () => {
    describe('createBloodpresureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBloodpresureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });

      it('passing IBloodpresure should create a new form with FormGroup', () => {
        const formGroup = service.createBloodpresureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          }),
        );
      });
    });

    describe('getBloodpresure', () => {
      it('should return NewBloodpresure for default Bloodpresure initial value', () => {
        const formGroup = service.createBloodpresureFormGroup(sampleWithNewData);

        const bloodpresure = service.getBloodpresure(formGroup) as any;

        expect(bloodpresure).toMatchObject(sampleWithNewData);
      });

      it('should return NewBloodpresure for empty Bloodpresure initial value', () => {
        const formGroup = service.createBloodpresureFormGroup();

        const bloodpresure = service.getBloodpresure(formGroup) as any;

        expect(bloodpresure).toMatchObject({});
      });

      it('should return IBloodpresure', () => {
        const formGroup = service.createBloodpresureFormGroup(sampleWithRequiredData);

        const bloodpresure = service.getBloodpresure(formGroup) as any;

        expect(bloodpresure).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBloodpresure should not enable id FormControl', () => {
        const formGroup = service.createBloodpresureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBloodpresure should disable id FormControl', () => {
        const formGroup = service.createBloodpresureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
