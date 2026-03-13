import { authHydrateFromStorage, authLogin, authLoginFailure, authLoginSuccess, authLogout } from './auth.actions';
import { authFeature } from './auth.reducer';
import { initialAuthState } from './auth.models';

describe('authReducer', () => {
  it('should set loading true on login', () => {
    const nextState = authFeature.reducer(initialAuthState, authLogin({ credentials: { email: 'a@b.com', password: '123456' } }));
    expect(nextState.loading).toBeTrue();
    expect(nextState.error).toBeNull();
  });

  it('should store user on login success', () => {
    const user = {
      accessToken: 'a',
      refreshToken: 'r',
      tokenType: 'Bearer',
      userId: 1,
      email: 'a@b.com',
      role: 'USER'
    };
    const nextState = authFeature.reducer(initialAuthState, authLoginSuccess({ user }));
    expect(nextState.user).toEqual(user);
    expect(nextState.loading).toBeFalse();
  });

  it('should clear session on logout', () => {
    const stateWithUser = {
      ...initialAuthState,
      user: {
        accessToken: 'a',
        refreshToken: 'r',
        tokenType: 'Bearer',
        userId: 1,
        email: 'a@b.com',
        role: 'ADMIN'
      },
      loading: true,
      error: 'x',
      initialized: true
    };
    const nextState = authFeature.reducer(stateWithUser, authLogout());
    expect(nextState.user).toBeNull();
    expect(nextState.loading).toBeFalse();
    expect(nextState.error).toBeNull();
  });

  it('should hydrate from storage', () => {
    const user = {
      accessToken: 'a',
      refreshToken: 'r',
      tokenType: 'Bearer',
      userId: 2,
      email: 'user@test.com',
      role: 'USER'
    };
    const nextState = authFeature.reducer(initialAuthState, authHydrateFromStorage({ user }));
    expect(nextState.user).toEqual(user);
    expect(nextState.initialized).toBeTrue();
  });

  it('should set error on login failure', () => {
    const nextState = authFeature.reducer(initialAuthState, authLoginFailure({ error: 'Erreur de connexion' }));
    expect(nextState.error).toBe('Erreur de connexion');
    expect(nextState.loading).toBeFalse();
  });
});

