import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComprador, Comprador } from '../comprador.model';

import { CompradorService } from './comprador.service';

describe('Comprador Service', () => {
  let service: CompradorService;
  let httpMock: HttpTestingController;
  let elemDefault: IComprador;
  let expectedResult: IComprador | IComprador[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompradorService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      apellidos: 'AAAAAAA',
      dni: 'AAAAAAA',
      fechaNacimiento: currentDate,
      fechaCarnet: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaCarnet: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Comprador', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaCarnet: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaNacimiento: currentDate,
          fechaCarnet: currentDate,
        },
        returnedFromService
      );

      service.create(new Comprador()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Comprador', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellidos: 'BBBBBB',
          dni: 'BBBBBB',
          fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaCarnet: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaNacimiento: currentDate,
          fechaCarnet: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Comprador', () => {
      const patchObject = Object.assign({}, new Comprador());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaNacimiento: currentDate,
          fechaCarnet: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Comprador', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellidos: 'BBBBBB',
          dni: 'BBBBBB',
          fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
          fechaCarnet: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaNacimiento: currentDate,
          fechaCarnet: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Comprador', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompradorToCollectionIfMissing', () => {
      it('should add a Comprador to an empty array', () => {
        const comprador: IComprador = { id: 123 };
        expectedResult = service.addCompradorToCollectionIfMissing([], comprador);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comprador);
      });

      it('should not add a Comprador to an array that contains it', () => {
        const comprador: IComprador = { id: 123 };
        const compradorCollection: IComprador[] = [
          {
            ...comprador,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompradorToCollectionIfMissing(compradorCollection, comprador);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Comprador to an array that doesn't contain it", () => {
        const comprador: IComprador = { id: 123 };
        const compradorCollection: IComprador[] = [{ id: 456 }];
        expectedResult = service.addCompradorToCollectionIfMissing(compradorCollection, comprador);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comprador);
      });

      it('should add only unique Comprador to an array', () => {
        const compradorArray: IComprador[] = [{ id: 123 }, { id: 456 }, { id: 61135 }];
        const compradorCollection: IComprador[] = [{ id: 123 }];
        expectedResult = service.addCompradorToCollectionIfMissing(compradorCollection, ...compradorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const comprador: IComprador = { id: 123 };
        const comprador2: IComprador = { id: 456 };
        expectedResult = service.addCompradorToCollectionIfMissing([], comprador, comprador2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(comprador);
        expect(expectedResult).toContain(comprador2);
      });

      it('should accept null and undefined values', () => {
        const comprador: IComprador = { id: 123 };
        expectedResult = service.addCompradorToCollectionIfMissing([], null, comprador, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(comprador);
      });

      it('should return initial array if no Comprador is added', () => {
        const compradorCollection: IComprador[] = [{ id: 123 }];
        expectedResult = service.addCompradorToCollectionIfMissing(compradorCollection, undefined, null);
        expect(expectedResult).toEqual(compradorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
