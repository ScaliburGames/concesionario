import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVendedor, Vendedor } from '../vendedor.model';
import { VendedorService } from '../service/vendedor.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-vendedor-update',
  templateUrl: './vendedor-update.component.html',
})
export class VendedorUpdateComponent implements OnInit {
  isSaving = false;

  ventasSharedCollection: IVenta[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellidos: [],
    codigoEmpleado: [],
    dni: [],
    venta: [],
  });

  constructor(
    protected vendedorService: VendedorService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendedor }) => {
      this.updateForm(vendedor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendedor = this.createFromForm();
    if (vendedor.id !== undefined) {
      this.subscribeToSaveResponse(this.vendedorService.update(vendedor));
    } else {
      this.subscribeToSaveResponse(this.vendedorService.create(vendedor));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendedor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vendedor: IVendedor): void {
    this.editForm.patchValue({
      id: vendedor.id,
      nombre: vendedor.nombre,
      apellidos: vendedor.apellidos,
      codigoEmpleado: vendedor.codigoEmpleado,
      dni: vendedor.dni,
      venta: vendedor.venta,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, vendedor.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): IVendedor {
    return {
      ...new Vendedor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      codigoEmpleado: this.editForm.get(['codigoEmpleado'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      venta: this.editForm.get(['venta'])!.value,
    };
  }
}
