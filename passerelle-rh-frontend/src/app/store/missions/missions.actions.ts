import { createAction, props } from '@ngrx/store';
import { Mission, MissionStatut, Page } from '../../services/auth.service';

export const loadMissions = createAction(
  '[Missions] Load Missions',
  props<{ page?: number; statut?: MissionStatut | null }>()
);

export const loadMissionsSuccess = createAction(
  '[Missions] Load Missions Success',
  props<{ response: Page<Mission> }>()
);

export const loadMissionsFailure = createAction(
  '[Missions] Load Missions Failure',
  props<{ error: string }>()
);

export const changePage = createAction(
  '[Missions] Change Page',
  props<{ page: number }>()
);

export const changeStatutFilter = createAction(
  '[Missions] Change Statut Filter',
  props<{ statut: MissionStatut | null }>()
);

export const deleteMission = createAction(
    '[Missions] Delete Mission',
    props<{ id: number }>()
);

export const deleteMissionSuccess = createAction(
    '[Missions] Delete Mission Success',
    props<{ id: number }>()
);

export const deleteMissionFailure = createAction(
    '[Missions] Delete Mission Failure',
    props<{ error: string }>()
);

export const createMissionSuccess = createAction(
    '[Missions] Create Mission Success',
    props<{ mission: Mission }>()
);

