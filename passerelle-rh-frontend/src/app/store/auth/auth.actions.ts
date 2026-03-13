import { createAction, props } from '@ngrx/store';
import { AuthResponse, LoginRequest } from '../../services/auth.service';

export const authHydrateFromStorage = createAction(
  '[Auth] Hydrate From Storage',
  props<{ user: AuthResponse | null }>()
);

export const authLogin = createAction(
  '[Auth] Login',
  props<{ credentials: LoginRequest }>()
);

export const authLoginSuccess = createAction(
  '[Auth] Login Success',
  props<{ user: AuthResponse }>()
);

export const authLoginFailure = createAction(
  '[Auth] Login Failure',
  props<{ error: string }>()
);

export const authLogout = createAction('[Auth] Logout');

