import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBloodpressure } from '../bloodpressure.model';

@Component({
  standalone: true,
  selector: 'jhi-bloodpressure-detail',
  templateUrl: './bloodpressure-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BloodpressureDetailComponent {
  bloodpressure = input<IBloodpressure | null>(null);

  previousState(): void {
    window.history.back();
  }
}
