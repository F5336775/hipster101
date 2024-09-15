import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBloodpresure } from '../bloodpresure.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bloodpresure.test-samples';

import { BloodpresureService } from './bloodpresure.service';

const requireRestSample: IBloodpresure = {
  ...sampleWithRequiredData,
};

describe('Bloodpresure Service', () => {
  let service: BloodpresureService;
  let httpMock: HttpTestingController;
  let expectedResult: IBloodpresure | IBloodpresure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BloodpresureService);
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

    it('should create a Bloodpresure', () => {
      const bloodpresure = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bloodpresure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bloodpresure', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bloodpresure', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Bloodpresure', () => {
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

    describe('addBloodpresureToCollectionIfMissing', () => {
      it('should add a Bloodpresure to an empty array', () => {
        const bloodpresure: IBloodpresure = sampleWithRequiredData;
        expectedResult = service.addBloodpresureToCollectionIfMissing([], bloodpresure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bloodpresure);
      });

      it('should not add a Bloodpresure to an array that contains it', () => {
        const bloodpresure: IBloodpresure = sampleWithRequiredData;
        const bloodpresureCollection: IBloodpresure[] = [
          {
            ...bloodpresure,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBloodpresureToCollectionIfMissing(bloodpresureCollection, bloodpresure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bloodpresure to an array that doesn't contain it", () => {
        const bloodpresure: IBloodpresure = sampleWithRequiredData;
        const bloodpresureCollection: IBloodpresure[] = [sampleWithPartialData];
        expectedResult = service.addBloodpresureToCollectionIfMissing(bloodpresureCollection, bloodpresure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bloodpresure);
      });

      it('should add only unique Bloodpresure to an array', () => {
        const bloodpresureArray: IBloodpresure[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bloodpresureCollection: IBloodpresure[] = [sampleWithRequiredData];
        expectedResult = service.addBloodpresureToCollectionIfMissing(bloodpresureCollection, ...bloodpresureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bloodpresure: IBloodpresure = sampleWithRequiredData;
        const bloodpresure2: IBloodpresure = sampleWithPartialData;
        expectedResult = service.addBloodpresureToCollectionIfMissing([], bloodpresure, bloodpresure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bloodpresure);
        expect(expectedResult).toContain(bloodpresure2);
      });

      it('should accept null and undefined values', () => {
        const bloodpresure: IBloodpresure = sampleWithRequiredData;
        expectedResult = service.addBloodpresureToCollectionIfMissing([], null, bloodpresure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bloodpresure);
      });

      it('should return initial array if no Bloodpresure is added', () => {
        const bloodpresureCollection: IBloodpresure[] = [sampleWithRequiredData];
        expectedResult = service.addBloodpresureToCollectionIfMissing(bloodpresureCollection, undefined, null);
        expect(expectedResult).toEqual(bloodpresureCollection);
      });
    });

    describe('compareBloodpresure', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBloodpresure(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBloodpresure(entity1, entity2);
        const compareResult2 = service.compareBloodpresure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBloodpresure(entity1, entity2);
        const compareResult2 = service.compareBloodpresure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBloodpresure(entity1, entity2);
        const compareResult2 = service.compareBloodpresure(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
