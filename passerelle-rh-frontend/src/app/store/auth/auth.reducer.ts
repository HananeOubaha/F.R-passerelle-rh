import { createFeature, createReducer, on } from '@ngrx/store';
import {
  authHydrateFromStorage,
  authLogin,
  authLoginFailure,
  authLoginSuccess,
  authLogout
} from './auth.actions';
import { initialAuthState } from './auth.models';

export const authFeature = createFeature({
  name: 'auth',
  reducer: createReducer(
    initialAuthState,
    on(authHydrateFromStorage, (state, { user }) => ({
      ...state,
      user,
      initialized: true,
      error: null
    })),
    on(authLogin, (state) => ({
      ...state,
      loading: true,
      error: null
    })),
    on(authLoginSuccess, (state, { user }) => ({
      ...state,
      user,
      loading: false,
      error: null,
      initialized: true
    })),
    on(authLoginFailure, (state, { error }) => ({
      ...state,
      loading: false,
      error,
      user: null,
      initialized: true
    })),
    on(authLogout, (state) => ({
      ...state,
      user: null,
      loading: false,
      error: null,
      initialized: true
    }))
  )
});

