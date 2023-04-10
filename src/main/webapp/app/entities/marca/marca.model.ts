import { ICoche } from 'app/entities/coche/coche.model';
import { IModelo } from 'app/entities/modelo/modelo.model';

export interface IMarca {
  id?: number;
  nombre?: string | null;
  coche?: ICoche | null;
  modelos?: IModelo[] | null;
}

export class Marca implements IMarca {
  constructor(public id?: number, public nombre?: string | null, public coche?: ICoche | null, public modelos?: IModelo[] | null) {}
}

export function getMarcaIdentifier(marca: IMarca): number | undefined {
  return marca.id;
}
