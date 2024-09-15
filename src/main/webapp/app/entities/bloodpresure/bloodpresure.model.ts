export interface IBloodpresure {
  id: number;
}

export type NewBloodpresure = Omit<IBloodpresure, 'id'> & { id: null };
