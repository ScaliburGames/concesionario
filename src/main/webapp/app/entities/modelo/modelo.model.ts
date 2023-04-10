import { IMarca } from 'app/entities/marca/marca.model';

export interface IModelo {
  id?: number;
  nombre?: string | null;
  marca?: IMarca | null;
}

export class Modelo implements IModelo {
  constructor(public id?: number, public nombre?: string | null, public marca?: IMarca | null) {}
}

export function getModeloIdentifier(modelo: IModelo): number | undefined {
  return modelo.id;
}
