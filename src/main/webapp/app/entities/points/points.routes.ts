import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PointsResolve from './route/points-routing-resolve.service';

const pointsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/points.component').then(m => m.PointsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/points-detail.component').then(m => m.PointsDetailComponent),
    resolve: {
      points: PointsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/points-update.component').then(m => m.PointsUpdateComponent),
    resolve: {
      points: PointsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/points-update.component').then(m => m.PointsUpdateComponent),
    resolve: {
      points: PointsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pointsRoute;
