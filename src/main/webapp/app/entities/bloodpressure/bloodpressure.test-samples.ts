import dayjs from 'dayjs/esm';

import { IBloodpressure, NewBloodpressure } from './bloodpressure.model';

export const sampleWithRequiredData: IBloodpressure = {
  id: 22792,
  datetime: dayjs('2024-09-03T16:28'),
};

export const sampleWithPartialData: IBloodpressure = {
  id: 30086,
  datetime: dayjs('2024-09-03T10:26'),
  systolic: 19788,
};

export const sampleWithFullData: IBloodpressure = {
  id: 11454,
  datetime: dayjs('2024-09-04T03:53'),
  systolic: 23215,
  diastolic: 25934,
};

export const sampleWithNewData: NewBloodpressure = {
  datetime: dayjs('2024-09-04T04:13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
