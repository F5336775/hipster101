import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPreference } from '../preference.model';
import { PreferenceService } from '../service/preference.service';

@Component({
  standalone: true,
  templateUrl: './preference-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PreferenceDeleteDialogComponent {
  preference?: IPreference;

  protected preferenceService = inject(PreferenceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.preferenceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
