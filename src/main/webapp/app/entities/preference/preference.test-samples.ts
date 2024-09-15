import { IPreference, NewPreference } from './preference.model';

export const sampleWithRequiredData: IPreference = {
  id: 24348,
};

export const sampleWithPartialData: IPreference = {
  id: 25547,
  weightUnits: 'KG',
};

export const sampleWithFullData: IPreference = {
  id: 17692,
  weeklyGoal: 11,
  weightUnits: 'KG',
};

export const sampleWithNewData: NewPreference = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
