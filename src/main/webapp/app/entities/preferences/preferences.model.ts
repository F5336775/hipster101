import { IUser } from 'app/entities/user/user.model';

export interface IPreferences {
  id: number;
  weekelygoal?: string | null;
  weightunits?: string | null;
  manytoone?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPreferences = Omit<IPreferences, 'id'> & { id: null };
