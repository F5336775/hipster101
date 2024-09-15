import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IBloodpresure, NewBloodpresure } from '../bloodpresure.model';

export type EntityResponseType = HttpResponse<IBloodpresure>;
export type EntityArrayResponseType = HttpResponse<IBloodpresure[]>;

@Injectable({ providedIn: 'root' })
export class BloodpresureService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bloodpresures');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/bloodpresures/_search');

  create(bloodpresure: NewBloodpresure): Observable<EntityResponseType> {
    return this.http.post<IBloodpresure>(this.resourceUrl, bloodpresure, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBloodpresure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBloodpresure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBloodpresure[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IBloodpresure[]>()], asapScheduler)));
  }

  getBloodpresureIdentifier(bloodpresure: Pick<IBloodpresure, 'id'>): number {
    return bloodpresure.id;
  }

  compareBloodpresure(o1: Pick<IBloodpresure, 'id'> | null, o2: Pick<IBloodpresure, 'id'> | null): boolean {
    return o1 && o2 ? this.getBloodpresureIdentifier(o1) === this.getBloodpresureIdentifier(o2) : o1 === o2;
  }

  addBloodpresureToCollectionIfMissing<Type extends Pick<IBloodpresure, 'id'>>(
    bloodpresureCollection: Type[],
    ...bloodpresuresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bloodpresures: Type[] = bloodpresuresToCheck.filter(isPresent);
    if (bloodpresures.length > 0) {
      const bloodpresureCollectionIdentifiers = bloodpresureCollection.map(bloodpresureItem =>
        this.getBloodpresureIdentifier(bloodpresureItem),
      );
      const bloodpresuresToAdd = bloodpresures.filter(bloodpresureItem => {
        const bloodpresureIdentifier = this.getBloodpresureIdentifier(bloodpresureItem);
        if (bloodpresureCollectionIdentifiers.includes(bloodpresureIdentifier)) {
          return false;
        }
        bloodpresureCollectionIdentifiers.push(bloodpresureIdentifier);
        return true;
      });
      return [...bloodpresuresToAdd, ...bloodpresureCollection];
    }
    return bloodpresureCollection;
  }
}
