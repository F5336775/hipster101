import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, map, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWeight, NewWeight } from '../weight.model';

export type PartialUpdateWeight = Partial<IWeight> & Pick<IWeight, 'id'>;

type RestOf<T extends IWeight | NewWeight> = Omit<T, 'datetime'> & {
  datetime?: string | null;
};

export type RestWeight = RestOf<IWeight>;

export type NewRestWeight = RestOf<NewWeight>;

export type PartialUpdateRestWeight = RestOf<PartialUpdateWeight>;

export type EntityResponseType = HttpResponse<IWeight>;
export type EntityArrayResponseType = HttpResponse<IWeight[]>;

@Injectable({ providedIn: 'root' })
export class WeightService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weights');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/weights/_search');

  create(weight: NewWeight): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weight);
    return this.http
      .post<RestWeight>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(weight: IWeight): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weight);
    return this.http
      .put<RestWeight>(`${this.resourceUrl}/${this.getWeightIdentifier(weight)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(weight: PartialUpdateWeight): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weight);
    return this.http
      .patch<RestWeight>(`${this.resourceUrl}/${this.getWeightIdentifier(weight)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWeight>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWeight[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestWeight[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),

      catchError(() => scheduled([new HttpResponse<IWeight[]>()], asapScheduler)),
    );
  }

  getWeightIdentifier(weight: Pick<IWeight, 'id'>): number {
    return weight.id;
  }

  compareWeight(o1: Pick<IWeight, 'id'> | null, o2: Pick<IWeight, 'id'> | null): boolean {
    return o1 && o2 ? this.getWeightIdentifier(o1) === this.getWeightIdentifier(o2) : o1 === o2;
  }

  addWeightToCollectionIfMissing<Type extends Pick<IWeight, 'id'>>(
    weightCollection: Type[],
    ...weightsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const weights: Type[] = weightsToCheck.filter(isPresent);
    if (weights.length > 0) {
      const weightCollectionIdentifiers = weightCollection.map(weightItem => this.getWeightIdentifier(weightItem));
      const weightsToAdd = weights.filter(weightItem => {
        const weightIdentifier = this.getWeightIdentifier(weightItem);
        if (weightCollectionIdentifiers.includes(weightIdentifier)) {
          return false;
        }
        weightCollectionIdentifiers.push(weightIdentifier);
        return true;
      });
      return [...weightsToAdd, ...weightCollection];
    }
    return weightCollection;
  }

  protected convertDateFromClient<T extends IWeight | NewWeight | PartialUpdateWeight>(weight: T): RestOf<T> {
    return {
      ...weight,
      datetime: weight.datetime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWeight: RestWeight): IWeight {
    return {
      ...restWeight,
      datetime: restWeight.datetime ? dayjs(restWeight.datetime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWeight>): HttpResponse<IWeight> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWeight[]>): HttpResponse<IWeight[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
