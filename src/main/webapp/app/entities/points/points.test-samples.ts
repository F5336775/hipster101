import dayjs from 'dayjs/esm';

import { IPoints, NewPoints } from './points.model';

export const sampleWithRequiredData: IPoints = {
  id: 2227,
  date: dayjs('2024-09-02'),
};

export const sampleWithPartialData: IPoints = {
  id: 15982,
  date: dayjs('2024-09-02'),
  exercise: 781,
  alcohol: 32136,
  notes: 'carefully strange hungrily',
};

export const sampleWithFullData: IPoints = {
  id: 27821,
  date: dayjs('2024-09-03'),
  exercise: 15670,
  meals: 16501,
  alcohol: 1652,
  notes: 'atop',
};

export const sampleWithNewData: NewPoints = {
  date: dayjs('2024-09-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
