import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBloodpressure } from '../bloodpressure.model';
import { BloodpressureService } from '../service/bloodpressure.service';

const bloodpressureResolve = (route: ActivatedRouteSnapshot): Observable<null | IBloodpressure> => {
  const id = route.params.id;
  if (id) {
    return inject(BloodpressureService)
      .find(id)
      .pipe(
        mergeMap((bloodpressure: HttpResponse<IBloodpressure>) => {
          if (bloodpressure.body) {
            return of(bloodpressure.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bloodpressureResolve;
