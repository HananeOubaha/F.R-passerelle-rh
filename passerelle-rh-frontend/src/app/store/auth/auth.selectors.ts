import { createSelector } from '@ngrx/store';
import { authFeature } from './auth.reducer';

export const selectAuthState = authFeature.selectAuthState;
export const selectAuthUser = authFeature.selectUser;
export const selectAuthLoading = authFeature.selectLoading;
export const selectAuthError = authFeature.selectError;
export const selectAuthInitialized = authFeature.selectInitialized;

export const selectIsAuthenticated = createSelector(
  selectAuthUser,
  (user) => !!user?.accessToken
);

export const selectIsAdmin = createSelector(
  selectAuthUser,
  (user) => user?.role === 'ADMIN'
);

