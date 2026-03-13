import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { AuthFacade } from '../store/auth/auth.facade';

export const authGuard: CanActivateFn = () => {
    const authFacade = inject(AuthFacade);
    const authService = inject(AuthService);
    const router = inject(Router);

    return authFacade.isAuthenticated$.pipe(
        take(1),
        map((isAuthenticated) => {
            if (isAuthenticated || authService.isLoggedIn()) {
                return true;
            }
            return router.parseUrl('/login');
        })
    );
};
