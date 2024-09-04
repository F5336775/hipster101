import dayjs from 'dayjs/esm';

import { IPoints, NewPoints } from './points.model';

export const sampleWithRequiredData: IPoints = {
  id: 31292,
  date: dayjs('2024-09-02'),
};

export const sampleWithPartialData: IPoints = {
  id: 27295,
  date: dayjs('2024-09-02'),
  exercise: 12540,
  meals: 2820,
};

export const sampleWithFullData: IPoints = {
  id: 8419,
  date: dayjs('2024-09-02'),
  exercise: 6665,
  meals: 22351,
  alcohol: 26861,
  notes: 'delirious city',
};

export const sampleWithNewData: NewPoints = {
  date: dayjs('2024-09-03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
