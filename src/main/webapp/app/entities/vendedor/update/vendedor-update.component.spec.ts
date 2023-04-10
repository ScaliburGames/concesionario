import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VendedorService } from '../service/vendedor.service';
import { IVendedor, Vendedor } from '../vendedor.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { VendedorUpdateComponent } from './vendedor-update.component';

describe('Vendedor Management Update Component', () => {
  let comp: VendedorUpdateComponent;
  let fixture: ComponentFixture<VendedorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vendedorService: VendedorService;
  let ventaService: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VendedorUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VendedorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendedorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vendedorService = TestBed.inject(VendedorService);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Venta query and add missing value', () => {
      const vendedor: IVendedor = { id: 456 };
      const venta: IVenta = { id: 94782 };
      vendedor.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 16258 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vendedor });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vendedor: IVendedor = { id: 456 };
      const venta: IVenta = { id: 17413 };
      vendedor.venta = venta;

      activatedRoute.data = of({ vendedor });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vendedor));
      expect(comp.ventasSharedCollection).toContain(venta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vendedor>>();
      const vendedor = { id: 123 };
      jest.spyOn(vendedorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendedor }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vendedorService.update).toHaveBeenCalledWith(vendedor);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vendedor>>();
      const vendedor = new Vendedor();
      jest.spyOn(vendedorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vendedor }));
      saveSubject.complete();

      // THEN
      expect(vendedorService.create).toHaveBeenCalledWith(vendedor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Vendedor>>();
      const vendedor = { id: 123 };
      jest.spyOn(vendedorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vendedor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vendedorService.update).toHaveBeenCalledWith(vendedor);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVentaById', () => {
      it('Should return tracked Venta primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVentaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
