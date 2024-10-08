import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { BloodpressureService } from '../service/bloodpressure.service';
import { IBloodpressure } from '../bloodpressure.model';
import { BloodpressureFormService } from './bloodpressure-form.service';

import { BloodpressureUpdateComponent } from './bloodpressure-update.component';

describe('Bloodpressure Management Update Component', () => {
  let comp: BloodpressureUpdateComponent;
  let fixture: ComponentFixture<BloodpressureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bloodpressureFormService: BloodpressureFormService;
  let bloodpressureService: BloodpressureService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BloodpressureUpdateComponent],
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
      .overrideTemplate(BloodpressureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BloodpressureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bloodpressureFormService = TestBed.inject(BloodpressureFormService);
    bloodpressureService = TestBed.inject(BloodpressureService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const bloodpressure: IBloodpressure = { id: 456 };
      const user: IUser = { id: 21281 };
      bloodpressure.user = user;

      const userCollection: IUser[] = [{ id: 7310 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bloodpressure });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bloodpressure: IBloodpressure = { id: 456 };
      const user: IUser = { id: 424 };
      bloodpressure.user = user;

      activatedRoute.data = of({ bloodpressure });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.bloodpressure).toEqual(bloodpressure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBloodpressure>>();
      const bloodpressure = { id: 123 };
      jest.spyOn(bloodpressureFormService, 'getBloodpressure').mockReturnValue(bloodpressure);
      jest.spyOn(bloodpressureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bloodpressure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bloodpressure }));
      saveSubject.complete();

      // THEN
      expect(bloodpressureFormService.getBloodpressure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bloodpressureService.update).toHaveBeenCalledWith(expect.objectContaining(bloodpressure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBloodpressure>>();
      const bloodpressure = { id: 123 };
      jest.spyOn(bloodpressureFormService, 'getBloodpressure').mockReturnValue({ id: null });
      jest.spyOn(bloodpressureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bloodpressure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bloodpressure }));
      saveSubject.complete();

      // THEN
      expect(bloodpressureFormService.getBloodpressure).toHaveBeenCalled();
      expect(bloodpressureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBloodpressure>>();
      const bloodpressure = { id: 123 };
      jest.spyOn(bloodpressureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bloodpressure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bloodpressureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
