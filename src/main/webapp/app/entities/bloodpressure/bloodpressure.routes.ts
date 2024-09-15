import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BloodpressureResolve from './route/bloodpressure-routing-resolve.service';

const bloodpressureRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bloodpressure.component').then(m => m.BloodpressureComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bloodpressure-detail.component').then(m => m.BloodpressureDetailComponent),
    resolve: {
      bloodpressure: BloodpressureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bloodpressure-update.component').then(m => m.BloodpressureUpdateComponent),
    resolve: {
      bloodpressure: BloodpressureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bloodpressure-update.component').then(m => m.BloodpressureUpdateComponent),
    resolve: {
      bloodpressure: BloodpressureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bloodpressureRoute;
