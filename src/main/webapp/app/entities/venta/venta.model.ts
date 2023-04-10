import dayjs from 'dayjs/esm';
import { IVendedor } from 'app/entities/vendedor/vendedor.model';
import { IComprador } from 'app/entities/comprador/comprador.model';
import { ICoche } from 'app/entities/coche/coche.model';
import { TipoPago } from 'app/entities/enumerations/tipo-pago.model';

export interface IVenta {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  tipoPago?: TipoPago | null;
  vendedores?: IVendedor[] | null;
  compradores?: IComprador[] | null;
  coches?: ICoche[] | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public tipoPago?: TipoPago | null,
    public vendedores?: IVendedor[] | null,
    public compradores?: IComprador[] | null,
    public coches?: ICoche[] | null
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
