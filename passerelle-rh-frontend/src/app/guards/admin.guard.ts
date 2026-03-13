import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map, take } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { AuthFacade } from '../store/auth/auth.facade';

export const adminGuard: CanActivateFn = () => {
    const authFacade = inject(AuthFacade);
    const authService = inject(AuthService);
    const router = inject(Router);

    return authFacade.isAdmin$.pipe(
        take(1),
        map((isAdmin) => {
            const fallbackAdmin = authService.getUser()?.role === 'ADMIN';
            if (isAdmin || fallbackAdmin) {
                return true;
            }
            return router.parseUrl('/dashboard');
        })
    );
};
