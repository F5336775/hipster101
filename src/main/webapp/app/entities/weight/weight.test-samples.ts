import dayjs from 'dayjs/esm';

import { IWeight, NewWeight } from './weight.model';

export const sampleWithRequiredData: IWeight = {
  id: 29898,
  datetime: dayjs('2024-09-03T19:30'),
};

export const sampleWithPartialData: IWeight = {
  id: 57,
  datetime: dayjs('2024-09-03T13:37'),
};

export const sampleWithFullData: IWeight = {
  id: 31434,
  datetime: dayjs('2024-09-03T09:39'),
  weight: 27385,
};

export const sampleWithNewData: NewWeight = {
  datetime: dayjs('2024-09-03T13:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
