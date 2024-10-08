import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBloodpressure } from '../bloodpressure.model';
import { BloodpressureService } from '../service/bloodpressure.service';

@Component({
  standalone: true,
  templateUrl: './bloodpressure-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BloodpressureDeleteDialogComponent {
  bloodpressure?: IBloodpressure;

  protected bloodpressureService = inject(BloodpressureService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bloodpressureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
