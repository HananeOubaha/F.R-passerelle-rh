import { createSelector } from '@ngrx/store';
import { missionsFeature } from './missions.reducer';

export const {
  selectMissions,
  selectTotalElements,
  selectTotalPages,
  selectCurrentPage,
  selectPageSize,
  selectLoading,
  selectError,
  selectSelectedStatut
} = missionsFeature;

export const selectMissionsState = missionsFeature.selectMissionsState;

export const selectMissionsViewModel = createSelector(
    selectMissions,
    selectTotalElements,
    selectCurrentPage,
    selectTotalPages,
    selectLoading,
    selectError,
    selectSelectedStatut,
    (missions, totalElements, page, pages, loading, error, statut) => ({
        missions,
        totalElements,
        page,
        pages,
        loading,
        error,
        statut
    })
);

