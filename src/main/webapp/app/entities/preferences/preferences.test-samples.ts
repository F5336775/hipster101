import { IPreferences, NewPreferences } from './preferences.model';

export const sampleWithRequiredData: IPreferences = {
  id: 12335,
};

export const sampleWithPartialData: IPreferences = {
  id: 30435,
};

export const sampleWithFullData: IPreferences = {
  id: 8877,
  weekelygoal: 'bleakly',
  weightunits: 'foolishly',
};

export const sampleWithNewData: NewPreferences = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
