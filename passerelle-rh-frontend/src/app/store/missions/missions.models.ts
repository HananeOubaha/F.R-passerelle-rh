import { Mission, MissionStatut } from '../../services/auth.service';

export interface MissionsState {
  missions: Mission[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
  loading: boolean;
  error: string | null;
  selectedStatut: MissionStatut | null;
}

export const initialMissionsState: MissionsState = {
  missions: [],
  totalElements: 0,
  totalPages: 0,
  currentPage: 0,
  pageSize: 9, // ou 10, adapte selon le design
  loading: false,
  error: null,
  selectedStatut: null
};

