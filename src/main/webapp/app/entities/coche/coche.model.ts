import { IVenta } from 'app/entities/venta/venta.model';
import { IMarca } from 'app/entities/marca/marca.model';

export interface ICoche {
  id?: number;
  matricula?: string | null;
  color?: string | null;
  precio?: number | null;
  vendido?: boolean | null;
  venta?: IVenta | null;
  marcas?: IMarca[] | null;
}

export class Coche implements ICoche {
  constructor(
    public id?: number,
    public matricula?: string | null,
    public color?: string | null,
    public precio?: number | null,
    public vendido?: boolean | null,
    public venta?: IVenta | null,
    public marcas?: IMarca[] | null
  ) {
    this.vendido = this.vendido ?? false;
  }
}

export function getCocheIdentifier(coche: ICoche): number | undefined {
  return coche.id;
}
