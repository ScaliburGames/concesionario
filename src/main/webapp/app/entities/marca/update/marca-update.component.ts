import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMarca, Marca } from '../marca.model';
import { MarcaService } from '../service/marca.service';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';

@Component({
  selector: 'jhi-marca-update',
  templateUrl: './marca-update.component.html',
})
export class MarcaUpdateComponent implements OnInit {
  isSaving = false;

  cochesSharedCollection: ICoche[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    coche: [],
  });

  constructor(
    protected marcaService: MarcaService,
    protected cocheService: CocheService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ marca }) => {
      this.updateForm(marca);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const marca = this.createFromForm();
    if (marca.id !== undefined) {
      this.subscribeToSaveResponse(this.marcaService.update(marca));
    } else {
      this.subscribeToSaveResponse(this.marcaService.create(marca));
    }
  }

  trackCocheById(index: number, item: ICoche): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMarca>>): void {
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

  protected updateForm(marca: IMarca): void {
    this.editForm.patchValue({
      id: marca.id,
      nombre: marca.nombre,
      coche: marca.coche,
    });

    this.cochesSharedCollection = this.cocheService.addCocheToCollectionIfMissing(this.cochesSharedCollection, marca.coche);
  }

  protected loadRelationshipsOptions(): void {
    this.cocheService
      .query()
      .pipe(map((res: HttpResponse<ICoche[]>) => res.body ?? []))
      .pipe(map((coches: ICoche[]) => this.cocheService.addCocheToCollectionIfMissing(coches, this.editForm.get('coche')!.value)))
      .subscribe((coches: ICoche[]) => (this.cochesSharedCollection = coches));
  }

  protected createFromForm(): IMarca {
    return {
      ...new Marca(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      coche: this.editForm.get(['coche'])!.value,
    };
  }
}
