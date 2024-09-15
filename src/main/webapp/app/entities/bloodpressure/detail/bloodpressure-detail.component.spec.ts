import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BloodpressureDetailComponent } from './bloodpressure-detail.component';

describe('Bloodpressure Management Detail Component', () => {
  let comp: BloodpressureDetailComponent;
  let fixture: ComponentFixture<BloodpressureDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BloodpressureDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bloodpressure-detail.component').then(m => m.BloodpressureDetailComponent),
              resolve: { bloodpressure: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BloodpressureDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BloodpressureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bloodpressure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BloodpressureDetailComponent);

      // THEN
      expect(instance.bloodpressure()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
