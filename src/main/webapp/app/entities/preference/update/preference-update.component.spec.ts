import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { PreferenceService } from '../service/preference.service';
import { IPreference } from '../preference.model';
import { PreferenceFormService } from './preference-form.service';

import { PreferenceUpdateComponent } from './preference-update.component';

describe('Preference Management Update Component', () => {
  let comp: PreferenceUpdateComponent;
  let fixture: ComponentFixture<PreferenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let preferenceFormService: PreferenceFormService;
  let preferenceService: PreferenceService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PreferenceUpdateComponent],
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
      .overrideTemplate(PreferenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PreferenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    preferenceFormService = TestBed.inject(PreferenceFormService);
    preferenceService = TestBed.inject(PreferenceService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const preference: IPreference = { id: 456 };
      const onetoone: IUser = { id: 26166 };
      preference.onetoone = onetoone;

      const userCollection: IUser[] = [{ id: 20521 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [onetoone];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ preference });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const preference: IPreference = { id: 456 };
      const onetoone: IUser = { id: 4855 };
      preference.onetoone = onetoone;

      activatedRoute.data = of({ preference });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(onetoone);
      expect(comp.preference).toEqual(preference);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPreference>>();
      const preference = { id: 123 };
      jest.spyOn(preferenceFormService, 'getPreference').mockReturnValue(preference);
      jest.spyOn(preferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ preference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: preference }));
      saveSubject.complete();

      // THEN
      expect(preferenceFormService.getPreference).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(preferenceService.update).toHaveBeenCalledWith(expect.objectContaining(preference));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPreference>>();
      const preference = { id: 123 };
      jest.spyOn(preferenceFormService, 'getPreference').mockReturnValue({ id: null });
      jest.spyOn(preferenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ preference: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: preference }));
      saveSubject.complete();

      // THEN
      expect(preferenceFormService.getPreference).toHaveBeenCalled();
      expect(preferenceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPreference>>();
      const preference = { id: 123 };
      jest.spyOn(preferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ preference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(preferenceService.update).toHaveBeenCalled();
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
