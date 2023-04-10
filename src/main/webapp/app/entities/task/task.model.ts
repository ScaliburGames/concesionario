export interface ITask {
  id?: number;
  salario?: number | null;
  numeroVenta?: number | null;
}

export class Task implements ITask {
  constructor(public id?: number, public salario?: number | null, public numeroVenta?: number | null) {}
}

export function getTaskIdentifier(task: ITask): number | undefined {
  return task.id;
}
