import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPreference } from '../preference.model';
import { PreferenceService } from '../service/preference.service';

const preferenceResolve = (route: ActivatedRouteSnapshot): Observable<null | IPreference> => {
  const id = route.params.id;
  if (id) {
    return inject(PreferenceService)
      .find(id)
      .pipe(
        mergeMap((preference: HttpResponse<IPreference>) => {
          if (preference.body) {
            return of(preference.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default preferenceResolve;
