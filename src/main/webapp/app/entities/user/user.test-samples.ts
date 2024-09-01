import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 217,
  login: 'h_K',
};

export const sampleWithPartialData: IUser = {
  id: 11684,
  login: 'dh',
};

export const sampleWithFullData: IUser = {
  id: 13902,
  login: 'eOuUh',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
