import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { MissionStatut } from '../../services/auth.service';
import {
  changePage,
  changeStatutFilter,
  deleteMission,
  loadMissions
} from './missions.actions';
import { selectMissionsViewModel } from './missions.selectors';

@Injectable({ providedIn: 'root' })
export class MissionsFacade {
  readonly vm$ = this.store.select(selectMissionsViewModel);

  constructor(private store: Store) {}

  loadMissions(): void {
    this.store.dispatch(loadMissions({}));
  }

  changePage(page: number): void {
    this.store.dispatch(changePage({ page }));
  }

  filterByStatut(statut: MissionStatut | null): void {
    this.store.dispatch(changeStatutFilter({ statut }));
  }

  deleteMission(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette mission ?')) {
        this.store.dispatch(deleteMission({ id }));
    }
  }
}

