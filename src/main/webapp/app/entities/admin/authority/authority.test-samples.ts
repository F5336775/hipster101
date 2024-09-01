import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '74230c3a-4230-4e0c-a924-b45a560c7af9',
};

export const sampleWithPartialData: IAuthority = {
  name: 'e5c01226-d488-4f88-b26b-a60f81bf899c',
};

export const sampleWithFullData: IAuthority = {
  name: '54e94324-22c1-4774-9567-bf3c3049880c',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
