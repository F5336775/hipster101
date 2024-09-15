import { IBloodpresure, NewBloodpresure } from './bloodpresure.model';

export const sampleWithRequiredData: IBloodpresure = {
  id: 30546,
};

export const sampleWithPartialData: IBloodpresure = {
  id: 9174,
};

export const sampleWithFullData: IBloodpresure = {
  id: 5647,
};

export const sampleWithNewData: NewBloodpresure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
