import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IPreference, NewPreference } from '../preference.model';

export type PartialUpdatePreference = Partial<IPreference> & Pick<IPreference, 'id'>;

export type EntityResponseType = HttpResponse<IPreference>;
export type EntityArrayResponseType = HttpResponse<IPreference[]>;

@Injectable({ providedIn: 'root' })
export class PreferenceService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/preferences');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/preferences/_search');

  create(preference: NewPreference): Observable<EntityResponseType> {
    return this.http.post<IPreference>(this.resourceUrl, preference, { observe: 'response' });
  }

  update(preference: IPreference): Observable<EntityResponseType> {
    return this.http.put<IPreference>(`${this.resourceUrl}/${this.getPreferenceIdentifier(preference)}`, preference, {
      observe: 'response',
    });
  }

  partialUpdate(preference: PartialUpdatePreference): Observable<EntityResponseType> {
    return this.http.patch<IPreference>(`${this.resourceUrl}/${this.getPreferenceIdentifier(preference)}`, preference, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPreference>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPreference[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPreference[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IPreference[]>()], asapScheduler)));
  }

  getPreferenceIdentifier(preference: Pick<IPreference, 'id'>): number {
    return preference.id;
  }

  comparePreference(o1: Pick<IPreference, 'id'> | null, o2: Pick<IPreference, 'id'> | null): boolean {
    return o1 && o2 ? this.getPreferenceIdentifier(o1) === this.getPreferenceIdentifier(o2) : o1 === o2;
  }

  addPreferenceToCollectionIfMissing<Type extends Pick<IPreference, 'id'>>(
    preferenceCollection: Type[],
    ...preferencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const preferences: Type[] = preferencesToCheck.filter(isPresent);
    if (preferences.length > 0) {
      const preferenceCollectionIdentifiers = preferenceCollection.map(preferenceItem => this.getPreferenceIdentifier(preferenceItem));
      const preferencesToAdd = preferences.filter(preferenceItem => {
        const preferenceIdentifier = this.getPreferenceIdentifier(preferenceItem);
        if (preferenceCollectionIdentifiers.includes(preferenceIdentifier)) {
          return false;
        }
        preferenceCollectionIdentifiers.push(preferenceIdentifier);
        return true;
      });
      return [...preferencesToAdd, ...preferenceCollection];
    }
    return preferenceCollection;
  }
}
