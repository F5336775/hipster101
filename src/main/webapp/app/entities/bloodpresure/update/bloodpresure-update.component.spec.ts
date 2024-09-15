import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BloodpresureService } from '../service/bloodpresure.service';
import { IBloodpresure } from '../bloodpresure.model';
import { BloodpresureFormService } from './bloodpresure-form.service';

import { BloodpresureUpdateComponent } from './bloodpresure-update.component';

describe('Bloodpresure Management Update Component', () => {
  let comp: BloodpresureUpdateComponent;
  let fixture: ComponentFixture<BloodpresureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bloodpresureFormService: BloodpresureFormService;
  let bloodpresureService: BloodpresureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BloodpresureUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BloodpresureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BloodpresureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bloodpresureFormService = TestBed.inject(BloodpresureFormService);
    bloodpresureService = TestBed.inject(BloodpresureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bloodpresure: IBloodpresure = { id: 456 };

      activatedRoute.data = of({ bloodpresure });
      comp.ngOnInit();

      expect(comp.bloodpresure).toEqual(bloodpresure);
    });
  });

  describe('save', () => {
    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBloodpresure>>();
      const bloodpresure = { id: 123 };
      jest.spyOn(bloodpresureFormService, 'getBloodpresure').mockReturnValue({ id: null });
      jest.spyOn(bloodpresureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bloodpresure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bloodpresure }));
      saveSubject.complete();

      // THEN
      expect(bloodpresureFormService.getBloodpresure).toHaveBeenCalled();
      expect(bloodpresureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });
  });
});
