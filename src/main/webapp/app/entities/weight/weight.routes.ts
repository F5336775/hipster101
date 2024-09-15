import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WeightResolve from './route/weight-routing-resolve.service';

const weightRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/weight.component').then(m => m.WeightComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/weight-detail.component').then(m => m.WeightDetailComponent),
    resolve: {
      weight: WeightResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/weight-update.component').then(m => m.WeightUpdateComponent),
    resolve: {
      weight: WeightResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/weight-update.component').then(m => m.WeightUpdateComponent),
    resolve: {
      weight: WeightResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default weightRoute;
