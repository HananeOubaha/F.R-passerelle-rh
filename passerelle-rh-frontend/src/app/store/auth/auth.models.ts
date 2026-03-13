import { AuthResponse } from '../../services/auth.service';

export interface AuthState {
  user: AuthResponse | null;
  loading: boolean;
  error: string | null;
  initialized: boolean;
}

export const initialAuthState: AuthState = {
  user: null,
  loading: false,
  error: null,
  initialized: false
};

