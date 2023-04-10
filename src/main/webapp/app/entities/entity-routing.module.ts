import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'marca',
        data: { pageTitle: 'Marcas' },
        loadChildren: () => import('./marca/marca.module').then(m => m.MarcaModule),
      },
      {
        path: 'modelo',
        data: { pageTitle: 'Modelos' },
        loadChildren: () => import('./modelo/modelo.module').then(m => m.ModeloModule),
      },
      {
        path: 'coche',
        data: { pageTitle: 'Coches' },
        loadChildren: () => import('./coche/coche.module').then(m => m.CocheModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'Ventas' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'Tasks' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'comprador',
        data: { pageTitle: 'Compradors' },
        loadChildren: () => import('./comprador/comprador.module').then(m => m.CompradorModule),
      },
      {
        path: 'vendedor',
        data: { pageTitle: 'Vendedors' },
        loadChildren: () => import('./vendedor/vendedor.module').then(m => m.VendedorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
