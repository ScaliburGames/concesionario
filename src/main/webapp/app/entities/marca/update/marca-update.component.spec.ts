import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MarcaService } from '../service/marca.service';
import { IMarca, Marca } from '../marca.model';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';

import { MarcaUpdateComponent } from './marca-update.component';

describe('Marca Management Update Component', () => {
  let comp: MarcaUpdateComponent;
  let fixture: ComponentFixture<MarcaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let marcaService: MarcaService;
  let cocheService: CocheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MarcaUpdateComponent],
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
      .overrideTemplate(MarcaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MarcaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    marcaService = TestBed.inject(MarcaService);
    cocheService = TestBed.inject(CocheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Coche query and add missing value', () => {
      const marca: IMarca = { id: 456 };
      const coche: ICoche = { id: 69830 };
      marca.coche = coche;

      const cocheCollection: ICoche[] = [{ id: 28189 }];
      jest.spyOn(cocheService, 'query').mockReturnValue(of(new HttpResponse({ body: cocheCollection })));
      const additionalCoches = [coche];
      const expectedCollection: ICoche[] = [...additionalCoches, ...cocheCollection];
      jest.spyOn(cocheService, 'addCocheToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      expect(cocheService.query).toHaveBeenCalled();
      expect(cocheService.addCocheToCollectionIfMissing).toHaveBeenCalledWith(cocheCollection, ...additionalCoches);
      expect(comp.cochesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const marca: IMarca = { id: 456 };
      const coche: ICoche = { id: 15047 };
      marca.coche = coche;

      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(marca));
      expect(comp.cochesSharedCollection).toContain(coche);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Marca>>();
      const marca = { id: 123 };
      jest.spyOn(marcaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marca }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(marcaService.update).toHaveBeenCalledWith(marca);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Marca>>();
      const marca = new Marca();
      jest.spyOn(marcaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: marca }));
      saveSubject.complete();

      // THEN
      expect(marcaService.create).toHaveBeenCalledWith(marca);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Marca>>();
      const marca = { id: 123 };
      jest.spyOn(marcaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ marca });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(marcaService.update).toHaveBeenCalledWith(marca);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCocheById', () => {
      it('Should return tracked Coche primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCocheById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
