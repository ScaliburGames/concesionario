import dayjs from 'dayjs/esm';
import { IVenta } from 'app/entities/venta/venta.model';

export interface IComprador {
  id?: number;
  nombre?: string | null;
  apellidos?: string | null;
  dni?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  fechaCarnet?: dayjs.Dayjs | null;
  venta?: IVenta | null;
}

export class Comprador implements IComprador {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellidos?: string | null,
    public dni?: string | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public fechaCarnet?: dayjs.Dayjs | null,
    public venta?: IVenta | null
  ) {}
}

export function getCompradorIdentifier(comprador: IComprador): number | undefined {
  return comprador.id;
}
