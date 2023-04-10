import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IComprador, Comprador } from '../comprador.model';
import { CompradorService } from '../service/comprador.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

@Component({
  selector: 'jhi-comprador-update',
  templateUrl: './comprador-update.component.html',
})
export class CompradorUpdateComponent implements OnInit {
  isSaving = false;

  ventasSharedCollection: IVenta[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellidos: [],
    dni: [],
    fechaNacimiento: [],
    fechaCarnet: [],
    venta: [],
  });

  constructor(
    protected compradorService: CompradorService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comprador }) => {
      if (comprador.id === undefined) {
        const today = dayjs().startOf('day');
        comprador.fechaNacimiento = today;
        comprador.fechaCarnet = today;
      }

      this.updateForm(comprador);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comprador = this.createFromForm();
    if (comprador.id !== undefined) {
      this.subscribeToSaveResponse(this.compradorService.update(comprador));
    } else {
      this.subscribeToSaveResponse(this.compradorService.create(comprador));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComprador>>): void {
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

  protected updateForm(comprador: IComprador): void {
    this.editForm.patchValue({
      id: comprador.id,
      nombre: comprador.nombre,
      apellidos: comprador.apellidos,
      dni: comprador.dni,
      fechaNacimiento: comprador.fechaNacimiento ? comprador.fechaNacimiento.format(DATE_TIME_FORMAT) : null,
      fechaCarnet: comprador.fechaCarnet ? comprador.fechaCarnet.format(DATE_TIME_FORMAT) : null,
      venta: comprador.venta,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, comprador.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): IComprador {
    return {
      ...new Comprador(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      dni: this.editForm.get(['dni'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value
        ? dayjs(this.editForm.get(['fechaNacimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaCarnet: this.editForm.get(['fechaCarnet'])!.value
        ? dayjs(this.editForm.get(['fechaCarnet'])!.value, DATE_TIME_FORMAT)
        : undefined,
      venta: this.editForm.get(['venta'])!.value,
    };
  }
}
