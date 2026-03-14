import { missionsFeature } from './missions.reducer';
import { initialMissionsState } from './missions.models';
import { changePage, loadMissions, loadMissionsSuccess, loadMissionsFailure } from './missions.actions';

describe('missionsReducer', () => {
  it('should set loading to true on loadMissions', () => {
    const nextState = missionsFeature.reducer(initialMissionsState, loadMissions({}));
    expect(nextState.loading).toBeTrue();
    expect(nextState.error).toBeNull();
  });

  it('should update state on loadMissionsSuccess', () => {
    const response = {
      content: [{ id: 1, titre: 'Mission 1' } as any],
      totalElements: 10,
      totalPages: 2,
      size: 5,
      number: 0,
      first: true,
      last: false,
      empty: false
    };
    const nextState = missionsFeature.reducer(
        { ...initialMissionsState, loading: true },
        loadMissionsSuccess({ response })
    );
    expect(nextState.missions.length).toBe(1);
    expect(nextState.totalElements).toBe(10);
    expect(nextState.currentPage).toBe(0);
    expect(nextState.loading).toBeFalse();
  });

  it('should update error on loadMissionsFailure', () => {
    const nextState = missionsFeature.reducer(
        { ...initialMissionsState, loading: true },
        loadMissionsFailure({ error: 'Erreur API' })
    );
    expect(nextState.loading).toBeFalse();
    expect(nextState.error).toBe('Erreur API');
  });

  it('should update page on changePage', () => {
    const nextState = missionsFeature.reducer(initialMissionsState, changePage({ page: 2 }));
    expect(nextState.currentPage).toBe(2);
  });
});

