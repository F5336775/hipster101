import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBloodpresure } from '../bloodpresure.model';

@Component({
  standalone: true,
  selector: 'jhi-bloodpresure-detail',
  templateUrl: './bloodpresure-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BloodpresureDetailComponent {
  bloodpresure = input<IBloodpresure | null>(null);

  previousState(): void {
    window.history.back();
  }
}
