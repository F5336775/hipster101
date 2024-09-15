import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPreference } from '../preference.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../preference.test-samples';

import { PreferenceService } from './preference.service';

const requireRestSample: IPreference = {
  ...sampleWithRequiredData,
};

describe('Preference Service', () => {
  let service: PreferenceService;
  let httpMock: HttpTestingController;
  let expectedResult: IPreference | IPreference[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PreferenceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Preference', () => {
      const preference = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(preference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Preference', () => {
      const preference = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(preference).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Preference', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Preference', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Preference', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Preference', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addPreferenceToCollectionIfMissing', () => {
      it('should add a Preference to an empty array', () => {
        const preference: IPreference = sampleWithRequiredData;
        expectedResult = service.addPreferenceToCollectionIfMissing([], preference);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(preference);
      });

      it('should not add a Preference to an array that contains it', () => {
        const preference: IPreference = sampleWithRequiredData;
        const preferenceCollection: IPreference[] = [
          {
            ...preference,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPreferenceToCollectionIfMissing(preferenceCollection, preference);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Preference to an array that doesn't contain it", () => {
        const preference: IPreference = sampleWithRequiredData;
        const preferenceCollection: IPreference[] = [sampleWithPartialData];
        expectedResult = service.addPreferenceToCollectionIfMissing(preferenceCollection, preference);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(preference);
      });

      it('should add only unique Preference to an array', () => {
        const preferenceArray: IPreference[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const preferenceCollection: IPreference[] = [sampleWithRequiredData];
        expectedResult = service.addPreferenceToCollectionIfMissing(preferenceCollection, ...preferenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const preference: IPreference = sampleWithRequiredData;
        const preference2: IPreference = sampleWithPartialData;
        expectedResult = service.addPreferenceToCollectionIfMissing([], preference, preference2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(preference);
        expect(expectedResult).toContain(preference2);
      });

      it('should accept null and undefined values', () => {
        const preference: IPreference = sampleWithRequiredData;
        expectedResult = service.addPreferenceToCollectionIfMissing([], null, preference, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(preference);
      });

      it('should return initial array if no Preference is added', () => {
        const preferenceCollection: IPreference[] = [sampleWithRequiredData];
        expectedResult = service.addPreferenceToCollectionIfMissing(preferenceCollection, undefined, null);
        expect(expectedResult).toEqual(preferenceCollection);
      });
    });

    describe('comparePreference', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePreference(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePreference(entity1, entity2);
        const compareResult2 = service.comparePreference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePreference(entity1, entity2);
        const compareResult2 = service.comparePreference(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePreference(entity1, entity2);
        const compareResult2 = service.comparePreference(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
