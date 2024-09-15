import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BloodpresureDetailComponent } from './bloodpresure-detail.component';

describe('Bloodpresure Management Detail Component', () => {
  let comp: BloodpresureDetailComponent;
  let fixture: ComponentFixture<BloodpresureDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BloodpresureDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bloodpresure-detail.component').then(m => m.BloodpresureDetailComponent),
              resolve: { bloodpresure: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BloodpresureDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BloodpresureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bloodpresure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BloodpresureDetailComponent);

      // THEN
      expect(instance.bloodpresure()).toEqual(expect.objectContaining({ id: 123 }));
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
