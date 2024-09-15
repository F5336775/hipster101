import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBloodpresure } from '../bloodpresure.model';
import { BloodpresureService } from '../service/bloodpresure.service';

@Component({
  standalone: true,
  templateUrl: './bloodpresure-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BloodpresureDeleteDialogComponent {
  bloodpresure?: IBloodpresure;

  protected bloodpresureService = inject(BloodpresureService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bloodpresureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
