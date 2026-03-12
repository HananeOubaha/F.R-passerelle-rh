import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { catchError, of } from 'rxjs';
import { AuthService, Mission } from '../services/auth.service';

/**
 * Resolver qui pré-charge la liste des missions de l'utilisateur connecté.
 * En cas d'erreur, retourne un tableau vide sans bloquer la navigation.
 */
export const missionsResolver: ResolveFn<Mission[]> = (route, state) => {
  const authService = inject(AuthService);

  return authService.getMissions().pipe(
    catchError((error) => {
      console.error('MissionsResolver: erreur chargement missions', error);
      return of([]);
    })
  );
};

