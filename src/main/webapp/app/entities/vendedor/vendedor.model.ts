import { IVenta } from 'app/entities/venta/venta.model';

export interface IVendedor {
  id?: number;
  nombre?: string | null;
  apellidos?: string | null;
  codigoEmpleado?: string | null;
  dni?: string | null;
  venta?: IVenta | null;
}

export class Vendedor implements IVendedor {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellidos?: string | null,
    public codigoEmpleado?: string | null,
    public dni?: string | null,
    public venta?: IVenta | null
  ) {}
}

export function getVendedorIdentifier(vendedor: IVendedor): number | undefined {
  return vendedor.id;
}
