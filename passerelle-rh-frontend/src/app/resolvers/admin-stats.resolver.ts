import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AdminService, AdminStats } from '../services/admin.service';

/**
 * Resolver qui pré-charge les statistiques admin avant d'afficher le dashboard.
 * En cas d'erreur, retourne null sans bloquer la navigation.
 */
export const adminStatsResolver: ResolveFn<AdminStats | null> = (route, state) => {
  const adminService = inject(AdminService);

  return adminService.getStats().pipe(
    catchError((error) => {
      console.error('AdminStatsResolver: erreur chargement stats', error);
      return of(null);
    })
  );
};

