import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPreference } from '../preference.model';

@Component({
  standalone: true,
  selector: 'jhi-preference-detail',
  templateUrl: './preference-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PreferenceDetailComponent {
  preference = input<IPreference | null>(null);

  previousState(): void {
    window.history.back();
  }
}
