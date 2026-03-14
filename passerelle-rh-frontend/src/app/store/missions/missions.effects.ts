import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { catchError, map, mergeMap, of, tap, withLatestFrom } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import {
  changePage,
  changeStatutFilter,
  createMissionSuccess,
  deleteMission,
  deleteMissionFailure,
  deleteMissionSuccess,
  loadMissions,
  loadMissionsFailure,
  loadMissionsSuccess
} from './missions.actions';
import { selectMissionsState } from './missions.selectors';

@Injectable()
export class MissionsEffects {

  loadMissions$ = createEffect(() =>
    this.actions$.pipe(
      ofType(loadMissions),
      withLatestFrom(this.store.select(selectMissionsState)),
      mergeMap(([action, state]) => {
        // Use params from action if present (optional), else from state
        // In our pattern, action triggers reload based on state mostly.
        // But for initial load we might want defaults.
        // 'page' in action is optional, etc.
        // Actually, let's trust state updated by changePage/changeFilter reducers.
        const page = action.page ?? state.currentPage;
        const statut = action.statut ?? state.selectedStatut;

        // If action provided overrides, we rely on reducer handling them.. oh wait
        // loadMissions reducer handles loading=true. It doesn't update page/statut.
        // So we strictly use state unless we specifically pass args.
        // Let's stick to: changePage updates state -> triggers loadMissions.
        // loadMissions uses state.

        return this.authService.getMissionsPaginated(
            state.currentPage,
            state.pageSize,
            'dateDebut,desc',
            state.selectedStatut || undefined
        ).pipe(
          map((response) => loadMissionsSuccess({ response })),
          catchError((error) =>
            of(loadMissionsFailure({ error: error.message || 'Erreur chargement missions' }))
          )
        );
      })
    )
  );

  refreshWhenParamsChange$ = createEffect(() =>
    this.actions$.pipe(
        ofType(changePage, changeStatutFilter),
        map(() => loadMissions({}))
    )
  );

  deleteMission$ = createEffect(() =>
    this.actions$.pipe(
        ofType(deleteMission),
        mergeMap(({ id }) =>
            this.authService.deleteMission(id).pipe(
                map(() => deleteMissionSuccess({ id })),
                catchError((error) => of(deleteMissionFailure({ error: error.message })))
            )
        )
    )
  );

  reloadAfterModification$ = createEffect(() =>
    this.actions$.pipe(
        ofType(deleteMissionSuccess, createMissionSuccess),
        map(() => loadMissions({}))
    )
  );

  constructor(
    private actions$: Actions,
    private authService: AuthService,
    private store: Store
  ) {}
}

