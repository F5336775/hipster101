import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'jhipster101App.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'points',
    data: { pageTitle: 'jhipster101App.points.home.title' },
    loadChildren: () => import('./points/points.routes'),
  },
  {
    path: 'weight',
    data: { pageTitle: 'jhipster101App.weight.home.title' },
    loadChildren: () => import('./weight/weight.routes'),
  },
  {
    path: 'bloodpressure',
    data: { pageTitle: 'jhipster101App.bloodpressure.home.title' },
    loadChildren: () => import('./bloodpressure/bloodpressure.routes'),
  },
  {
    path: 'preferences',
    data: { pageTitle: 'jhipster101App.preferences.home.title' },
    loadChildren: () => import('./preferences/preferences.routes'),
  },
  {
    path: 'preference',
    data: { pageTitle: 'jhipster101App.preference.home.title' },
    loadChildren: () => import('./preference/preference.routes'),
  },
  {
    path: 'bloodpresure',
    data: { pageTitle: 'jhipster101App.bloodpresure.home.title' },
    loadChildren: () => import('./bloodpresure/bloodpresure.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
