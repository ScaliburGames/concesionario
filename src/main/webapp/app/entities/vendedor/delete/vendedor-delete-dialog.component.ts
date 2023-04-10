import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVendedor } from '../vendedor.model';
import { VendedorService } from '../service/vendedor.service';

@Component({
  templateUrl: './vendedor-delete-dialog.component.html',
})
export class VendedorDeleteDialogComponent {
  vendedor?: IVendedor;

  constructor(protected vendedorService: VendedorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vendedorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
