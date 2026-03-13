import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, of, switchMap, tap } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { authLogin, authLoginFailure, authLoginSuccess, authLogout } from './auth.actions';

@Injectable()
export class AuthEffects {
  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(authLogin),
      switchMap(({ credentials }) =>
        this.authService.login(credentials).pipe(
          map((user) => authLoginSuccess({ user })),
          catchError((error) =>
            of(
              authLoginFailure({
                error: error?.error?.message || 'Erreur de connexion'
              })
            )
          )
        )
      )
    )
  );

  logout$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(authLogout),
        tap(() => this.authService.logout())
      ),
    { dispatch: false }
  );

  constructor(private actions$: Actions, private authService: AuthService) {}
}

