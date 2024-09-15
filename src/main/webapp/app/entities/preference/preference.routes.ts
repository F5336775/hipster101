import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PreferenceResolve from './route/preference-routing-resolve.service';

const preferenceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/preference.component').then(m => m.PreferenceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/preference-detail.component').then(m => m.PreferenceDetailComponent),
    resolve: {
      preference: PreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/preference-update.component').then(m => m.PreferenceUpdateComponent),
    resolve: {
      preference: PreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/preference-update.component').then(m => m.PreferenceUpdateComponent),
    resolve: {
      preference: PreferenceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default preferenceRoute;
