import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BloodpresureResolve from './route/bloodpresure-routing-resolve.service';

const bloodpresureRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bloodpresure.component').then(m => m.BloodpresureComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bloodpresure-detail.component').then(m => m.BloodpresureDetailComponent),
    resolve: {
      bloodpresure: BloodpresureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bloodpresure-update.component').then(m => m.BloodpresureUpdateComponent),
    resolve: {
      bloodpresure: BloodpresureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bloodpresureRoute;
