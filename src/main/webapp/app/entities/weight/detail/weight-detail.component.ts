import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IWeight } from '../weight.model';

@Component({
  standalone: true,
  selector: 'jhi-weight-detail',
  templateUrl: './weight-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WeightDetailComponent {
  weight = input<IWeight | null>(null);

  previousState(): void {
    window.history.back();
  }
}
