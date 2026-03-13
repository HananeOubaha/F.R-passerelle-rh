import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AuthResponse, LoginRequest } from '../../services/auth.service';
import {
  authHydrateFromStorage,
  authLogin,
  authLogout
} from './auth.actions';
import {
  selectAuthError,
  selectAuthLoading,
  selectAuthUser,
  selectIsAdmin,
  selectIsAuthenticated
} from './auth.selectors';

@Injectable({ providedIn: 'root' })
export class AuthFacade {
  readonly user$ = this.store.select(selectAuthUser);
  readonly loading$ = this.store.select(selectAuthLoading);
  readonly error$ = this.store.select(selectAuthError);
  readonly isAuthenticated$ = this.store.select(selectIsAuthenticated);
  readonly isAdmin$ = this.store.select(selectIsAdmin);

  constructor(private store: Store) {}

  hydrateFromStorage(user: AuthResponse | null): void {
    this.store.dispatch(authHydrateFromStorage({ user }));
  }

  login(credentials: LoginRequest): void {
    this.store.dispatch(authLogin({ credentials }));
  }

  logout(): void {
    this.store.dispatch(authLogout());
  }
}

