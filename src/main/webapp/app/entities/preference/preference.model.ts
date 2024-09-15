import { IUser } from 'app/entities/user/user.model';
import { Units } from 'app/entities/enumerations/units.model';

export interface IPreference {
  id: number;
  weeklyGoal?: number | null;
  weightUnits?: keyof typeof Units | null;
  onetoone?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPreference = Omit<IPreference, 'id'> & { id: null };
