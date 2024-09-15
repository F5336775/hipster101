import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBloodpresure } from '../bloodpresure.model';
import { BloodpresureService } from '../service/bloodpresure.service';

const bloodpresureResolve = (route: ActivatedRouteSnapshot): Observable<null | IBloodpresure> => {
  const id = route.params.id;
  if (id) {
    return inject(BloodpresureService)
      .find(id)
      .pipe(
        mergeMap((bloodpresure: HttpResponse<IBloodpresure>) => {
          if (bloodpresure.body) {
            return of(bloodpresure.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bloodpresureResolve;
