import { createFeature, createReducer, on } from '@ngrx/store';
import { initialMissionsState } from './missions.models';
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

export const missionsFeature = createFeature({
  name: 'missions',
  reducer: createReducer(
    initialMissionsState,
    on(loadMissions, (state) => ({
      ...state,
      loading: true,
      error: null
    })),
    on(loadMissionsSuccess, (state, { response }) => ({
      ...state,
      missions: response.content,
      totalElements: response.totalElements,
      totalPages: response.totalPages,
      currentPage: response.number,
      loading: false,
      error: null
    })),
    on(loadMissionsFailure, (state, { error }) => ({
      ...state,
      loading: false,
      error
    })),
    on(changePage, (state, { page }) => ({
      ...state,
      currentPage: page
    })),
    on(changeStatutFilter, (state, { statut }) => ({
        ...state,
        selectedStatut: statut,
        currentPage: 0 // Reset page to 0 when filter changes
    })),
    // Optimistic UI for delete ? Or wait for effects
    on(deleteMission, (state) => ({
        ...state,
        loading: true,
        error: null
    })),
    on(deleteMissionSuccess, (state, { id }) => ({
        ...state,
        loading: false,
        missions: state.missions.filter(m => m.id !== id),
        totalElements: state.totalElements - 1
        // Could be complex to adjust pagina without reload, but fine for now
    })),
    on(deleteMissionFailure, (state, { error }) => ({
        ...state,
        loading: false,
        error
    })),
    on(createMissionSuccess, (state, { mission }) => ({
        ...state,
        // Optimistic add if valid locally, but we often reload
        // missions: [mission, ...state.missions],
        // totalElements: state.totalElements + 1
    }))
  )
});

