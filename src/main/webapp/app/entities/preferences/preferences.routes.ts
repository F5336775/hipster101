import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PreferencesResolve from './route/preferences-routing-resolve.service';

const preferencesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/preferences.component').then(m => m.PreferencesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/preferences-detail.component').then(m => m.PreferencesDetailComponent),
    resolve: {
      preferences: PreferencesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/preferences-update.component').then(m => m.PreferencesUpdateComponent),
    resolve: {
      preferences: PreferencesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/preferences-update.component').then(m => m.PreferencesUpdateComponent),
    resolve: {
      preferences: PreferencesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default preferencesRoute;
