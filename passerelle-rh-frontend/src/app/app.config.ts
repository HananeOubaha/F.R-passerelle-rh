import { APP_INITIALIZER, ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideStore, provideState } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';

import { routes } from './app.routes';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { authFeature } from './store/auth/auth.reducer';
import { AuthEffects } from './store/auth/auth.effects';
import { missionsFeature } from './store/missions/missions.reducer';
import { MissionsEffects } from './store/missions/missions.effects';
import { AuthFacade } from './store/auth/auth.facade';
import { AuthService } from './services/auth.service';

function initAuthSession(authFacade: AuthFacade, authService: AuthService) {
  return () => {
    authFacade.hydrateFromStorage(authService.getUser());
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    provideStore(),
    provideState(authFeature),
    provideState(missionsFeature),
    provideEffects([AuthEffects, MissionsEffects]),
    {
      provide: APP_INITIALIZER,
      useFactory: initAuthSession,
      deps: [AuthFacade, AuthService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    provideCharts(withDefaultRegisterables())
  ]
};
