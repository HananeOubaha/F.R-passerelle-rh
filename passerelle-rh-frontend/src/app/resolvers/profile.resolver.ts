import { inject } from '@angular/core';
import { ResolveFn, Router } from '@angular/router';
import { catchError, EMPTY } from 'rxjs';
import { AuthService, UserProfile } from '../services/auth.service';

/**
 * Resolver qui pré-charge le profil utilisateur avant d'entrer dans la route.
 * Si erreur 401/403, redirige vers /login.
 */
export const profileResolver: ResolveFn<UserProfile> = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getProfile().pipe(
    catchError((error) => {
      console.error('ProfileResolver: erreur chargement profil', error);
      if (error.status === 401 || error.status === 403) {
        authService.logout();
        router.navigate(['/login']);
      }
      return EMPTY;
    })
  );
};

